package com.project.springjavafx.javaFXApp.data.dao;

import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.javaFXApp.data.models.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }


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

    public void fetchAllEmployees() {
        String query = "SELECT id, first_name, last_name, position, email FROM Employees";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("ID | First Name | Last Name | Position | Email");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String position = resultSet.getString("position");
                String email = resultSet.getString("email");

                System.out.printf("%d | %s | %s | %s | %s%n", id, firstName, lastName, position, email);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching employees: " + e.getMessage());
        }
    }


    public void addEmployee(String firstName, String lastName, String position, String email, String birthDate, String hireDate, int hourlyWage) {
        String query = "INSERT INTO Employees (first_name, last_name, position, email, birth_date, hire_date, hourly_wage) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, position);
            statement.setString(4, email);
            statement.setString(5, birthDate); // Pl. "2000-01-01"
            statement.setString(6, hireDate); // Pl. "2023-11-18"
            statement.setInt(7, hourlyWage);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Employee successfully added!");
            }

        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }



    public void updateEmployeeHourlyWage(int employeeId, int newHourlyWage) {
        String query = "UPDATE Employees SET hourly_wage = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newHourlyWage);
            statement.setInt(2, employeeId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee hourly wage updated successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }


    public void deleteEmployee(int employeeId) {
        String query = "DELETE FROM Employees WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }


}
