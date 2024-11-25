package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.LeaveRequestDAO;
import com.project.springjavafx.javaFXApp.data.models.LeaveRequest;
import com.project.springjavafx.javaFXApp.utility.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TimeoffController extends MainController implements Initializable {

    @FXML
    private Button timeoffbutton;

    @FXML
    private Label remainingHolidays;

    @FXML
    private Label remainingSickLeaves;

    @FXML
    private Label usedHolidays;

    @FXML
    private Label usedSickLeaves;

    @FXML
    private Label usedSicknessPension;


    private LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
    protected List<LeaveRequest> leaveRequests = leaveRequestDAO.getLeaveRequestsbyEmployeeId(employee.getId());


    /**
     *
     *  3 types of leaves ->
     *      Holiday (Szabadság) (salary 100%)
     *      Sick Leave (Betegszabadság) (salary 70%)
     *      Sickness pension (Táppénz) (salary 60%)
     *
     *      if no more PTO and no more Sick Leave then its Sickness pension
     */
    private static final int numberOfHolidays = 20;
    private static final int numberOfSickLeave = 15;




    public int numberOfUsedHolidays = leaveRequests.stream()
            .filter((leaves) ->
                    leaves.getLeaveType()
                            .equalsIgnoreCase("Holiday"))
            .toList()
            .size();

    public int numberOfUsedSickLeaves = leaveRequests.stream()
            .filter((leaves) ->
                    leaves.getLeaveType()
                            .equalsIgnoreCase("Sick Leave"))
            .toList()
            .size();

    public int numberOfSicknessPension = leaveRequests.size() - (numberOfUsedHolidays + numberOfUsedSickLeaves);

    public int numberOfRemainingHolidays = numberOfHolidays - numberOfUsedHolidays;
    public int numberOFRemainingSickLeaves = numberOfSickLeave - numberOfUsedSickLeaves;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        super.initialize(url, resourceBundle);

        if (employee != null && leaveRequestDAO.getLeaveRequestsbyEmployeeId(employee.getId()) != null) {

            remainingHolidays.setText("Holidays: " + (numberOfRemainingHolidays));
            remainingSickLeaves.setText("Sick Leaves: " + (numberOFRemainingSickLeaves));

            usedHolidays.setText("Holidays: " + numberOfUsedHolidays);
            usedSickLeaves.setText("Sick Leaves: " + numberOfUsedSickLeaves);
            usedSicknessPension.setText("Sickness Pensions: " + numberOfSicknessPension);

        }


    }


    public void onShowLeaveRequestPageButtonClick(MouseEvent mouseEvent)
    {
        try{
            SceneLoader.showScene(mouseEvent, "leaveRequestFormFXML");

        } catch (IOException e) {
            System.out.println("ERROR:" + e.getMessage());
            System.out.println("CAUSE:" + e.getCause());
        }
    }

}
