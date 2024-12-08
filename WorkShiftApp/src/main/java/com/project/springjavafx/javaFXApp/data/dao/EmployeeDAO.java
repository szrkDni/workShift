package com.project.springjavafx.javaFXApp.data.dao;

import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.javaFXApp.data.models.Employee;
import com.project.springjavafx.javaFXApp.data.models.LoginData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//import static com.project.springjavafx.javaFXApp.data.db.DatabaseConnector.connection;

public class EmployeeDAO {

    // Method to get a database connection
    public Connection getConnection() {
        return DatabaseConnector.connect();  // Assuming DatabaseConnector is properly defined elsewhere
    }

    // Method to get all employees
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employees";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

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

    // Method to add an employee
    public boolean addEmployee(Employee employee) {
        String query = "INSERT INTO Employees (id, first_name, last_name, pswd, position, email, birth_date, hire_date, hourly_wage) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, employee.getId());
            stmt.setString(2, employee.getFirstName());
            stmt.setString(3, employee.getLastName());
            stmt.setString(4, employee.getPassword());
            stmt.setString(5, employee.getPosition());
            stmt.setString(6, employee.getEmail());
            stmt.setDate(7, employee.getBirthDate());
            stmt.setDate(8, employee.getHireDate());
            stmt.setInt(9, employee.getHourlyWage());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error while inserting employee: " + e.getMessage());
            e.printStackTrace(); // This will help you debug the issue
            return false;
        }
    }


    // Method to update an employee
    public boolean updateEmployee(Employee employee) {
        String query = "UPDATE Employees SET first_name = ?, last_name = ?, pswd = ?, position = ?, email = ?, " +
                "birth_date = ?, hire_date = ?, hourly_wage = ? WHERE id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getPassword());
            stmt.setString(4, employee.getPosition());
            stmt.setString(5, employee.getEmail());
            stmt.setDate(6, employee.getBirthDate());
            stmt.setDate(7, employee.getHireDate());
            stmt.setInt(8, employee.getHourlyWage());
            stmt.setInt(9, employee.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Return true if the update was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    // Method to delete an employee by ID
    public boolean deleteEmployee(int employeeId) {
        String query = "DELETE FROM Employees WHERE id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, employeeId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
