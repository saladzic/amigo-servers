package com.amigoservers.backend.user;

import com.amigoservers.backend.util.driver.Db;
import com.amigoservers.backend.util.exception.LoginFailedException;
import com.amigoservers.backend.util.main.Config;
import com.amigoservers.backend.util.main.RandomString;
import com.amigoservers.backend.util.mvc.Model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Session extends Model {
    private String sessionId;

    public Session() {
    }

    public Session(String sessionid) {
        this.sessionId = sessionid;
    }

    public String getId() {
        return sessionId;
    }

    public boolean isValid() {
        try {
            Config config = new Config();
            int hours = config.getSessionExpiration();

            Db db = new Db();
            PreparedStatement preparedStatement = db.getDb()
                    .prepareStatement("SELECT active FROM amigo_session WHERE token=? AND active=1 AND last_action+" + hours +">UNIX_TIMESTAMP()");
            preparedStatement.setString(1, sessionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public void logout() {
        try {
            Db db = new Db();
            Connection connection = db.getDb();
            PreparedStatement stmt = connection.prepareStatement("UPDATE amigo_session SET active=0 WHERE token=?");
            stmt.setString(1, sessionId);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Session login(String username, String password, String userAgent, String ip) throws LoginFailedException {
        try {
            Db db = new Db();
            PreparedStatement preparedStatement = db.getDb()
                    .prepareStatement("SELECT id FROM amigo_user WHERE username=? AND password=?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, generateHash(password));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                return create(userId, userAgent, ip);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        throw new LoginFailedException();
    }

    private Session create(int userId, String userAgent, String ip) {
        String token = RandomString.random(15);
        try {
            Db db = new Db();
            Connection connection = db.getDb();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO amigo_session " +
                    "(token,user_id,user_agent,ip_address,created_at,last_action) VALUES (?,?,?,?,UNIX_TIMESTAMP(),UNIX_TIMESTAMP())");
            stmt.setString(1, token);
            stmt.setInt(2, userId);
            stmt.setString(3, userAgent);
            stmt.setString(4, ip);
            stmt.executeUpdate();
            sessionId = token;
            return this;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private String generateHash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hash = md.digest(str.getBytes(StandardCharsets.UTF_8));
            return convertToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts the given byte[] to a hex string.
     * @param raw the byte[] to convert
     * @return the string the given byte[] represents
     */
    private String convertToHex(byte[] raw) {
        StringBuilder sb = new StringBuilder();
        for (byte b : raw) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
