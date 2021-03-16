package util.driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    public Connection connect(String host, String db, String username, String password) throws SQLException {
        return DriverManager
                .getConnection("jdbc:mysql://" + host + "/" + db + "?"
                        + "user=" + username + "&password=" + password);
    }

    public Connection connect(String host, String username, String password) throws SQLException {
        return DriverManager
                .getConnection("jdbc:mysql://" + host + "/?"
                        + "user=" + username + "&password=" + password);
    }
}
