package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.EmployeeDAO;
import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectController extends MainController  {

    @FXML
    public TextField project_id;
    @FXML
    public TextField project_name;
    @FXML
    public TextField project_manager;
    @FXML
    public TextField project_description;
    @FXML
    public DatePicker project_start_date;
    @FXML
    public DatePicker project_end_date;


    @FXML
    public void clearProjectFields(MouseEvent mouseEvent) {
        project_id.clear();
        project_name.clear();
        project_manager.clear();
        project_description.clear();
        project_start_date.setValue(null);
        project_end_date.setValue(null);
    }

    public void deleteProject() {
        DatabaseConnector.connect();
        String query = "DELETE FROM Projects WHERE id = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, Integer.parseInt(project_id.getText()) );

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Project deleted successfully.");
            } else {
                System.out.println("No project found with the specified ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting project: " + e.getMessage());
        }
    }


    public void updateProject() {
        DatabaseConnector.connect();
        String query = "UPDATE Projects SET project_name = ?, project_manager = ?,  description = ?, start_date = ?, end_date = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, project_name.getText());
            statement.setInt(2, Integer.parseInt(project_manager.getText()));
            statement.setString(3, project_description.getText());
            statement.setDate(4, project_start_date != null ? java.sql.Date.valueOf(project_start_date.getValue()) : null);
            statement.setDate(5, project_end_date != null ? java.sql.Date.valueOf(project_end_date.getValue()) : null);
            statement.setInt(6, Integer.parseInt(project_id.getText()) );

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Project updated successfully.");
            } else {
                System.out.println("No project found with the specified ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error while updating project: " + e.getMessage());
        }
    }



    public void addProject() {
        DatabaseConnector.connect();
        String query = "INSERT INTO Projects (project_name, project_manager,  description, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, project_name.getText());
            statement.setInt(2, Integer.parseInt(project_manager.getText()));
            statement.setString(3, project_description.getText());
            statement.setDate(4, project_start_date != null ? java.sql.Date.valueOf(project_start_date.getValue()) : null);
            statement.setDate(5, project_end_date != null ? java.sql.Date.valueOf(project_end_date.getValue()) : null);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Project added successfully.");
            } else {
                System.out.println("Error adding project.");
            }
        } catch (SQLException e) {
            System.out.println("Error while adding project: " + e.getMessage());
        }
    }







}