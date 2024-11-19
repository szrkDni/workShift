package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dto.AfterLoginDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    @FXML public TextField loggedinas;
    @FXML public TextField thisposition;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedinas.setText(String.valueOf(AfterLoginDTO.employeeId));
        thisposition.setText(String.valueOf(AfterLoginDTO.isManager));
    }
}
