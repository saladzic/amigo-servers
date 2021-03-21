package com.amigoservers.backend.util.driver;

import com.amigoservers.backend.util.main.Config;

import java.sql.Connection;
import java.sql.SQLException;

public class Db {
    private Connection connection;
    private boolean noDb = false;

    public Db() {
        loadDb();
    }

    public Db(boolean noDb) {
        this.noDb = noDb;
        loadDb();
    }

    public Connection connect(String scheme) throws SQLException {
        String host;
        String dbname;
        String username;
        String password;

        Config config = new Config();
        if (config.getMode().equals("dev")) {
            host = config.getDevHost();
            dbname = config.getDevDbname();
            username = config.getDevUsername();
            password = config.getDevPassword();
        } else {
            host = config.getProdHost();
            dbname = config.getProdDbname();
            username = config.getProdUsername();
            password = config.getProdPassword();
        }

        if (scheme.equals("mysql")) {
            try {
                MySQL mysql = new MySQL();
                if (!noDb) {
                    return mysql.connect(host, dbname, username, password);
                } else {
                    // Connect without Database
                    return mysql.connect(host, username, password);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } else if (scheme.equals("postgresql")) {
            // ToDo: PostgreSQL driver implementation
        }
        return null;
    }

    public Connection getDb() {
        return connection;
    }

    private String chooseDbScheme() {
        Config config = new Config();
        if (config.getMode().equals("prod")) {
            return config.getProdScheme();
        } else {
            return config.getDevScheme();
        }
    }

    public void loadDb() {
        try {
            String scheme = chooseDbScheme();
            connection = connect(scheme);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Could not connect to a database");
        }
    }
}
