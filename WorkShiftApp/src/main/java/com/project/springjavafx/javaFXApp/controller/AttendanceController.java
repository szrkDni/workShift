package com.project.springjavafx.javaFXApp.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.project.springjavafx.javaFXApp.data.dao.LeaveRequestDAO;
import com.project.springjavafx.javaFXApp.data.dao.WorkShiftDAO;
import com.project.springjavafx.javaFXApp.data.models.LeaveRequest;
import com.project.springjavafx.javaFXApp.data.models.WorkShift;
import com.project.springjavafx.javaFXApp.utility.DayStatus;
import com.project.springjavafx.javaFXApp.utility.SceneLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class AttendanceController extends MainController implements Initializable {

    @FXML
    VBox mainLayout;

    @FXML
    AnchorPane claendarAnchorpane;

    @FXML
    Pane attendNormalButton;

    @FXML
    Pane attendExtraButton;

    @FXML
    Label numberOfNormalHours;

    @FXML
    Label numberOfExtraHours;

    @FXML
    Label numberOfBreaks;

    @FXML
    Label totalHoursToday;

    ///////////////
    @FXML
    Label totalWorkedHoursThisMonthLabel;

    @FXML
    Label normalThisMonth;

    @FXML
    Label extraThisMonth;

    @FXML
    Label totalWorkDaysThisMonth;

    @FXML
    Label workDaysThisMonth;

    @FXML
    Label takenHolidays;
    
    @FXML
    Label takenSickness;

    @FXML
    PieChart pieChartOfHours;

    @FXML
    PieChart pieChartOfDays;

    @FXML
    AnchorPane claendarAnchorpane11;
    
    private YearMonth currentYearMonth;

    private boolean isAttendedNormalToday = false;
    private boolean isAttendedExtraToday = false;

    private OptionalInt maxIdOfAttendance = OptionalInt.empty();


    private final WorkShiftDAO workShiftDAO = new WorkShiftDAO();

    private final List<WorkShift> workShiftsOfEmployee = workShiftDAO.getAllWorkShifts().stream().filter(workShift -> workShift.getEmployeeId() == employee.getId()).toList();

    private final List<LeaveRequest> leaveRequestsOfEmployee = new LeaveRequestDAO().getLeaveRequestsbyEmployeeId(employee.getId()).stream().filter(leaveRequest -> leaveRequest.getStatus().equalsIgnoreCase("approved")).toList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DayStatus.extraHoursToday = 0;
        DayStatus.normalHoursToday = 0;
        DayStatus.breakHoursToday = 0;

        maxIdOfAttendance = workShiftDAO.getAllWorkShifts().stream().mapToInt(WorkShift::getId).max();

        try {

            currentYearMonth = YearMonth.now();

            AtomicReference<GridPane> calendarGrid = new AtomicReference<>(createCalendar(currentYearMonth, workShiftsOfEmployee, leaveRequestsOfEmployee));

            super.initialize(url, resourceBundle);

            attendNormalButton.setOnMouseClicked(event -> {
                WorkShift normalWorkshift = new WorkShift(maxIdOfAttendance.getAsInt() + 1, employee.getId(), Date.valueOf(LocalDate.now()), 8.0, "Normal");
                boolean success = workShiftDAO.addShift(normalWorkshift);
                attendNormalButton.setDisable(true);

                try{
                    SceneLoader.showScene(event,"attendanceFXML");

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

            });

            attendExtraButton.setOnMouseClicked(event -> {
                WorkShift normalWorkshift = new WorkShift(maxIdOfAttendance.getAsInt() + 1, employee.getId(), Date.valueOf(LocalDate.now()), 4.0, "Extra");
                boolean success = workShiftDAO.addShift(normalWorkshift);
                attendExtraButton.setDisable(true);

                try{
                    SceneLoader.showScene(event,"attendanceFXML");

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }


            });

            if (!isAttendedNormalToday)
            {
                attendExtraButton.setDisable(true);
            }

            mainLayout.setSpacing(10);


            setLayout(calendarGrid);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        numberOfNormalHours.setText("Normal: " + DayStatus.normalHoursToday);
        numberOfExtraHours.setText("Extra: " + DayStatus.extraHoursToday);
        numberOfBreaks.setText("Break: " + DayStatus.breakHoursToday);
        totalHoursToday.setText("Total: " + (DayStatus.breakHoursToday + DayStatus.normalHoursToday + DayStatus.extraHoursToday));

    }


    public void setLayout(AtomicReference<GridPane> calendarGrid)
    {

        try {
            mainLayout.setAlignment(Pos.CENTER);
            mainLayout.getChildren().add(calendarGrid.get());


            Button nextMonthButton = new Button(">");
            nextMonthButton.setPrefSize(40, 30);

            Button previousMonthButton = new Button("<");
            previousMonthButton.setPrefSize(40, 30);

            HBox hbox = new HBox(3, previousMonthButton, nextMonthButton);
            hbox.setAlignment(Pos.CENTER);


            previousMonthButton.setOnMouseClicked(event -> {
                calendarGrid.set(createCalendar(currentYearMonth.minusMonths(1), workShiftsOfEmployee, leaveRequestsOfEmployee));
                mainLayout.getChildren().add(calendarGrid.get());
                mainLayout.getChildren().add(hbox);

            });


            nextMonthButton.setOnMouseClicked(event -> {
                calendarGrid.set(createCalendar(currentYearMonth.plusMonths(1), workShiftsOfEmployee, leaveRequestsOfEmployee));
                mainLayout.getChildren().add(calendarGrid.get());
                mainLayout.getChildren().add(hbox);
            });

            if (isAttendedNormalToday) {
                attendNormalButton.setDisable(true);
            }

            if (isAttendedExtraToday) {
                attendExtraButton.setDisable(true);
            }


            mainLayout.getChildren().add(hbox);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public GridPane createCalendar(YearMonth yearMonth, List<WorkShift> workShifts, List<LeaveRequest> leaveRequests) {

        GridPane calendar = new GridPane();

        try {
            workShifts = workShifts.stream().filter(workShift -> yearMonth.getMonth().getValue() == (workShift.getWorkdayDate().getMonth() + 1)).toList();
            leaveRequests = leaveRequests.stream().filter(request -> yearMonth.getMonth().getValue() == (request.getStartDate().getMonth() +1)).toList();

            currentYearMonth = yearMonth;
            mainLayout.getChildren().clear();

            Label month = new Label(currentYearMonth.getMonth().toString());
            month.getStyleClass().add("monthNameText");
            mainLayout.getChildren().add(month);


            calendar.setAlignment(Pos.CENTER);

            // Add day headers
            String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            for (int i = 0; i < days.length; i++) {
                Text dayName = new Text(days[i]);
                dayName.getStyleClass().add("dayNameText");
                calendar.add(dayName, i, 0);
                GridPane.setHalignment(dayName, HPos.CENTER);
            }

            // Get the first day of the month
            LocalDate firstDay = yearMonth.atDay(1);
            int dayOfWeek = firstDay.getDayOfWeek().getValue(); // Monday = 1

            boolean isAttendedNormal = false;
            boolean isAttendedExtra = false;




            // Fill calendar with buttons
            int dayCounter = 1;
            for (int row = 1; row <= 6; row++) { // Max 6 rows in a month view
                for (int col = 0; col < 7; col++) {

                    Pane dayPane = new Pane();
                    dayPane.getStyleClass().add("dayPane");

                    if (row == 1 && col < dayOfWeek - 1) {
                        // Empty spaces before the first day
                        dayPane.setStyle("-fx-background-color: #d0d0cf");
                        calendar.add(dayPane, col, row);
                        continue;
                    }
                    if (dayCounter > yearMonth.lengthOfMonth()) {

                        dayPane.setStyle("-fx-background-color: #d0d0cf");
                        calendar.add(dayPane, col, row);

                        continue;
                    }

                    Text text = new Text(String.valueOf(dayCounter));
                    text.setTextAlignment(TextAlignment.CENTER);
                    text.getStyleClass().add("dayNameText");
                    VBox vbox = new VBox(text);
                    vbox.setSpacing(2);
                    vbox.setPadding(new Insets(0,0,0,4));


                    Label normalHours = getCustomLabel("red");
                    Tooltip normalTooltip = new Tooltip("Normal 8 hours shift (+ 1 hour break)");
                    Tooltip.install(normalHours,normalTooltip);

                    Label extraHours = getCustomLabel( "green");
                    Tooltip extraTooltip = new Tooltip("Overtime, 4 hours, paid 200%");
                    Tooltip.install(extraHours,extraTooltip);

                    Label holidayLabel = getCustomLabel( "#ffcc00");
                    Tooltip holdayToolTip = new Tooltip("Holiday, paid 100%");
                    Tooltip.install(holidayLabel,holdayToolTip);


                    Label sicknessPensionLabel = getCustomLabel( "#ccbccb");
                    Tooltip sicknessPensionToolTip = new Tooltip("Sickness Pension, paid 60%");
                    Tooltip.install(sicknessPensionLabel,sicknessPensionToolTip);

                    Label sickLeaveLabel = getCustomLabel( "#003f5c");
                    Tooltip sickLeaveTooltip = new Tooltip("Sick Leave, paid 70%");
                    Tooltip.install(sickLeaveLabel,sickLeaveTooltip);

                    isAttendedNormal = false;
                    isAttendedExtra = false;

                    int finalDayCounter = dayCounter;

                    List<WorkShift> currentDayShifts = workShifts.stream().filter(workShift ->
                            workShift.getWorkdayDate().toLocalDate().getDayOfMonth() == finalDayCounter).toList();


                    List<LeaveRequest> currentDayTimeOffs = leaveRequests.stream().filter(leaveRequest ->
                            leaveRequest.getStartDate().toLocalDate().isEqual(ChronoLocalDate.from(LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(),finalDayCounter))) ||
                            leaveRequest.getEndDate().toLocalDate().isEqual(ChronoLocalDate.from(LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(),finalDayCounter))) ||
                            LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(),finalDayCounter).isAfter(ChronoLocalDate.from(leaveRequest.getStartDate().toLocalDate())) &&
                            LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(),finalDayCounter).isBefore(ChronoLocalDate.from(leaveRequest.getEndDate().toLocalDate())
                    )).toList();


                    boolean isOnHoliday = false;
                    boolean isOnSickLeave = false;
                    boolean isOnSicknessPension = false;

                    if (!currentDayShifts.isEmpty()) {
                        for (WorkShift workShift : currentDayShifts) {
                            if (workShift.getShiftType().equalsIgnoreCase("normal")) {
                                isAttendedNormal = true;
                                if (workShift.getWorkdayDate().compareTo(Date.valueOf(LocalDate.now())) == 0)
                                {
                                    DayStatus.breakHoursToday++;
                                    DayStatus.normalHoursToday+=8;
                                }
                            } else if (workShift.getShiftType().equalsIgnoreCase("extra")) {
                                isAttendedExtra = true;

                                if (workShift.getWorkdayDate().compareTo(Date.valueOf(LocalDate.now())) == 0)
                                {
                                    DayStatus.extraHoursToday+=4;
                                }

                            }


                        }


                    }

                    if(!currentDayTimeOffs.isEmpty()) {

                            if (currentDayTimeOffs.get(0).getLeaveType().equalsIgnoreCase("holiday")) {
                                isOnHoliday = true;
                            }
                            else if (currentDayTimeOffs.get(0).getLeaveType().equalsIgnoreCase("Sick Leave")) {
                                isOnSickLeave = true;
                            }
                            else if (currentDayTimeOffs.get(0).getLeaveType().equalsIgnoreCase("Sickness Pension")) {
                                isOnSicknessPension = true;
                            }

                    }

                    if (finalDayCounter == LocalDate.now().getDayOfMonth()) {
                        if (isAttendedNormal) {
                            isAttendedNormalToday = true;
                        }

                        if (isAttendedExtra) {
                            isAttendedExtraToday = true;
                        }
                    }


                    if (isAttendedNormal)
                    {
                        vbox.getChildren().add(normalHours);
                    }

                    if (isAttendedExtra)
                    {
                        vbox.getChildren().add(extraHours);
                    }

                    if (isOnHoliday)
                    {
                        vbox.getChildren().add(holidayLabel);
                    }

                    if (isOnSickLeave)
                    {
                        vbox.getChildren().add(sickLeaveLabel);
                    }

                    if (isOnSicknessPension)
                    {
                        vbox.getChildren().add(sicknessPensionLabel);
                    }


                    dayPane.getChildren().add(vbox);


                    calendar.add(dayPane, col, row);
                    dayCounter++;
                }

                if (dayCounter > yearMonth.lengthOfMonth()) {

                    break;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());

            return new GridPane();
        }

        initialiseMonthlyData(workShiftsOfEmployee, leaveRequestsOfEmployee, yearMonth);

        return calendar;
    }

    private Label getCustomLabel(String color)
    {
        Label label = new Label("---------------");
        label.setAlignment(Pos.CENTER);
        label.setStyle(String.format("-fx-text-fill: %s; -fx-background-color: %s;",color,color));
        label.getStyleClass().add("shiftLabels");
        label.setLayoutX(5);


        return label;
    }

    private void initialiseMonthlyData(List<WorkShift> workShifts, List<LeaveRequest> leaveRequests, YearMonth yearMonth)
    {


        workShifts = workShifts.stream().filter(workShift -> yearMonth.getMonth().getValue() == (workShift.getWorkdayDate().getMonth() + 1)).toList();
        leaveRequests = leaveRequests.stream().filter(request -> yearMonth.getMonth().getValue() == (request.getStartDate().getMonth() +1)).toList();

        //in days
        int numberOfDaysWorkedThisMonth = workShifts.stream().filter(workShift -> workShift.getWorkHour() == 8).toList().size();

        int numberOfTakenHolidaysThisMonth = 0;
        int numberOfSickDaysThisMonth = 0;

        for (LeaveRequest request : leaveRequests)
        {


            if (request.getLeaveType().equalsIgnoreCase("Holiday"))
            {
                numberOfTakenHolidaysThisMonth += (int) ChronoUnit.DAYS.between(request.getStartDate().toLocalDate(), request.getEndDate().toLocalDate().plusDays(1));
            }
            else{
                numberOfSickDaysThisMonth += (int) ChronoUnit.DAYS.between(request.getStartDate().toLocalDate(), request.getEndDate().toLocalDate().plusDays(1));
            }
        }


        int totalWorkReleatedDaysThisMonth = numberOfDaysWorkedThisMonth + numberOfTakenHolidaysThisMonth + numberOfSickDaysThisMonth;

        totalWorkDaysThisMonth.setText("Days: " + totalWorkReleatedDaysThisMonth);
        workDaysThisMonth.setText("Working days: " + numberOfDaysWorkedThisMonth);
        takenHolidays.setText("Holidays: " + numberOfTakenHolidaysThisMonth);
        takenSickness.setText("Sickness: " + numberOfSickDaysThisMonth);

        //in hours
        int numberOfNormalWorkedHoursThisMonth = numberOfDaysWorkedThisMonth * 8;
        int numberOfOvertimeHoursThisMonth = (workShifts.size() - numberOfDaysWorkedThisMonth) * 4;
        int totalWorkedHoursThisMonth = numberOfNormalWorkedHoursThisMonth + numberOfOvertimeHoursThisMonth;


        totalWorkedHoursThisMonthLabel.setText("Worked Hours: " + totalWorkedHoursThisMonth);
        normalThisMonth.setText("Normal: " + numberOfNormalWorkedHoursThisMonth);
        extraThisMonth.setText("Extra: " + numberOfOvertimeHoursThisMonth);


        ObservableList<PieChart.Data> pieChartData = FXCollections.observableList(List.of(
                new PieChart.Data("Normal", numberOfNormalWorkedHoursThisMonth),
                new PieChart.Data("Extra", numberOfOvertimeHoursThisMonth))
        );

        //piechart
        pieChartOfHours.setData(pieChartData);

        pieChartOfHours.setLabelLineLength(5);
        pieChartOfHours.getStyleClass().add("pieChart");

        //piechart days

        pieChartData = FXCollections.observableList(List.of(
                new PieChart.Data("Work", numberOfDaysWorkedThisMonth),
                new PieChart.Data("Holiday", numberOfTakenHolidaysThisMonth),
                new PieChart.Data("Sickness", numberOfTakenHolidaysThisMonth)

        ));

        pieChartOfDays.setData(pieChartData);

        pieChartOfDays.setLabelLineLength(5);
        pieChartOfDays.getStyleClass().add("pieChart");


    }
}
