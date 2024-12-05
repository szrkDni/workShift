package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.WorkShiftDAO;
import com.project.springjavafx.javaFXApp.data.models.WorkShift;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class AttendanceController extends MainController implements Initializable {

    @FXML
    VBox mainLayout;

    @FXML
    AnchorPane claendarAnchorpane;

    private YearMonth currentYearMonth;

    private WorkShiftDAO workShiftDAO = new WorkShiftDAO();

    private List<WorkShift> workShiftsOfEmployee = workShiftDAO.getAllWorkShifts().stream().filter(workShift -> workShift.getEmployeeId() == employee.getId()).toList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        super.initialize(url, resourceBundle);

        currentYearMonth = YearMonth.now();

        mainLayout.setSpacing(5);

        AtomicReference<GridPane> calendarGrid = new AtomicReference<>(createCalendar(currentYearMonth, workShiftsOfEmployee));

        setLayout(calendarGrid);
    }



    public void setLayout(AtomicReference<GridPane> calendarGrid)
    {
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().add(calendarGrid.get());

        Button nextMonthButton = new Button(">");
        nextMonthButton.setPrefSize(40, 30);

        Button previousMonthButton = new Button("<");
        previousMonthButton.setPrefSize(40, 30);

        HBox hbox = new HBox(3, previousMonthButton, nextMonthButton);
        hbox.setAlignment(Pos.CENTER);


        previousMonthButton.setOnAction(event -> {
            calendarGrid.set(createCalendar(currentYearMonth.minusMonths(1), workShiftsOfEmployee));
            mainLayout.getChildren().add(calendarGrid.get());
            mainLayout.getChildren().add(hbox);
        });

        nextMonthButton.setOnAction(event -> {
            calendarGrid.set(createCalendar(currentYearMonth.plusMonths(1), workShiftsOfEmployee));
            mainLayout.getChildren().add(calendarGrid.get());
            mainLayout.getChildren().add(hbox);
        });

        mainLayout.getChildren().add(hbox);
    }

    public GridPane createCalendar(YearMonth yearMonth, List<WorkShift> workShifts) {

        workShifts = workShifts.stream().filter(workShift -> yearMonth.getMonth().getValue() == (workShift.getWorkdayDate().getMonth()+1)).toList();


        currentYearMonth = yearMonth;
        mainLayout.getChildren().clear();

        GridPane calendar = new GridPane();
        calendar.setAlignment(Pos.CENTER);
        calendar.setHgap(10);
        calendar.setVgap(10);

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

                isAttendedNormal = false;
                isAttendedExtra = false;

                int finalDayCounter = dayCounter;

                List<WorkShift> currentDayShifts = workShifts.stream().filter(workShift -> workShift.getWorkdayDate().toLocalDate().getDayOfMonth() == finalDayCounter).toList();

                if (currentDayShifts.size() > 0) {
                    for (WorkShift workShift : currentDayShifts) {
                        if (workShift.getShiftType().equalsIgnoreCase("normal"))
                        {
                            isAttendedNormal = true;
                        } else if (workShift.getShiftType().equalsIgnoreCase("extra")) {
                            isAttendedExtra = true;
                        }
                    }
                }

                if (row == 1 && col < dayOfWeek - 1) {
                    // Empty spaces before the first day
                    continue;
                }
                if (dayCounter > yearMonth.lengthOfMonth()) {
                    // Stop if we've passed the last day
                    break;
                }

                VBox vbox = new VBox(new Text(String.valueOf(dayCounter)));
                vbox.setAlignment(Pos.CENTER);

                StackPane stackPane = new StackPane();

                Circle redCircle = new Circle(5, Color.RED);
                Circle greenCircle = new Circle(5, Color.GREEN);

                if (isAttendedNormal && isAttendedExtra) {
                   try{
                       stackPane.getChildren().add(redCircle);
                       stackPane.getChildren().add(greenCircle);

                       StackPane.setAlignment(redCircle, Pos.CENTER_LEFT);
                       StackPane.setAlignment(greenCircle, Pos.CENTER_RIGHT);
                   } catch (Exception e) {
                       System.out.println(e.getCause());
                   }


                }else if (isAttendedNormal)
                {
                    stackPane.getChildren().add(redCircle);
                    StackPane.setAlignment(redCircle, Pos.CENTER);
                }else if (isAttendedExtra) {
                    stackPane.getChildren().add(greenCircle);
                    StackPane.setAlignment(greenCircle, Pos.CENTER);
                }

                vbox.getChildren().add(stackPane);

                Button dayButton = new Button();
                dayButton.setPrefSize(50, 50);

                dayButton.setGraphic(vbox);

                /*dayButton.setOnAction(event -> {
                    System.out.println("Selected date: " + yearMonth.atDay(Integer.parseInt(dayButton.getText())));
                });*/

                calendar.add(dayButton, col, row);
                dayCounter++;
            }
        }

        return calendar;
    }
}
