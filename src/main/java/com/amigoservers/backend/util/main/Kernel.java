package com.amigoservers.backend.util.main;

import com.amigoservers.backend.util.driver.Db;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.sql.Statement;

public class Kernel {
    public static void main(String[] args) {
        // Processing rest api request
    }

    public void resetDb() {
        try {
            // Get DB-name
            Config config = new Config();
            String dbname = config.getDevDbname();

            // Execute Update
            Db db = new Db(true);
            Statement stmt = db.getDb().createStatement();
            stmt.executeUpdate("DROP DATABASE IF EXISTS " + dbname);
            stmt.executeUpdate("CREATE DATABASE " + dbname);
            stmt.executeUpdate("USE " + dbname);
            ScriptRunner sr = new ScriptRunner(db.getDb());
            sr.setLogWriter(null);
            sr.setAutoCommit(true);
            sr.setStopOnError(false);
            //Creating a reader object
            Reader reader = new BufferedReader(new FileReader("database/db.sql"));
            Reader reader2 = new BufferedReader(new FileReader("database/sample_data.sql"));
            //Running the script
            sr.runScript(reader);
            sr.runScript(reader2);
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }
}
