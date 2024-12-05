package com.project.springjavafx.javaFXApp.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {/**
     *
     *  3 types of leaves ->
     *      Holiday (Szabadság) (salary 100%)
     *      Sick Leave (Betegszabadság) (salary 70%)
     *      Sickness pension (Táppénz) (salary 60%)
     *
     *      if no more PTO and no more Sick Leave then its Sickness pension
     */

    private SceneLoader() {
    }

    /**
     * Utility method to load and display a new scene based on the given FXML file name.
     *
     * @param mouseEvent The MouseEvent triggering the scene change.
     * @param fxmlName   The name of the FXML file to load (without extension).
     * @throws IOException If the FXML file cannot be found or loaded.
     */
    public static void showScene(MouseEvent mouseEvent, String fxmlName) throws IOException {
        // Load the FXML file and create a new Parent object
        Parent parent = FXMLLoader.load(SceneLoader.class.getResource("/fxml/" + fxmlName + ".fxml"));

        // Create a new Scene using the loaded Parent
        Scene scene = new Scene(parent);

        // Get the current stage from the event and set the new scene
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(fxmlName);
        stage.show(); // Show the new scene
    }
}
