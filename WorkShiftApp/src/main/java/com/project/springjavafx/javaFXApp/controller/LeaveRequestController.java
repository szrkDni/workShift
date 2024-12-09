package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.LeaveRequestDAO;
import com.project.springjavafx.javaFXApp.data.models.LeaveRequest;
import com.project.springjavafx.javaFXApp.utility.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;


public class LeaveRequestController extends MainController implements Initializable {
    @FXML
    public DatePicker startDateField;

    @FXML
    public DatePicker endDateField;

    @FXML
    public ComboBox<String> leaveTypeSelection;

    @FXML
    public Button submitTimeOffRequest;

    @FXML
    public Label remainingHolidayLR;

    @FXML
    public Label remainingSickLeaveLR;

    LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        remainingHolidayLR.setText(String.valueOf(TimeoffController.staticremainingHolidays));
        remainingSickLeaveLR.setText(String.valueOf(TimeoffController.staticremainingSickness));
    }

    @FXML
    public void onFinaliseRequest(MouseEvent mouseEvent)
    {
        LeaveRequest request = null;

        try {
            request = new LeaveRequest(
                    TimeoffController.numberOfRequests.getAsInt() + 1,
                    employee.getId(),
                    leaveTypeSelection.getValue(),
                    Date.valueOf(startDateField.getValue()),
                    Date.valueOf(endDateField.getValue()),
                    "Pending"
            );

            leaveRequestDAO.addLeaveRequest(request);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("Request:\n" + request.getLeaveId() + " " + request.getLeaveType() + " " + request.getStatus() + "\n" + request.getStartDate() + " " + request.getEndDate());

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try{
            if (request == null)
            {
                throw new NullPointerException("request is null");
            }
            SceneLoader.showScene(mouseEvent, "timeoffFXML");

        } catch (IOException e) {
            System.out.println(e.getCause());
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
}
