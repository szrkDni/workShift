package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.utility.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class ProfileController extends MainController implements Initializable
{
        @FXML
        private TextField birth_date_textField;

        @FXML
        private AnchorPane controlAnchorPane;

        @FXML
        private Button controlbutton;

        @FXML
        private TextField email_textField;

        @FXML
        private TextField first_name_textField;

        @FXML
        private Label hire_date_label;

        @FXML
        private Label hourly_wage_label;

        @FXML
        private TextField last_name_textField;

        @FXML
        private Label nameLabel;

        @FXML
        private PasswordField password_textField;

        @FXML
        private Label positionLabel;

        @FXML
        private Label position_label;

        @FXML
        private Button profile_save_changes;

        @FXML
        private Label work_hard_label;

        @FXML
        void onSaveMouseClicked(MouseEvent event) {
                employee.setFirstName(first_name_textField.getText());
                employee.setLastName(last_name_textField.getText());
                employee.setEmail(email_textField.getText());
                employee.setPassword(password_textField.getText());
                employee.setBirthDate(Date.valueOf(birth_date_textField.getText()));

                employeeDAO.updateEmployee(employee);
                try {
                        SceneLoader.showScene(event,"profileFXML");
                } catch (IOException e) {
                        System.out.println(e.getMessage());
                }
        }

        @FXML
        void onWorkHardEntered(MouseEvent event) {

        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                super.initialize(url, resourceBundle);
                first_name_textField.setText(employee.getFirstName());
                last_name_textField.setText(employee.getLastName());
                email_textField.setText(employee.getEmail());
                password_textField.setText(employee.getPassword());
                birth_date_textField.setText(employee.getBirthDate().toString());
                position_label.setText(employee.getPosition());
                hire_date_label.setText(employee.getHireDate().toString());
                hourly_wage_label.setText(String.valueOf(employee.getHourlyWage()));

                int x = employee.getHourlyWage();
                int poor_year = 245 * 8 * x;
                int rich_year = 260 * 8 * x;
                work_hard_label.setText(String.valueOf(poor_year)+ "Ft" + " - " + String.valueOf(rich_year) + "Ft");
        }
}
