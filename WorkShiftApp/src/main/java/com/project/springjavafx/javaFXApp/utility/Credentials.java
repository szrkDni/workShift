package com.project.springjavafx.javaFXApp.utility;

import com.project.springjavafx.javaFXApp.data.dao.EmployeeDAO;
import com.project.springjavafx.javaFXApp.data.dto.AfterLoginDTO;
import com.project.springjavafx.javaFXApp.data.models.Employee;
import com.project.springjavafx.javaFXApp.data.models.LoginData;
import com.project.springjavafx.javaFXApp.exceptions.LoginformException;

import java.util.ArrayList;

public class Credentials {
    private Credentials() {
    }


    public static boolean check(ArrayList<LoginData> logindatalist, String username, String password) throws LoginformException {

        // Loop through the list of users to validate the login credentials
        for(LoginData loginData : logindatalist) {

            // If credentials match
            if (loginData.getUsername().equals(username) && loginData.getPassword().equals(password)) {

                // Fetch the employee data for the logged-in user
                EmployeeDAO employeeDAO = new EmployeeDAO();
                Employee loggedinemployee = employeeDAO.getEmployeeById(loginData.getEmployeeID());

                // Store the logged-in employee's ID and role (manager or not) in a DTO
                AfterLoginDTO.employeeId = loginData.getEmployeeID();
                AfterLoginDTO.isManager = loggedinemployee.isManager();

                // Navigate to the next page after successful login

                return true; // Exit the method after successful login
            }

        }

        // If no match is found
        throw new LoginformException("User not found whit the given credenials");
    }
}


