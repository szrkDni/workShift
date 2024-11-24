package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.LeaveRequestDAO;
import com.project.springjavafx.javaFXApp.data.models.LeaveRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TimeoffController extends MainController implements Initializable {

    @FXML
    public Button timeoffbutton;

    @FXML
    public Label remainingHolidays;

    @FXML
    public Label remainingSickLeaves;

    @FXML
    public Label usedHolidays;

    @FXML
    public Label usedSickLeaves;

    @FXML
    public Label usedSicknessPension;


    private LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
    private List<LeaveRequest> leaverequests = leaveRequestDAO.getLeaveRequestsbyEmployeeId(employee.getId());
    /**
     *
     *  3 types of leaves ->
     *      Holiday (Szabadság) (salary 100%)
     *      Sick Leave (Betegszabadság) (salary 70%)
     *      Sickness pension (Táppénz) (salary 60%)
     *
     *      if no more PTO and no more Sick Leave then its Sickness pension
     */
    public final int numberOfRemainingHolidays = 20;
    public final int numberOfRemainingSickLeave = 15;


    public int numberOfUsedHolidays = leaverequests.stream()
            .filter((leaves) ->
            leaves.getLeaveType()
            .equalsIgnoreCase("Holiday"))
            .toList()
            .size();

    public int numberOfUsedSickLeaves = leaverequests.stream()
            .filter((leaves) ->
            leaves.getLeaveType()
            .equalsIgnoreCase("Sick Leave"))
            .toList()
            .size();

    public int numberOfSicknessPension = leaverequests.size() - (numberOfUsedHolidays + numberOfUsedSickLeaves);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        super.initialize(url, resourceBundle);

        remainingHolidays.setText("Holidays: " + (numberOfRemainingHolidays - numberOfUsedHolidays));
        remainingSickLeaves.setText("Sick Leaves: " + (numberOfRemainingSickLeave - numberOfUsedSickLeaves));

        usedHolidays.setText("Holidays: " + numberOfUsedHolidays);
        usedSickLeaves.setText("Sick Leaves: " + numberOfUsedSickLeaves);
        usedSicknessPension.setText("Sickness Pensions: " + numberOfSicknessPension);
    }

}
