package com.project.springjavafx.javaFXApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class javaFXMain extends Application {

   public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(javaFXMain.class.getResource("/fxml/mainFXML.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
   }
}
