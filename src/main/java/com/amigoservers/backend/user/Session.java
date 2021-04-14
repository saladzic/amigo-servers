package com.amigoservers.backend.user;

import com.amigoservers.backend.util.driver.Db;
import com.amigoservers.backend.util.exception.LoginFailedException;
import com.amigoservers.backend.util.exception.ServerException;
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
    public static int login(String username, String password) throws LoginFailedException, ServerException {
        try {
            Db db = new Db();
            PreparedStatement preparedStatement = db.getDb()
                    .prepareStatement("SELECT id FROM amigo_user WHERE username=? AND password=?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, Session.generateHash(password));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new LoginFailedException();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new ServerException(exception.getMessage());
        }
    }

    public static String create(int userId, String userAgent, String ip) {
        String token = RandomString.random(15);
        try {
            Db db = new Db();
            Connection connection = db.getDb();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO amigo_session " +
                    "(token,user_id,user_agent,ip_address,created_at) VALUES (?,?,?,?,UNIX_TIMESTAMP())");
            stmt.setString(1, token);
            stmt.setInt(2, userId);
            stmt.setString(3, userAgent);
            stmt.setString(4, ip);
            stmt.executeUpdate();
            return token;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static String generateHash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hash = md.digest(str.getBytes(StandardCharsets.UTF_8));
            return Session.convertToHex(hash);
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
    public static String convertToHex(byte[] raw) {
        StringBuilder sb = new StringBuilder();
        for (byte b : raw) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
