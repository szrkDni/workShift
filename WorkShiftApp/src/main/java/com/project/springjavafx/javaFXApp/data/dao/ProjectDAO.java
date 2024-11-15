package com.project.springjavafx.javaFXApp.data.dao;

import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.javaFXApp.data.models.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        Connection connection = DatabaseConnector.connect();
        try {
            String query = "SELECT * FROM Projects";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Project proj = new Project(
                        rs.getInt("id"),
                        rs.getString("project_name"),
                        rs.getInt("project_manager"),
                        rs.getString("description"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date")
                );
                projects.add(proj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
}

