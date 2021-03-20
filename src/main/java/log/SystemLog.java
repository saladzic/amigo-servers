package log;

import util.driver.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SystemLog extends AbstractLog {
    public List<SystemLog> getLogs() {
        try {
            PreparedStatement stmt = getDb().prepareStatement("SELECT id,type,heading_block,message_block,active" +
                    " FROM amigo_log_system ORDER BY created_at DESC");
            ResultSet rs = stmt.executeQuery();
            List<SystemLog> list = new ArrayList<>();
            while (rs.next()) {
                SystemLog log = new SystemLog();
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

    public void success(String heading, String message) {
        create("success", heading, message);
    }

    public void warn(String heading, String message) {
        create("warn", heading, message);
    }

    public void error(String heading, String message) {
        create("error", heading, message);
    }

    private void create(String type, String heading, String message) {
        try {
            Db db = new Db();
            Connection connection = db.getDb();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO amigo_log_system " +
                    "(type,heading_block,message_block,created_at) VALUES (?,?,?,UNIX_TIMESTAMP())");
            stmt.setString(1, type);
            stmt.setString(2, heading);
            stmt.setString(3, message);
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
