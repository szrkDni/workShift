package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.EmployeeDAO;
import com.project.springjavafx.javaFXApp.data.dto.AfterLoginDTO;
import com.project.springjavafx.javaFXApp.data.models.Employee;
import com.project.springjavafx.javaFXApp.utility.ButtoneventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.aspectj.lang.annotation.After;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final ButtoneventHandler buttoneventHandler = new ButtoneventHandler();

    protected EmployeeDAO employeeDAO = new EmployeeDAO();
    protected Employee employee = employeeDAO.getEmployeeById(AfterLoginDTO.employeeId);



    @FXML
    AnchorPane controlAnchorPane;

    @FXML
    Label nameLabel;

    @FXML
    Label positionLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controlAnchorPane.setVisible(AfterLoginDTO.isManager);
        nameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
        positionLabel.setText(employee.getPosition());
    }

    @FXML
    public void onlogoutButtonClick(MouseEvent mouseEvent)
    {
        System.out.println("Logout button clicked");
        buttoneventHandler.logout(mouseEvent);
    }

    @FXML
    public void onprofileButtonClick(MouseEvent mouseEvent)
    {
        System.out.println("Profile button clicked");
        buttoneventHandler.profile(mouseEvent);
    }

    @FXML
    public void onattendanceButtonClick(MouseEvent mouseEvent)
    {
        System.out.println("Attendance button clicked");
        buttoneventHandler.attendance(mouseEvent);
    }

    @FXML
    public void onpaymentButtonClick(MouseEvent mouseEvent)
    {
        System.out.println("Payment button clicked");
        buttoneventHandler.payment(mouseEvent);
    }

    @FXML
    public void ontimeoffButtonClick(MouseEvent mouseEvent)
    {
        System.out.println("Time Off button clicked");
        buttoneventHandler.timeoff(mouseEvent);
    }

    @FXML
    public void oncontrolButtonClick(MouseEvent mouseEvent)
    {
        System.out.println("Control button clicked");
        buttoneventHandler.control(mouseEvent);
    }


}
