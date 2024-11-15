package com.project.springjavafx.javaFXApp.data.models;

import java.sql.Date;

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

    // Getters and Setters
    // ... Add getter and setter methods here ...
}
