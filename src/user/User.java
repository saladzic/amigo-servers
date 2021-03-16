package user;

import system.driver.Db;
import system.mvc.Model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Model {
    private int id;
    private String username;
    private BigDecimal balance;
    private String email;

    public User get() {
        try {
            Db db = new Db();
            PreparedStatement preparedStatement = db.getDb()
                    .prepareStatement("SELECT username,balance,email FROM amigo_user WHERE id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                username = resultSet.getString("username");
                balance = resultSet.getBigDecimal("balance");
                email = resultSet.getString("email");
                return this;
            } else {
                return null;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public User set() {
        try {
            Db db = new Db();
            PreparedStatement stmt = db.getDb()
                    .prepareStatement("UPDATE amigo_user SET username=?,balance=?,email=? WHERE id=?");
            stmt.setString(1, username);
            stmt.setBigDecimal(2, balance);
            stmt.setString(3, email);
            stmt.setInt(4, id);
            System.out.println(stmt);
            stmt.executeUpdate();
            return this;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public User setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }
}
