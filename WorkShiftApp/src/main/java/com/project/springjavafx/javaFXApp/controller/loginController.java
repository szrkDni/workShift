package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.EmployeeDAO;
import com.project.springjavafx.javaFXApp.data.dto.AfterLoginDTO;
import com.project.springjavafx.javaFXApp.data.models.Employee;
import com.project.springjavafx.javaFXApp.data.models.LoginData;
import com.project.springjavafx.javaFXApp.exceptions.LoginformException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
            showParenthScene(mouseEvent, "loginformFXML");
        } catch (IOException e) {
            e.printStackTrace(); // Log error if the FXML file cannot be loaded
        }
    }

    /**
     * Handles the login logic by checking entered credentials against a hardcoded list.
     * If credentials match, proceeds to the next scene.
     */
    public void oncheckdatatologin(MouseEvent mouseEvent) throws LoginformException, IOException {
        // Mock data for login verification (replace with real database in production)
        ArrayList<LoginData> logindatalist = new ArrayList<LoginData>() {
            {
                add(new LoginData(1, "admin", "admin", 1));
                add(new LoginData(2, "belatheking", "12345678", 2));
                add(new LoginData(3, "admin", "admin", 3));
                add(new LoginData(4, "admin", "admin", 4));
            }
        };

        // Extract entered username and password
        String username = loginformusername.getText();
        String password = loginformpassword.getText();

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
                showParenthScene(mouseEvent, "nextpageFXML");
                return; // Exit the method after successful login
            }
        }
        // If no match is found, no action is taken (could add an error message here)
    }

    /**
     * Handles the "Forgot Password" button click event.
     * Currently just logs a message to the console.
     */
    public void onforgotpasswordaction(MouseEvent mouseEvent) {
        System.out.println("Better luck next time"); // Placeholder for future functionality
    }

    /**
     * Utility method to load and display a new scene based on the given FXML file name.
     *
     * @param mouseEvent The MouseEvent triggering the scene change.
     * @param fxmlName   The name of the FXML file to load (without extension).
     * @throws IOException If the FXML file cannot be found or loaded.
     */
    private void showParenthScene(MouseEvent mouseEvent, String fxmlName) throws IOException {
        // Load the FXML file and create a new Parent object
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlName + ".fxml"));

        // Create a new Scene using the loaded Parent
        Scene scene = new Scene(parent);

        // Get the current stage from the event and set the new scene
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show(); // Show the new scene
    }
}
