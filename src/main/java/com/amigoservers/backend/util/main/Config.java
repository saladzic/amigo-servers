package com.amigoservers.backend.util.main;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class Config {
    private final String mode;
    private final int sessionExpiration;

    private final String prodScheme;
    private final String prodHost;
    private final String prodUsername;
    private final String prodPassword;
    private final String prodDbname;

    private final String devScheme;
    private final String devHost;
    private final String devUsername;
    private final String devPassword;
    private final String devDbname;

    public Config() {
        try {
            InputStream input = new FileInputStream(new File("config.yml"));
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);
            Map<String, Object> connections = (Map<String, Object>) data.get("connections");
            Map<String, Object> database = (Map<String, Object>) connections.get("database");
            Map<String, String> prod = (Map<String, String>) database.get("prod");
            Map<String, String> dev = (Map<String, String>) database.get("dev");
            Map<String, String> test = (Map<String, String>) database.get("test");

            mode = data.get("mode").toString();
            sessionExpiration = (int) data.get("sessionExpiration");

            prodScheme = prod.get("scheme");
            prodHost = prod.get("host");
            prodUsername = prod.get("username");
            prodPassword = prod.get("password");
            prodDbname = prod.get("dbname");

            devScheme = dev.get("scheme");
            devHost = dev.get("host");
            devUsername = dev.get("username");
            devPassword = dev.get("password");
            devDbname = dev.get("dbname");
        } catch (FileNotFoundException e) {
            System.out.println("Config not found!");
            throw new RuntimeException("Config not found!");
        }
    }

    public String getMode() {
        return mode;
    }

    public int getSessionExpiration() {
        return sessionExpiration;
    }

    public String getProdScheme() {
        return prodScheme;
    }

    public String getProdHost() {
        return prodHost;
    }

    public String getProdUsername() {
        return prodUsername;
    }

    public String getProdPassword() {
        return prodPassword;
    }

    public String getProdDbname() {
        return prodDbname;
    }

    public String getDevScheme() {
        return devScheme;
    }

    public String getDevHost() {
        return devHost;
    }

    public String getDevUsername() {
        return devUsername;
    }

    public String getDevPassword() {
        return devPassword;
    }

    public String getDevDbname() {
        return devDbname;
    }
}
