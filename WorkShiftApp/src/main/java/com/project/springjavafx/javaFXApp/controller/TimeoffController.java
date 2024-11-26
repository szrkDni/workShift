package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.LeaveRequestDAO;
import com.project.springjavafx.javaFXApp.data.models.LeaveRequest;
import com.project.springjavafx.javaFXApp.utility.SceneLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @FXML
    private TableView<LeaveRequest> leaveRequestsTable;

    @FXML
    private TableColumn<LeaveRequest,Date> startdateTableColumn;

    @FXML
    private TableColumn<LeaveRequest,Date> enddateTableColumn;

    @FXML
    private TableColumn<LeaveRequest,String> typeOfLeaveTableColumn;

    @FXML
    private TableColumn<LeaveRequest,String> statusOfRequestTableColumn;

    @FXML
    private TableColumn<LeaveRequest,Integer> managedByTableColumn;

    private LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
    protected List<LeaveRequest> leaveRequests1 = leaveRequestDAO.getLeaveRequestsbyEmployeeId(employee.getId());

    List<LeaveRequest> leaveRequests = List.of(
            new LeaveRequest(1,1,"Holiday", new Date(2024, 11, 1), new Date(2024, 11, 4), "Pending", 0),
            new LeaveRequest(1,1,"Sick Leave", new Date(2024, 10, 5), new Date(2024, 10, 10), "Pending", 0),
            new LeaveRequest(1,1,"Holiday", new Date(2024, 12, 20), new Date(2024, 12, 25), "Pending", 0),
            new LeaveRequest(1,1,"Holiday", new Date(2024, 5, 12), new Date(2024, 5, 19), "Pending", 0)
    );

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
            .filter((leaves) -> leaves.getLeaveType().equalsIgnoreCase("Holiday"))
            .mapToInt((leaves) ->
            {
                LocalDate startDate = leaves.getStartDate().toLocalDate();
                LocalDate endDate = leaves.getEndDate().toLocalDate();
                return (int) ChronoUnit.DAYS.between(startDate, endDate);

            })
            .sum();

    public int numberOfUsedSickLeaves = leaveRequests.stream()
            .filter((leaves) -> leaves.getLeaveType().equalsIgnoreCase("Sick Leave"))
            .mapToInt((leaves) ->
            {
                LocalDate startDate = leaves.getStartDate().toLocalDate();
                LocalDate endDate = leaves.getEndDate().toLocalDate();
                return (int) ChronoUnit.DAYS.between(startDate, endDate);

            })
            .sum();

    public int numberOfSicknessPension = leaveRequests.stream()
            .filter((leaves) -> leaves.getLeaveType().equalsIgnoreCase("Sickness Pension"))
            .mapToInt((leaves) ->
            {
                LocalDate startDate = leaves.getStartDate().toLocalDate();
                LocalDate endDate = leaves.getEndDate().toLocalDate();
                return (int) ChronoUnit.DAYS.between(startDate, endDate);

            })
            .sum();

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


            startdateTableColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            enddateTableColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            typeOfLeaveTableColumn.setCellValueFactory(new PropertyValueFactory<>("leaveType"));
            statusOfRequestTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            managedByTableColumn.setCellValueFactory(new PropertyValueFactory<>("approvedBy"));

            ObservableList<LeaveRequest> tableList = FXCollections.observableList(leaveRequests);

            leaveRequestsTable.setItems(tableList);
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
