package com.project.springjavafx.javaFXApp.data.models;

import java.sql.Date;

public class WorkShift {
    private int id;
    private int employeeId;
    private Date workdayDate;
    private double workHour;
    private String shiftType;

    // Constructor
    public WorkShift(int id, int employeeId, Date workdayDate, double workHour, String shiftType) {
        this.id = id;
        this.employeeId = employeeId;
        this.workdayDate = workdayDate;
        this.workHour = workHour;
        this.shiftType = shiftType;
    }

    // Getters and Setters
    // ... Add getter and setter methods here ...
}
