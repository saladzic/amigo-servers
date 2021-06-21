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
            int hours = 3600 * config.getSessionExpiration();

            PreparedStatement preparedStatement = getDb()
                    .prepareStatement("SELECT active FROM amigo_session WHERE token=? AND active=1 AND last_action+" + hours +">UNIX_TIMESTAMP()");
            preparedStatement.setString(1, sessionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean isAdmin() {
        try {
            int userId = getUserId();
            if (userId == 0) {
                return false;
            }
            PreparedStatement preparedStatement = getDb()
                    .prepareStatement("SELECT admin FROM amigo_user WHERE id=?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
            return resultSet.getBoolean("admin");
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

    public Session login(String email, String password, String userAgent, String ip) throws LoginFailedException {
        try {
            Db db = new Db();
            PreparedStatement preparedStatement = db.getDb()
                    .prepareStatement("SELECT id FROM amigo_user WHERE email=? AND password=?");
            preparedStatement.setString(1, email);
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

    public boolean register(String email, String password) {
        try {
            Db db = new Db();
            Connection connection = db.getDb();

            // Check if email is already registered
            PreparedStatement stmt = connection.prepareStatement("SELECT id FROM amigo_user WHERE email = ?");
            stmt.setString(1, email);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return false;
            }

            // Register user
            stmt = connection.prepareStatement("INSERT INTO amigo_user " +
                    "(email,password,created_at) VALUES (?,?,UNIX_TIMESTAMP())");
            stmt.setString(1, email);
            stmt.setString(2, generateHash(password));
            return stmt.executeUpdate() == 1;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public int getUserId() {
        try {
            Db db = new Db();
            Connection connection = db.getDb();

            // Check if session is still valid
            if (!isValid()) {
                return 0;
            }

            // Check if email is already registered
            PreparedStatement stmt = connection.prepareStatement("SELECT user_id FROM amigo_session WHERE token = ?");
            stmt.setString(1, getId());
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return res.getInt("user_id");
            }
            return 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return 0;
        }
    }
}
