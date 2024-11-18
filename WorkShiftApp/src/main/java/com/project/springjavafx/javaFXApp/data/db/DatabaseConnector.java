package com.project.springjavafx.javaFXApp.data.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    private static Connection connection;

    public static Connection connect() {
        if (connection == null) {
            try {
                Properties props = new Properties();

                // Használjuk a getResourceAsStream metódust a classpath eléréséhez
                try (InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream("config.properties")) {
                    if (input == null) {
                        System.out.println("Sorry, unable to find config.properties");
                        return null;
                    }

                    // Betöltjük a properties fájlt
                    props.load(input);
                }

                // Kapcsolódási adatok beolvasása a properties fájlból
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                // Létrehozzuk a kapcsolatot
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to the database.");
            } catch (SQLException | IOException e) {
                System.out.println("Database connection failed: " + e.getMessage());
            }
        }
        return connection;
    }
}
