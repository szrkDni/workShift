package com.project.springjavafx.javaFXApp.controller;

import com.project.springjavafx.javaFXApp.data.dao.EmployeeDAO;
import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.springApp.model.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class loginController {

    private Stage stage;
    private Scene scene;
    private Parent parent;

    public void onShowLoginPageCardClick(MouseEvent mouseEvent) throws Exception {
        parent = FXMLLoader.load(getClass().getResource("/fxml/mainFXML.fxml"));
        scene = new Scene(parent);
        stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    public void handleTest_bttn(ActionEvent actionEvent) {
            try {
                // Kapcsolódás az adatbázishoz
                Connection connection = DatabaseConnector.connect();

                if (connection != null) {
                    EmployeeDAO employeeDAO = new EmployeeDAO(connection);

                    // Példa: Összes alkalmazott lekérése
                    employeeDAO.fetchAllEmployees();

                    // Példa: Új alkalmazott hozzáadása
                    employeeDAO.addEmployee("John", "Doe", "Developer", "john.doe@example.com", "1990-01-01", "2023-01-01", 2000);

                    // Példa: Alkalmazott órabérének frissítése
                    employeeDAO.updateEmployeeHourlyWage(2, 2500);

                    // Példa: Alkalmazott törlése
                    employeeDAO.deleteEmployee(2);

                    System.out.println("Operation completed successfully.");
                } else {
                    System.out.println("Failed to connect to the database.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}

