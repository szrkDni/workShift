package com.project.springjavafx.javaFXApp.data.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/*
public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7744747";
    private static final String USERNAME = "your_username"; // Cseréld ki a valódi felhasználónévre
    private static final String PASSWORD = "your_password"; // Cseréld ki a valódi jelszóra

    public static Connection connect() {
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 8.x JDBC driver használata
            // Open a connection
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
*/



public class DatabaseConnector {

    public static Connection connect() {
        try {
            Properties props = new Properties();

            // Load properties file
            try (InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    System.out.println("Sorry, unable to find config.properties");
                    return null;
                }
                props.load(input);
            }

            // Read connection details from properties
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            // Create and return a new connection each time
            return DriverManager.getConnection(url, user, password);

        } catch (SQLException | IOException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}

