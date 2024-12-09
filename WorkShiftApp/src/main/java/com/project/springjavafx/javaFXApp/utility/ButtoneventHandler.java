package com.project.springjavafx.javaFXApp.utility;

import com.project.springjavafx.javaFXApp.controller.ProjectController;
import com.project.springjavafx.javaFXApp.data.dto.AfterLoginDTO;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ButtoneventHandler {

    public ButtoneventHandler() {
    }

    public void logout(MouseEvent mouseEvent) {

        try{
            AfterLoginDTO.clearData();
            System.out.println("Successfully logged out");
            SceneLoader.showScene(mouseEvent,"loginFXML");

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void profile(MouseEvent mouseEvent) {

        try{

            SceneLoader.showScene(mouseEvent,"profileFXML");

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void attendance(MouseEvent mouseEvent) {

        try{

            SceneLoader.showScene(mouseEvent,"attendanceFXML");

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void timeoff(MouseEvent mouseEvent) {

        try{

            SceneLoader.showScene(mouseEvent,"timeoffFXML");

        }catch(IOException e){
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }

    public void project(MouseEvent mouseEvent) {

        try{
            SceneLoader.showScene(mouseEvent,"projectFXML");
        }catch(IOException e){
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }

    public void control(MouseEvent mouseEvent) {

        try{

            SceneLoader.showScene(mouseEvent,"controlFXML");

        }catch(IOException e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
        }
    }


}
