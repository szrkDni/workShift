package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.ProjectDAO;
import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.javaFXApp.data.models.Project;
import com.project.springjavafx.javaFXApp.data.models.ProjectWorker;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.HashMap;
import java.util.Map;
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
    private AnchorPane projectWorkersAncor;

    @FXML
    private Button projectSearchButton;


    //innentol lefele a project-worker adatai és kapcsoloi
    @FXML
    private TreeTableView<ProjectWorker> projectTreeTableView;
    @FXML
    private TreeTableColumn<ProjectWorker, Integer> projectIdColumn;
    @FXML
    private TreeTableColumn<ProjectWorker, String> projectNameColumn2;
    @FXML
    private TreeTableColumn<ProjectWorker, Integer> workerIdColumn;
    @FXML
    private TreeTableColumn<ProjectWorker, String> workerNameColumn;

    @FXML
    private ComboBox<Integer> projectIdComboBox;
    @FXML
    private ComboBox<String> workerNameComboBox;

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
        initializeTreeTable();
        loadComboBoxData();

        projectsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onRowSelected());
        super.initialize(url, resourceBundle);
        if (!employee.isManager()){
            projectControls.setVisible(false);
            projectWorkersAncor.setVisible(false);
        }
    }

    private void initializeTreeTable() {

        projectIdColumn.setCellValueFactory(cellData -> {
            Integer projectId = cellData.getValue().getValue().getProjectId();
            return projectId != null ? new SimpleIntegerProperty(projectId).asObject() : null;
        });
        projectIdColumn.setCellFactory(column -> new TreeTableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(item));
                }
            }
        });


        projectNameColumn2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getProjectWorkerName()));
        workerIdColumn.setCellValueFactory(cellData -> {
            Integer workerId = cellData.getValue().getValue().getWorkerId();
            return workerId != null ? new SimpleIntegerProperty(workerId).asObject() : null;
        });
        workerIdColumn.setCellFactory(column -> new TreeTableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(item));
                }
            }
        });

        workerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getWorkerName()));

        loadTreeTableData();
    }

    private void loadTreeTableData() {

        TreeItem<ProjectWorker> root = new TreeItem<>(new ProjectWorker(null, "Projects", null, null));
        root.setExpanded(true);

        Map<Integer, TreeItem<ProjectWorker>> projectMap = projektDAO.getProjectWorkers();
        root.getChildren().addAll(projectMap.values());

        projectTreeTableView.setRoot(root);
        projectTreeTableView.setShowRoot(false);
    }

    private void loadComboBoxData() {
        try (Connection connection = DatabaseConnector.connect();
             Statement statement = connection.createStatement()) {
                projectIdComboBox.getItems().clear();
                workerNameComboBox.getItems().clear();
            // Project IDs
            ResultSet projectRs = statement.executeQuery("SELECT id FROM Projects");
            while (projectRs.next()) {
                projectIdComboBox.getItems().add(projectRs.getInt("id"));
            }

            // Worker Names
            ResultSet workerRs = statement.executeQuery("SELECT CONCAT(e.first_name, \" \", e.last_name) AS workerName FROM Employees e");
            while (workerRs.next()) {
                workerNameComboBox.getItems().add(workerRs.getString("workerName"));
            }

        } catch (SQLException e) {
            System.out.println("Error loading ComboBox data: " + e.getMessage());
        }
    }
    @FXML
    private void addWorkerToProject() {
        Integer selectedProjectId = projectIdComboBox.getValue();
        String selectedWorkerName = workerNameComboBox.getValue();

        if (selectedProjectId == null || selectedWorkerName == null) {
            System.out.println("Please select both Project ID and Worker Name.");
            return;
        }

        String query = "INSERT INTO Project_Workers (project_id, employee_id) VALUES (?, (SELECT id FROM Employees WHERE CONCAT(Employees.first_name, \" \", Employees.last_name) = ?))";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, selectedProjectId);
            statement.setString(2, selectedWorkerName);
            statement.executeUpdate();

            System.out.println("Worker added to project successfully.");
            loadTreeTableData();
            loadComboBoxData();

        } catch (SQLException e) {
            System.out.println("Error adding worker to project: " + e.getMessage());
        }
    }

    @FXML
    private void deleteWorkerFromProject() {
        TreeItem<ProjectWorker> selectedItem = projectTreeTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue().getWorkerId() == null) {
            System.out.println("Please select a worker to delete.");
            return;
        }

        Integer selectedWorkerId = selectedItem.getValue().getWorkerId();
        Integer selectedProjectId = selectedItem.getParent().getValue().getProjectId();

        String query = "DELETE FROM Project_Workers WHERE project_id = ? AND employee_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, selectedProjectId);
            statement.setInt(2, selectedWorkerId);
            statement.executeUpdate();

            System.out.println("Worker removed from project successfully.");
            loadTreeTableData();
            loadComboBoxData();

        } catch (SQLException e) {
            System.out.println("Error deleting worker from project: " + e.getMessage());
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
        ObservableList<Project> projects = projektDAO.getProjects(searchQuery);
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
        String query = "DELETE FROM Project_Workers WHERE project_id = ?;" +
                "DELETE FROM Projects WHERE id = ?;";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, Integer.parseInt(project_id.getText()) );
            statement.setInt(2, Integer.parseInt(project_id.getText()) );

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Project deleted successfully.");
            } else {
                System.out.println("No project found with the specified ID.");
            }
            loadProjects(null);
            loadTreeTableData();
            loadComboBoxData();
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
            loadTreeTableData();
            loadComboBoxData();
        } catch (SQLException e) {
            System.out.println("Error while updating project: " + e.getMessage());
        }
    }

    public void addProject() {
        boolean success = projektDAO.addProject(
                project_name.getText(),
                Integer.parseInt(project_manager.getText()),
                project_description.getText(),
                project_start_date.getValue() != null ? java.sql.Date.valueOf(project_start_date.getValue()) : null,
                project_end_date.getValue() != null ? java.sql.Date.valueOf(project_end_date.getValue()) : null
        );

        if (success) {
            System.out.println("Project added successfully.");
            loadProjects(null);
            loadTreeTableData();
            loadComboBoxData();
        } else {
            System.out.println("Error adding project.");
        }
    }

    private final ProjectDAO projektDAO = new ProjectDAO();
}