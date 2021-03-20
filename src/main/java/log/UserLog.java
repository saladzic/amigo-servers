package log;

import user.User;
import util.driver.Db;
import util.mvc.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserLog extends AbstractLog {
    public List<UserLog> getLogs(User user) {
        try {
            PreparedStatement stmt = getDb().prepareStatement("SELECT id,type,heading_block,message_block,active" +
                    " FROM amigo_log_user WHERE user_id=? ORDER BY created_at DESC");
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            List<UserLog> list = new ArrayList<>();
            while (rs.next()) {
                UserLog log = new UserLog();
                log.setId(rs.getInt("id"));
                log.setType(rs.getString("type"));
                log.setHeading(rs.getString("heading_block"));
                log.setMessage(rs.getString("message_block"));
                log.setActive(rs.getBoolean("active"));
                list.add(log);
            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void success(User user, String heading, String message) {
        create("success", user, heading, message);
    }

    public void warn(User user, String heading, String message) {
        create("warn", user, heading, message);
    }

    public void error(User user, String heading, String message) {
        create("error", user, heading, message);
    }

    private void create(String type, User user, String heading, String message) {
        try {
            Db db = new Db();
            Connection connection = db.getDb();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO amigo_log_user " +
                    "(user_id,type,heading_block,message_block,created_at) VALUES (?,?,?,?,UNIX_TIMESTAMP())");
            stmt.setInt(1, user.getId());
            stmt.setString(2, type);
            stmt.setString(3, heading);
            stmt.setString(4, message);
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
