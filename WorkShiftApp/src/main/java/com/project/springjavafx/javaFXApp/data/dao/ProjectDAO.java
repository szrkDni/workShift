package com.project.springjavafx.javaFXApp.data.dao;

import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.javaFXApp.data.models.Project;

import com.project.springjavafx.javaFXApp.data.models.ProjectWorker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ProjectDAO {

    // Kapcsolódás az adatbázishoz
    private Connection connect() throws SQLException {
        return DatabaseConnector.connect();
    }

    // Projekt hozzáadása
    public boolean addProject(String projectName, int projectManager, String description, Date startDate, Date endDate) {
        String query = "INSERT INTO Projects (project_name, project_manager, description, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, projectName);
            statement.setInt(2, projectManager);
            statement.setString(3, description);
            statement.setDate(4, startDate);
            statement.setDate(5, endDate);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding project: " + e.getMessage());
            return false;
        }
    }

    // Projekt frissítése
    public boolean updateProject(int id, String projectName, int projectManager, String description, Date startDate, Date endDate) {
        String query = "UPDATE Projects SET project_name = ?, project_manager = ?, description = ?, start_date = ?, end_date = ? WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, projectName);
            statement.setInt(2, projectManager);
            statement.setString(3, description);
            statement.setDate(4, startDate);
            statement.setDate(5, endDate);
            statement.setInt(6, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating project: " + e.getMessage());
            return false;
        }
    }

    // Projekt törlése
    public boolean deleteProject(int id) {
        String query = "DELETE FROM Projects WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting project: " + e.getMessage());
            return false;
        }
    }

    // Projektek lekérése
    public ObservableList<Project> getProjects(String searchQuery) {
        ObservableList<Project> projectsList = FXCollections.observableArrayList();
        String query = "SELECT id, project_name, project_manager, description, start_date, end_date FROM Projects";

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            query += " WHERE project_name LIKE ?";
        }

        try (Connection connection = connect();
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
                Date startDate = resultSet.getDate("start_date");
                Date endDate = resultSet.getDate("end_date");

                projectsList.add(new Project(id, projectName, projectManager, description, startDate, endDate));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching projects: " + e.getMessage());
        }

        return projectsList;
    }

    // Projektek és dolgozók lekérése TreeTable-höz
    public Map<Integer, TreeItem<ProjectWorker>> getProjectWorkers() {
        Map<Integer, TreeItem<ProjectWorker>> projectMap = new HashMap<>();

        String query = "SELECT p.id AS projectId, p.project_name, e.id AS worker_Id, " +
                "CONCAT(e.first_name, ' ', e.last_name) AS workerName " +
                "FROM Projects p " +
                "LEFT JOIN Project_Workers pw ON p.id = pw.project_id " +
                "LEFT JOIN Employees e ON pw.employee_id = e.id";

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int projectId = rs.getInt("projectId");
                String projectName = rs.getString("project_name");
                Integer workerId = rs.getInt("worker_Id");
                String workerName = rs.getString("workerName");

                TreeItem<ProjectWorker> projectItem = projectMap.get(projectId);
                if (projectItem == null) {
                    projectItem = new TreeItem<>(new ProjectWorker(projectId, projectName, null, null));
                    projectMap.put(projectId, projectItem);
                }

                if (workerId != null) {
                    projectItem.getChildren().add(new TreeItem<>(new ProjectWorker(null, null, workerId, workerName)));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching project workers: " + e.getMessage());
        }

        return projectMap;
    }
}


