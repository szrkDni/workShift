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
                        rs.getString("status")
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
                        rs.getString("status")
                );
                leaveRequests.add(leaveReq);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return leaveRequests;
    }

    public boolean addLeaveRequest(LeaveRequest leaveReq) {

        int row = 0;

        try{
            Connection connection = DatabaseConnector.connect();

            String query = "INSERT INTO Leave_Requests (leave_id, employee_id, leave_type, start_date, end_date, status)"  +
                    " VALUES ('" + leaveReq.getLeaveId() + "'," +
                     "'" + leaveReq.getEmployeeId() + "'," +
                    "'" + leaveReq.getLeaveType() + "'," +
                    "'" + leaveReq.getStartDate() + "'," +
                    "'" + leaveReq.getEndDate() + "'," +
                    "'" + leaveReq.getStatus() + "'"
                    +")";
            PreparedStatement stmt = connection.prepareStatement(query);

            System.out.println(query);

            row = stmt.executeUpdate();



        }catch (SQLException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }


        return row > 0;
    }
}
