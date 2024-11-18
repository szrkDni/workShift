package com.project.springjavafx.javaFXApp;

import com.project.springjavafx.javaFXApp.data.dao.EmployeeDAO;
import com.project.springjavafx.javaFXApp.data.db.DatabaseConnector;
import com.project.springjavafx.springApp.SpringJavaFxApplication;
import com.project.springjavafx.springApp.model.Employee;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import java.sql.Connection;
import java.util.List;


public class javaFXMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(javaFXMain.class.getResource("/fxml/loginFXML.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
