package com.project.springjavafx.javaFXApp.data.dao;

import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.javaFXApp.data.models.Employee;
import com.project.springjavafx.javaFXApp.data.models.LoginData;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        Connection connection = DatabaseConnector.connect();
        try {
            String query = "SELECT * FROM Employees";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee emp = new Employee(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("pswd"),
                        rs.getString("position"),
                        rs.getString("email"),
                        rs.getDate("birth_date"),
                        rs.getDate("hire_date"),
                        rs.getInt("hourly_wage")
                );
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public Employee getEmployeeById(int empID) {
        Connection connection = DatabaseConnector.connect();
        Employee employee = null;

        try {
            String query = "SELECT * FROM Employees WHERE id = " + empID;

            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("pswd"),
                        rs.getString("position"),
                        rs.getString("email"),
                        rs.getDate("birth_date"),
                        rs.getDate("hire_date"),
                        rs.getInt("hourly_wage")
                );

            }

            System.out.println("Employee found: " + employee.getId());


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't find employee");
        }

        return employee;
    }


    public String getEmployeePasswordById(int employeeId) {
        String query = "SELECT pswd FROM Employees WHERE id = ?";
        String password = null;

        Connection connection = DatabaseConnector.connect();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    password = resultSet.getString("pswd");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching password for employee ID " + employeeId + ": " + e.getMessage());
        }

        return password;
    }

    public ArrayList<LoginData> getLoginDataList() throws SQLException {
        Connection connection = DatabaseConnector.connect();
        ArrayList<LoginData> loginDataList = new ArrayList<>();
        String query = "SELECT id AS employeeID, first_name, last_name, pswd AS password FROM Employees";
        Statement statement = connection.createStatement();
        try (ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int employeeID = resultSet.getInt("employeeID");
                String username = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                String password = resultSet.getString("password");
                long loginID = employeeID; // Ha külön mező nincs, akkor azonos az employeeID-val
                // Hozzáadjuk a LoginData objektumot a listához
                loginDataList.add(new LoginData(loginID, username, password, employeeID));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching login data: " + e.getMessage());
        }

        return loginDataList;
    }


}
