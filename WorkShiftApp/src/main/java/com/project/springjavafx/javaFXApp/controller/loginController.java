package com.project.springjavafx.javaFXApp.controller;

import com.mysql.cj.log.Log;
import com.project.springjavafx.javaFXApp.data.dao.EmployeeDAO;
import com.project.springjavafx.javaFXApp.data.dto.AfterLoginDTO;
import com.project.springjavafx.javaFXApp.data.models.Employee;
import com.project.springjavafx.javaFXApp.data.models.LoginData;
import com.project.springjavafx.javaFXApp.exceptions.LoginformException;
import com.project.springjavafx.javaFXApp.utility.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.boot.context.FileEncodingApplicationListener;

import java.io.IOException;
import java.util.ArrayList;

public class loginController {

    // TextField for the username input field in the login form
    @FXML public TextField loginformusername;

    // PasswordField for the password input field in the login form
    @FXML public PasswordField loginformpassword;

    /**
     * Handles the event to show the login page.
     * Loads a new FXML scene called "loginformFXML".
     */
    public void onShowLoginPageCardClick(MouseEvent mouseEvent) {
        try {
            SceneLoader.showScene(mouseEvent, "loginformFXML");
        } catch (IOException e) {
            e.printStackTrace(); // Log error if the FXML file cannot be loaded
        }
    }

    /**
     * Handles the login logic by checking entered credentials against a hardcoded list.
     * If credentials match, proceeds to the next scene.
     */
    public void oncheckdatatologin(MouseEvent mouseEvent) {
        // Mock data for login verification (replace with real database in production)
        ArrayList<LoginData> logindatalist = new ArrayList<LoginData>() {
            {
                add(new LoginData(1, "admin", "admin", 1));
                add(new LoginData(2, "belatheking", "12345678", 2));
                add(new LoginData(3, "szsza", "jelszo", 3));
                add(new LoginData(4, "username", "password", 4));
            }
        };

        // Extract entered username and password
        String username = loginformusername.getText();
        String password = loginformpassword.getText();

        boolean isLoginSuccess = false;

        try{
            isLoginSuccess = checkCredentials(logindatalist, username, password);
        }
        catch(LoginformException e){

            System.out.println(e.getMessage());

        }


        if (isLoginSuccess)
        {
            try{

                SceneLoader.showScene(mouseEvent, "nextpageFXML");

            }
            catch(IOException e){

                System.out.println(e.getMessage());

            }
        }
    }

    /**
     * Handles the "Forgot Password" button click event.
     * Currently just logs a message to the console.
     */
    public void onforgotpasswordaction(MouseEvent mouseEvent) {
        System.out.println("Better luck next time"); // Placeholder for future functionality
    }




    private boolean checkCredentials(ArrayList<LoginData> logindatalist, String username, String password) throws LoginformException {

        // Loop through the list of users to validate the login credentials
        for (int i = 0; i < logindatalist.size(); i++) {
            // If credentials match
            if (logindatalist.get(i).getUsername().equals(username) &&
                    logindatalist.get(i).getPassword().equals(password)) {

                // Fetch the employee data for the logged-in user
                EmployeeDAO employeeDAO = new EmployeeDAO();
                Employee loggedinemployee = employeeDAO.getEmployeeById(logindatalist.get(i).getEmployeeID());

                // Store the logged-in employee's ID and role (manager or not) in a DTO
                AfterLoginDTO.employeeId = logindatalist.get(i).getEmployeeID();
                AfterLoginDTO.isManager = loggedinemployee.isManager();

                // Navigate to the next page after successful login

                return true; // Exit the method after successful login
            }

        }
        // If no match is found, no action is taken (could add an error message here)

        throw new LoginformException("User not found whit the given credenials");
    }
}
