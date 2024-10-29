package com.example.springjavafx.javaFXApp.Controller;

import com.example.springjavafx.springApp.SpringManager;
import javafx.event.ActionEvent;

public class mainController {
    public void onStartDatabaseButtonClick(ActionEvent actionEvent) {

        System.out.println("Adatbázis indítása!");
        SpringManager.startSpringApp();

    }

    public void onStopDataBaseButtonClick(ActionEvent actionEvent) {

        System.out.println("Adatbázis leállítása");
        SpringManager.stopSpringApp();
    }

    public void onListDatabaseContentButtonClick(ActionEvent actionEvent) {

        System.out.println("Adatbázis tartalmának listázása!");
        SpringManager.getSpringApp().printAll();
    //bele írtam valamit Nandi
    }

    public void onAddTestPersonButtonClick(ActionEvent actionEvent) {

        System.out.println("Teszt személy hozzáadása!");
        SpringManager.getSpringApp().addTestPerson();

    }
}
