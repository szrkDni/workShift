package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.LeaveRequestDAO;
import com.project.springjavafx.javaFXApp.data.dao.WorkShiftDAO;
import com.project.springjavafx.javaFXApp.data.models.LeaveRequest;
import com.project.springjavafx.javaFXApp.data.models.WorkShift;
import com.project.springjavafx.javaFXApp.utility.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class AttendanceController extends MainController implements Initializable {

    @FXML
    VBox mainLayout;

    @FXML
    AnchorPane claendarAnchorpane;

    @FXML
    Button attendNormalButton;

    @FXML
    Button attendExtraButton;

    private YearMonth currentYearMonth;

    private boolean isAttendedNormalToday = false;
    private boolean isAttendedExtraToday = false;
    private boolean isOnHoliday = false;
    private boolean isOnSickLeave = false;
    private boolean isOnSicknessPension = false;

    private OptionalInt maxIdOfAttendance = OptionalInt.empty();



    private WorkShiftDAO workShiftDAO = new WorkShiftDAO();

    private List<WorkShift> workShiftsOfEmployee = workShiftDAO.getAllWorkShifts().stream().filter(workShift -> workShift.getEmployeeId() == employee.getId()).toList();

    private List<LeaveRequest> leaveRequestsOfEmployee = new LeaveRequestDAO().getLeaveRequestsbyEmployeeId(employee.getId()).stream().filter(leaveRequest -> !leaveRequest.getStatus().equalsIgnoreCase("pending")).toList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        maxIdOfAttendance = workShiftDAO.getAllWorkShifts().stream().mapToInt(WorkShift::getId).max();

        try {

            currentYearMonth = YearMonth.now();

            AtomicReference<GridPane> calendarGrid = new AtomicReference<>(createCalendar(currentYearMonth, workShiftsOfEmployee, leaveRequestsOfEmployee));

            super.initialize(url, resourceBundle);

            attendNormalButton.setOnMouseClicked(event -> {
                WorkShift normalWorkshift = new WorkShift(maxIdOfAttendance.getAsInt() + 1, employee.getId(), Date.valueOf(LocalDate.now()), 8.0, "Normal");
                boolean success = workShiftDAO.addShift(normalWorkshift);
                //attendNormalButton.setDisable(true);

                try{
                    SceneLoader.showScene(event,"attendanceFXML");

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            });

            attendExtraButton.setOnMouseClicked(event -> {
                WorkShift normalWorkshift = new WorkShift(maxIdOfAttendance.getAsInt() + 1, employee.getId(), Date.valueOf(LocalDate.now()), 4.0, "Extra");
                boolean success = workShiftDAO.addShift(normalWorkshift);
                //attendExtraButton.setDisable(true);
                setLayout(calendarGrid);

                try{
                    SceneLoader.showScene(event,"attendanceFXML");

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            });


            mainLayout.setSpacing(10);


            setLayout(calendarGrid);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

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


            mainLayout.getChildren().add(new Label(currentYearMonth.getMonth().toString()));


            calendar.setAlignment(Pos.CENTER);

            // Add day headers
            String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            for (int i = 0; i < days.length; i++) {
                Text dayName = new Text(days[i]);
                calendar.add(dayName, i, 0);
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
                        Pane disabledDayPane = dayPane;
                        disabledDayPane.setStyle("-fx-background-color: grey");
                        calendar.add(disabledDayPane, col, row);
                        continue;
                    }
                    if (dayCounter > yearMonth.lengthOfMonth()) {

                        Pane disabledDayPane = dayPane;
                        disabledDayPane.setStyle("-fx-background-color: grey");
                        calendar.add(disabledDayPane, col, row);

                        continue;
                    }

                    VBox vbox = new VBox(new Text(String.valueOf(dayCounter)));
                    //vbox.setAlignment(Pos.CENTER);

                    Label normalHours = new Label("Normal");
                    normalHours.setAlignment(Pos.CENTER);
                    normalHours.setTextFill(Color.WHITE);
                    normalHours.setStyle("-fx-background-color: red;");
                    normalHours.getStyleClass().add("shiftLabels");


                    Label extraHours = new Label("Extra");
                    extraHours.setAlignment(Pos.CENTER);
                    extraHours.setTextFill(Color.WHITE);
                    extraHours.setStyle("-fx-background-color: green;");
                    extraHours.getStyleClass().add("shiftLabels");


                    Label holidayLabel = new Label("Holiday");
                    holidayLabel.setAlignment(Pos.CENTER);
                    holidayLabel.setTextFill(Color.WHITE);
                    holidayLabel.setStyle("-fx-background-color: orange;");
                    holidayLabel.getStyleClass().add("shiftLabels");

                    Label sicknessPensionLabel = new Label("Sickness Pension");
                    sicknessPensionLabel.setAlignment(Pos.CENTER);
                    sicknessPensionLabel.setTextFill(Color.WHITE);
                    sicknessPensionLabel.setStyle("-fx-background-color: grey;");
                    sicknessPensionLabel.getStyleClass().add("shiftLabels");

                    Label sickLeaveLabel = new Label("Sick Leave");
                    sickLeaveLabel.setAlignment(Pos.CENTER);
                    sickLeaveLabel.setTextFill(Color.WHITE);
                    sickLeaveLabel.setStyle("-fx-background-color: grey;");
                    sickLeaveLabel.getStyleClass().add("shiftLabels");

                    isAttendedNormal = false;
                    isAttendedExtra = false;

                    int finalDayCounter = dayCounter;

                    List<WorkShift> currentDayShifts = workShifts.stream().filter(workShift ->
                            workShift.getWorkdayDate().toLocalDate().getDayOfMonth() == finalDayCounter).toList();


                    List<LeaveRequest> currentDayTimeOffs = leaveRequests.stream().filter(leaveRequest ->
                            leaveRequest.getStartDate().toLocalDate().isEqual(ChronoLocalDate.from(LocalDate.of(Year.now().getValue(), YearMonth.now().getMonth(),finalDayCounter))) ||
                            leaveRequest.getEndDate().toLocalDate().isEqual(ChronoLocalDate.from(LocalDate.of(Year.now().getValue(), YearMonth.now().getMonth(),finalDayCounter))) ||
                            LocalDate.of(Year.now().getValue(), YearMonth.now().getMonth(),finalDayCounter).isAfter(ChronoLocalDate.from(leaveRequest.getStartDate().toLocalDate())) &&
                            LocalDate.of(Year.now().getValue(), YearMonth.now().getMonth(),finalDayCounter).isBefore(ChronoLocalDate.from(leaveRequest.getEndDate().toLocalDate())
                    )).toList();




                    isOnHoliday = false;
                    isOnSickLeave = false;
                    isOnSicknessPension = false;

                    if (!currentDayShifts.isEmpty()) {
                        for (WorkShift workShift : currentDayShifts) {
                            if (workShift.getShiftType().equalsIgnoreCase("normal")) {
                                isAttendedNormal = true;
                            } else if (workShift.getShiftType().equalsIgnoreCase("extra")) {
                                isAttendedExtra = true;
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
        return calendar;
    }
}
