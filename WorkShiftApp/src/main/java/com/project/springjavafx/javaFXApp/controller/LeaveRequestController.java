package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.models.LeaveRequest;
import com.project.springjavafx.javaFXApp.utility.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Date;


public class LeaveRequestController extends MainController {
    @FXML
    public DatePicker startDateField;

    @FXML
    public DatePicker endDateField;

    @FXML
    public ComboBox<String> leaveTypeSelection;

    @FXML
    public Button submitTimeOffRequest;


    @FXML
    public void onFinaliseRequest(MouseEvent mouseEvent)
    {

        LeaveRequest request = new LeaveRequest(5,1,leaveTypeSelection.getValue(),Date.valueOf(startDateField.getValue()), Date.valueOf(endDateField.getValue()),"Pending",0);

        System.out.println("Request:\n" + request.getLeaveType() + " " + request.getStatus() + "\n" + request.getStartDate() + " " + request.getEndDate());

        try{
            SceneLoader.showScene(mouseEvent, "timeoffFXML");

        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }
}
