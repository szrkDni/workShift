package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.EmployeeDAO;
import com.project.springjavafx.javaFXApp.data.dto.AfterLoginDTO;
import com.project.springjavafx.javaFXApp.data.models.LoginData;
import com.project.springjavafx.javaFXApp.exceptions.LoginformException;
import com.project.springjavafx.javaFXApp.utility.Credentials;
import com.project.springjavafx.javaFXApp.utility.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginController {

    // TextField for the username input field in the login form
    @FXML
    public TextField loginformusername;

    // PasswordField for the password input field in the login form
    @FXML
    public PasswordField loginformpassword;


    /**
     * Handles the event to show the login page.
     * Loads a new FXML scene called "loginformFXML".
     */
    @FXML
    public void onShowLoginPageCardClick(MouseEvent mouseEvent) {
        try {
            SceneLoader.showScene(mouseEvent, "loginformFXML");
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Log error if the FXML file cannot be loaded
        }
    }

    /**
     * Handles the login logic by checking entered credentials against a hardcoded list.
     * If credentials match, proceeds to the next scene.
     */
    @FXML
    public void onCheckDataToLoginButtonClick(MouseEvent mouseEvent) throws SQLException {
        // Mock data for login verification (replace with real database in production)
        EmployeeDAO employeeDAO = new EmployeeDAO();
        ArrayList<LoginData> logindatalist =  employeeDAO.getLoginDataList();

        // Extract entered username and password
        String username = loginformusername.getText().trim();
        String password = loginformpassword.getText().trim();

        boolean isLoginSuccess = false;

        try {
            isLoginSuccess = Credentials.check(logindatalist, username, password);
        } catch (LoginformException e) {

            System.out.println(e.getMessage());

        }


        if (isLoginSuccess) {
            System.out.println("Login success!\nUser: " + AfterLoginDTO.employeeId + " Manager: " + AfterLoginDTO.isManager);
            try {


                Thread.sleep(2000);

                SceneLoader.showScene(mouseEvent, "attendanceFXML");

            } catch (IOException | InterruptedException e) {

                System.out.println(e.getMessage());

            }
        } else {
            System.out.println("Login failed");

        }
    }

    /**
     * Handles the "Forgot Password" button click event.
     * Currently just logs a message to the console.
     */
    @FXML
    public void onForgotPasswordActionLabelClick(MouseEvent mouseEvent) {
        System.out.println("Better luck next time"); // Placeholder for future functionality
    }

}