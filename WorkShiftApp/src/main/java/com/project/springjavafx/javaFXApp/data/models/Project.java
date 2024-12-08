package com.project.springjavafx.javaFXApp.data.models;

import java.sql.Date;
import java.time.LocalDate;

public class Project {
    private int id;
    private String projectName;
    private int projectManager;
    private String description;
    private Date startDate;
    private Date endDate;

    // Constructor
    public Project(int id, String projectName, int projectManager, String description, Date startDate, Date endDate) {
        this.id = id;
        this.projectName = projectName;
        this.projectManager = projectManager;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public int getProjectManager() { return projectManager; }

    public void setProjectManager(String projectManager) { this.projectManager = Integer.parseInt(projectManager); }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = Date.valueOf(startDate); }

    public Date getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = Date.valueOf(endDate); }
}
