package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.EmployeeDAO;
import com.project.springjavafx.javaFXApp.data.dto.AfterLoginDTO;
import com.project.springjavafx.javaFXApp.data.models.LoginData;
import com.project.springjavafx.javaFXApp.exceptions.LoginformException;
import com.project.springjavafx.javaFXApp.utility.Credentials;
import com.project.springjavafx.javaFXApp.utility.SceneLoader;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class LoginController{

    // TextField for the username input field in the login form

    @FXML
    public AnchorPane loginAncorPane;
    @FXML
    public TextField loginformusername;

    // PasswordField for the password input field in the login form
    @FXML
    public PasswordField loginformpassword;

    @FXML
    public Rectangle designRectangle;

    @FXML
    public Rectangle designRectangle2;

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


                /*loginformusername.setStyle("-fx-border-color: green");
                loginformpassword.setStyle("-fx-border-color: green");*/

                //set the rectangle green
                designRectangle2.setStyle("-fx-fill: #247D01");
                designRectangle.setStyle("-fx-fill: #247D01");


                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(event -> {

                    try {
                        SceneLoader.showScene(mouseEvent, "attendanceFXML");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                });


                pause.play();

        } else {
            System.out.println("Login failed");

            /*loginformusername.setStyle("-fx-border-color: red");
            loginformpassword.setStyle("-fx-border-color: red");*/

            //set the rectangle red
            designRectangle2.setStyle("-fx-fill: #C00809");
            designRectangle.setStyle("-fx-fill: #C00809");

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