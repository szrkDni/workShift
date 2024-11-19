package com.project.springjavafx.javaFXApp.data.models;

import java.sql.Date;

public class LeaveRequest {
    private int leaveId;
    private int employeeId;
    private String leaveType;
    private Date startDate;
    private Date endDate;
    private String status;
    private int approvedBy;

    // Constructor
    public LeaveRequest(int leaveId, int employeeId, String leaveType, Date startDate, Date endDate, String status, int approvedBy) {
        this.leaveId = leaveId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.approvedBy = approvedBy;
    }

    // Getters and Setters
    // ... Add getter and setter methods here ...
}

