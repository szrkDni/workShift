package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.EmployeeDAO;
import com.project.springjavafx.javaFXApp.data.dto.AfterLoginDTO;
import com.project.springjavafx.javaFXApp.data.models.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
