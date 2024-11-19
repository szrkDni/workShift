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
}
