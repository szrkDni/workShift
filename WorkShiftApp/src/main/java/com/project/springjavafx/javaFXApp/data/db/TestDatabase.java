package com.project.springjavafx.javaFXApp.data.db;

import java.sql.*;

public class TestDatabase {
    public static void main(String[] args) {
        String url = "jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7744747?useUnicode=true&characterEncoding=utf8";
        String user = "sql7744747";
        String password = "w1CPC7I5ur";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT first_name, last_name FROM Employees WHERE id = 6";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
