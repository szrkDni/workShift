package com.project.springjavafx.javaFXApp.data.models;

import java.sql.Date;

public class WorkShift {
    private int id;
    private int employeeId;
    private Date workdayDate;
    private double workHour;
    private String shiftType;

    public int getId() {
        return id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getWorkdayDate() {
        return workdayDate;
    }

    public void setWorkdayDate(Date workdayDate) {
        this.workdayDate = workdayDate;
    }

    public double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(double workHour) {
        this.workHour = workHour;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

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
