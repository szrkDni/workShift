package com.project.springjavafx.javaFXApp.data.dao;

import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.javaFXApp.data.models.Employee;

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
                        rs.getString("position"),
                        rs.getString("email"),
                        rs.getDate("birth_date"),
                        rs.getDate("hire_date"),
                        rs.getInt("hourly_wage")
                );

                System.out.println(employee);
            }

            System.out.println("Employee found: " + employee.getId());


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't find employee");
        }

        return employee;
    }
}
