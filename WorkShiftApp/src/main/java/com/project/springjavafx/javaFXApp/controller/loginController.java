package com.project.springjavafx.javaFXApp.controller;

import com.mysql.cj.log.Log;
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
import java.sql.SQLOutput;
import java.util.ArrayList;

public class loginController {

    @FXML public TextField loginformusername;
    @FXML public PasswordField loginformpassword;

    public void onShowLoginPageCardClick(MouseEvent mouseEvent) {

        try {

            showParenthScene(mouseEvent, "loginformFXML");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void oncheckdatatologin(MouseEvent mouseEvent) throws LoginformException, IOException {
        ArrayList<LoginData> logindatalist = new ArrayList<LoginData>(){
            {
                add(new LoginData(1,"admin","admin",1));
                add(new LoginData(2,"belatheking","12345678",2));
                add(new LoginData(3,"admin","admin",3));
                add(new LoginData(4,"admin","admin",4));
            }
        };


        String username = loginformusername.getText();
        String password = loginformpassword.getText();

        System.out.println(username + " " + password);

        for (int i = 0; i < logindatalist.size(); i++) {

            //login success
            if (logindatalist.get(i).getUsername().equals(username) &&
                    logindatalist.get(i).getPassword().equals(password)) {

                EmployeeDAO employeeDAO = new EmployeeDAO();
                Employee loggedinemployee = employeeDAO.getEmployeeById(logindatalist.get(i).getEmployeeID());

                AfterLoginDTO.employeeId = logindatalist.get(i).getEmployeeID();
                AfterLoginDTO.isManager = loggedinemployee.isManager();

                showParenthScene(mouseEvent,"nextpageFXML");
            }
        }




    }

    private void showParenthScene(MouseEvent mouseEvent, String fxmlName ) throws IOException
    {

        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlName + ".fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
}
