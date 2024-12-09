package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.EmployeeDAO;
import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.javaFXApp.data.models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControlController  extends MainController implements Initializable {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @FXML
    private ImageView lbl_close_eye;

    @FXML
    private ImageView lbl_open_eye;

    @FXML
    private  PasswordField control_Password;

    @FXML
    private TextField control_PasswordShow;

    @FXML
    private TextField control_BirthDate;

    @FXML
    private TextField control_HireDate;

    @FXML
    private TextField control_Email;

    @FXML
    private TextField control_EmployeeId;

    @FXML
    private TextField control_FirstName;

    @FXML
    private TextField control_LastName;

    @FXML
    private ComboBox<String> control_Position;

    @FXML
    private TextField control_HourlyWage;

    @FXML
    private Button control_add;

    @FXML
    private TableColumn<Employee, Integer> control_employeeId;

    @FXML
    private TableColumn<Employee, String> control_firstName;

    @FXML
    private TableColumn<Employee, String> control_lastName;

    @FXML
    private TableColumn<Employee, String> control_email;

    @FXML
    private TableColumn<Employee, String> control_position;

    @FXML
    private TableColumn<Employee, Date> control_birthDate;

    @FXML
    private TableColumn<Employee, Date> control_hireDate;

    @FXML
    private TableColumn<Employee, Integer> control_hourlywage;

    @FXML
    private TextField control_search;

    @FXML
    private TableView<Employee> control_tableView;

    @FXML
    private Button control_update;

    @FXML
    private Button control_clear;

    @FXML
    private Button control_delete;

    private ObservableList<Employee> employeeList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);
        loadEmployeeData();  // Load the employee data initially
        setButtonActions();
        addEmployeePositionList();
        control_PasswordShow.setVisible(false);
        lbl_open_eye.setVisible(false);
    }


    // Load employee data from the database and populate the table
    private void loadEmployeeData() {
        // Fetch employee data only once when the controller is initialized
        List<Employee> employees = employeeDAO.getAllEmployees();
        employeeList = FXCollections.observableArrayList(employees);

        // Setting up the table columns to display employee properties
        control_employeeId.setCellValueFactory(new PropertyValueFactory<>("id"));
        control_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        control_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        control_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        control_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        control_birthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        control_hireDate.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        control_hourlywage.setCellValueFactory(new PropertyValueFactory<>("hourlyWage"));

        // Set the table view items
        control_tableView.setItems(employeeList);
    }

    private FilteredList<Employee> filteredList; // Declare filteredList globally

    public void EmployeeSearch() {
        // Create the FilteredList only once when the search method is called for the first time
        if (filteredList == null) {
            filteredList = new FilteredList<>(employeeList, e -> true); // employeeList should be loaded already
        }

        // Add a listener to the search field to filter the list dynamically
        control_search.textProperty().addListener((observable, oldValue, newValue) -> {
            // Log search query for debugging
            System.out.println("Search Query: " + newValue);

            filteredList.setPredicate(employee -> {
                // If the search field is empty, show all employees
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all employees when search is empty
                }

                // Convert the search term to lowercase for case-insensitive matching
                String lowerCaseSearchKey = newValue.toLowerCase();

                // Check if any of the employee fields match the search query
                return String.valueOf(employee.getId()).toLowerCase().contains(lowerCaseSearchKey) ||
                        employee.getFirstName().toLowerCase().contains(lowerCaseSearchKey) ||
                        employee.getLastName().toLowerCase().contains(lowerCaseSearchKey) ||
                        employee.getPosition().toLowerCase().contains(lowerCaseSearchKey) ||
                        employee.getEmail().toLowerCase().contains(lowerCaseSearchKey) ||
                        employee.getBirthDate().toString().toLowerCase().contains(lowerCaseSearchKey) ||
                        employee.getHireDate().toString().toLowerCase().contains(lowerCaseSearchKey);
            });
        });

        // Wrap the filtered list in a SortedList to keep it sorted
        SortedList<Employee> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(control_tableView.comparatorProperty());

        // Update the TableView with the filtered and sorted list
        control_tableView.setItems(sortedList);
    }


    private String[] positionList = {"Boss", "Admin", "Developer"};

    public void addEmployeePositionList() {
        List<String> listP = new ArrayList<>();

        for (String data : positionList) {
            listP.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listP);
        control_Position.setItems(listData);
    }


    // Sets actions for buttons (Add, Update, Delete, Clear)
    private void setButtonActions() {
        control_add.setOnAction(event -> addEmployee());
        control_update.setOnAction(event -> updateEmployee());
        control_delete.setOnAction(event -> deleteEmployee());
        control_clear.setOnAction(event -> clearFields());
        control_search.setOnKeyReleased(event -> EmployeeSearch());
    }


    // Adds a new employee to the database
    private void addEmployee() {
        try {
            if (validateEmployeeFields()) {
                showErrorAlert("Please fill all blank fields.");
                return;
            }

            Employee newEmployee = new Employee(
                    Integer.parseInt(control_EmployeeId.getText()),
                    control_FirstName.getText(),
                    control_LastName.getText(),
                    control_Password.getText(),
                    control_Position.getValue(),
                    control_Email.getText(),
                    Date.valueOf(control_BirthDate.getText()),
                    Date.valueOf(control_HireDate.getText()),
                    Integer.parseInt(control_HourlyWage.getText())
            );

            boolean success = employeeDAO.addEmployee(newEmployee);
            if (success) {
                loadEmployeeData();
                showSuccessAlert("Employee added successfully.");
                clearFields();
            } else {
                showErrorAlert("Error adding employee.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Invalid input or database error.");
        }
    }

    // Updates the selected employee's details in the database
    private void updateEmployee() {
        Employee selectedEmployee = control_tableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showErrorAlert("No employee selected for update.");
            return;
        }

        try {
            selectedEmployee.setFirstName(control_FirstName.getText());
            selectedEmployee.setLastName(control_LastName.getText());
            selectedEmployee.setPosition(control_Position.getValue());

            // Jelszó kezelése
            String password = control_Password.isVisible() ? control_Password.getText() : control_PasswordShow.getText();
            selectedEmployee.setPassword(password);

            selectedEmployee.setEmail(control_Email.getText());
            selectedEmployee.setBirthDate(Date.valueOf(control_BirthDate.getText()));
            selectedEmployee.setHireDate(Date.valueOf(control_HireDate.getText()));
            selectedEmployee.setHourlyWage(Integer.parseInt(control_HourlyWage.getText()));

            boolean success = employeeDAO.updateEmployee(selectedEmployee);
            if (success) {
                loadEmployeeData();
                showSuccessAlert("Employee updated successfully.");
                clearFields();
            } else {
                showErrorAlert("Error updating employee.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Invalid input or database error.");
        }
    }
    public void selectEmployee() {
        Employee selectedEmployee = control_tableView.getSelectionModel().getSelectedItem();
        int num = control_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        // Populate form fields with employee details
        control_EmployeeId.setText(String.valueOf(selectedEmployee.getId()));
        control_FirstName.setText(selectedEmployee.getFirstName());
        control_LastName.setText(selectedEmployee.getLastName());
        control_Password.setText(selectedEmployee.getPassword());
        control_PasswordShow.setText(selectedEmployee.getPassword());
        control_Email.setText(selectedEmployee.getEmail());
        control_Position.setValue(selectedEmployee.getPosition());
        control_BirthDate.setText(selectedEmployee.getBirthDate().toString());
        control_HireDate.setText(selectedEmployee.getHireDate().toString());
        control_HourlyWage.setText(String.valueOf(selectedEmployee.getHourlyWage()));
    }


    // Deletes the selected employee from the database
    private void deleteEmployee() {
        Employee selectedEmployee = control_tableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showErrorAlert("No employee selected for deletion.");
            return;
        }

        try {
            boolean success = employeeDAO.deleteEmployee(selectedEmployee.getId());
            if (success) {
                loadEmployeeData();
                //showSuccessAlert("Employee deleted successfully.");
            } else {
                loadEmployeeData();
                //showErrorAlert("Error deleting employee.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Database error during deletion.");
        }
    }

    // Clears the input fields after an operation
    private void clearFields() {
        control_EmployeeId.clear();
        control_FirstName.clear();
        control_LastName.clear();
        control_Password.clear();
        control_Position.setValue("Choose");
        control_Email.clear();
        control_BirthDate.clear();
        control_HireDate.clear();
        control_HourlyWage.clear();
        control_PasswordShow.clear();
    }

    // Validates if all fields are filled out
    private boolean validateEmployeeFields() {
        return control_EmployeeId.getText().isEmpty() ||
                control_FirstName.getText().isEmpty() ||
                control_LastName.getText().isEmpty() ||
                control_Position.getValue() == null ||
                control_Email.getText().isEmpty() ||
                control_BirthDate.getText().isEmpty() ||
                control_HireDate.getText().isEmpty() ||
                control_HourlyWage.getText().isEmpty();
    }

    public void Open_Eye_ClickOnAction(MouseEvent mouseEvent) {
        control_PasswordShow.setText(control_Password.getText()); // Szinkronizálás
        control_Password.setVisible(true);
        lbl_open_eye.setVisible(false);
        lbl_close_eye.setVisible(true);
        control_PasswordShow.setVisible(false);
    }

    public void Close_Eye_ClickOnAction(MouseEvent mouseEvent) {
        control_Password.setText(control_PasswordShow.getText()); // Szinkronizálás
        control_PasswordShow.setVisible(true);
        lbl_open_eye.setVisible(true);
        lbl_close_eye.setVisible(false);
        control_Password.setVisible(false);
    }

    // Displays a success alert
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Displays an error alert
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}