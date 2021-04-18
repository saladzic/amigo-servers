package com.amigoservers.backend.util.mvc;

import com.amigoservers.backend.util.driver.Db;
import com.amigoservers.backend.util.main.Kernel;

import java.sql.Connection;

public abstract class Model extends Kernel {
    private Connection connection;

    public Model() {
        Db db = new Db();
        connection = db.getDb();
    }

    public Connection getDb() {
        return connection;
    }
}
