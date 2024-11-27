package com.project.springjavafx.javaFXApp.data.dao;

import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.javaFXApp.data.models.LeaveRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestDAO {
    public List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        Connection connection = DatabaseConnector.connect();
        try {
            String query = "SELECT * FROM Leave_Requests";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LeaveRequest leaveReq = new LeaveRequest(
                        rs.getInt("leave_id"),
                        rs.getInt("employee_id"),
                        rs.getString("leave_type"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status"),
                        rs.getInt("approved_by")
                );
                leaveRequests.add(leaveReq);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaveRequests;
    }

    public List<LeaveRequest> getLeaveRequestsbyEmployeeId(int employeeId) {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        Connection connection = DatabaseConnector.connect();
        try {
            String query = "SELECT * FROM Leave_Requests WHERE employee_id = " + employeeId;
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LeaveRequest leaveReq = new LeaveRequest(
                        rs.getInt("leave_id"),
                        rs.getInt("employee_id"),
                        rs.getString("leave_type"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status"),
                        rs.getInt("approved_by")
                );
                leaveRequests.add(leaveReq);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return leaveRequests;
    }

    public boolean addLeaveRequest(LeaveRequest leaveReq) {
        Connection connection = DatabaseConnector.connect();

        int row = 0;

        try{

            String query = "INSERT INTO Leave_Requests (leave_id, employee_id, leave_type, start_date, end_date, status, approved_by) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, leaveReq.getLeaveId());
            stmt.setInt(2, leaveReq.getEmployeeId());
            stmt.setString(3, leaveReq.getLeaveType());
            stmt.setDate(4, leaveReq.getStartDate());
            stmt.setDate(5, leaveReq.getEndDate());
            stmt.setString(6, leaveReq.getStatus());
            stmt.setInt(7, leaveReq.getApprovedBy());



            row = stmt.executeUpdate();


        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }


        return row > 0;
    }
}
