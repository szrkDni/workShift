package com.project.springjavafx.javaFXApp.data.dao;

import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.javaFXApp.data.models.WorkShift;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkShiftDAO {
    public List<WorkShift> getAllWorkShifts() {
        List<WorkShift> workShifts = new ArrayList<>();
        Connection connection = DatabaseConnector.connect();
        try {
            String query = "SELECT * FROM Work_shifts";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                WorkShift shift = new WorkShift(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getDate("workday_date"),
                        rs.getDouble("work_hour"),
                        rs.getString("shift_type")
                );
                workShifts.add(shift);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workShifts;
    }


    public boolean addShift(WorkShift workShift) {

        int row = 0;

        try {
            Connection connection = DatabaseConnector.connect();

            String query = "INSERT INTO Work_shifts (id, employee_id, workday_date, work_hour, shift_type)"  +
                    " VALUES ('" + workShift.getId() + "'," +
                    "'" + workShift.getEmployeeId() + "'," +
                    "'" + workShift.getWorkdayDate() + "'," +
                    "'" + workShift.getWorkHour() + "'," +
                    "'" + workShift.getShiftType() + "'"
                    +")";


            PreparedStatement stmt = connection.prepareStatement(query);


            row = stmt.executeUpdate();

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }


        return row > 0;
    }
}

