package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.javaFXApp.data.models.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

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
    private TableView<Project> projectsTable;
    @FXML
    private TableColumn<Project, Integer> idColumn;
    @FXML
    private TableColumn<Project, String> projectNameColumn;
    @FXML
    private TableColumn<Project, Integer> projectManagerColumn;
    @FXML
    private TableColumn<Project, String> descriptionColumn;
    @FXML
    private TableColumn<Project, LocalDate> startDateColumn;
    @FXML
    private TableColumn<Project, LocalDate> endDateColumn;

    @FXML
    private TextField projectSearchField;

    @FXML
    private AnchorPane projectControls;

    @FXML
    private Button projectSearchButton;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){

        System.out.println("Initialized!");
        // Táblázat oszlopainak összekapcsolása a Project osztály mezőivel
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        projectManagerColumn.setCellValueFactory(new PropertyValueFactory<>("projectManager"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        // Betöltjük az összes projektet a táblázatba
        loadProjects(null);

        projectsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onRowSelected());
        super.initialize(url, resourceBundle);
        if (!employee.isManager()){
            projectControls.setVisible(false);
        }
    }

    @FXML
    private void onRowSelected() {
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {
            project_id.setText(String.valueOf(selectedProject.getId()));
            project_name.setText(selectedProject.getProjectName());
            project_manager.setText(String.valueOf(selectedProject.getProjectManager()));
            project_description.setText(selectedProject.getDescription());
            project_start_date.setValue(selectedProject.getStartDate() != null ? selectedProject.getStartDate().toLocalDate() : null);
            project_end_date.setValue(selectedProject.getEndDate() != null ? selectedProject.getEndDate().toLocalDate() : null);
        }
    }

    public ObservableList<Project> getProjects(String searchQuery){

        ObservableList<Project> projectsList = FXCollections.observableArrayList();
        String query = "SELECT id, project_name, project_manager, description, start_date, end_date FROM Projects";

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            query += " WHERE project_name LIKE ?";
        }


        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                statement.setString(1, "%" + searchQuery + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String projectName = resultSet.getString("project_name");
                int projectManager = resultSet.getInt("project_manager");
                String description = resultSet.getString("description");
                Date startDate = resultSet.getDate("start_date") != null ? Date.valueOf(resultSet.getDate("start_date").toLocalDate()) : null;
                Date endDate = resultSet.getDate("end_date") != null ? Date.valueOf(resultSet.getDate("end_date").toLocalDate()) : null;

                // Hozzáadjuk a Project objektumot a listához
                projectsList.add(new Project(id, projectName, projectManager, description, startDate, endDate));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching projects: " + e.getMessage());
        }

        return projectsList;
    }

    private void loadProjects(String searchQuery) {
        ObservableList<Project> projects = getProjects(searchQuery);
        projectsTable.setItems(projects);
    }

    @FXML
    private void onSearchButtonClicked() {
        System.out.println("Search button pressed");
        String searchQuery = projectSearchField.getText();
        loadProjects(searchQuery);
    }

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
            loadProjects(null);
        } catch (SQLException e) {
            System.out.println("Error while deleting project: " + e.getMessage());
        }
    }

    public void updateProject() {

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
            loadProjects(null);
        } catch (SQLException e) {
            System.out.println("Error while updating project: " + e.getMessage());
        }
    }

    public void addProject() {

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
            loadProjects(null);
        } catch (SQLException e) {
            System.out.println("Error while adding project: " + e.getMessage());
        }
    }
}