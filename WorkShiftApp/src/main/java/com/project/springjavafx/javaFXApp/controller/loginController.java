package com.project.springjavafx.javaFXApp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

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
}
