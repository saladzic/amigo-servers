package com.amigoservers.backend.user;

import com.amigoservers.backend.util.driver.Db;
import com.amigoservers.backend.util.mvc.Model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Model {
    private int id;
    private String email;
    private BigDecimal balance;

    public User get() {
        try {
            Db db = new Db();
            PreparedStatement preparedStatement = db.getDb()
                    .prepareStatement("SELECT email,balance FROM amigo_user WHERE id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                email = resultSet.getString("email");
                balance = resultSet.getBigDecimal("balance");
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
                    .prepareStatement("UPDATE amigo_user SET email=?,balance=? WHERE id=?");
            stmt.setString(1, email);
            stmt.setBigDecimal(2, balance);
            stmt.setInt(3, id);
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
