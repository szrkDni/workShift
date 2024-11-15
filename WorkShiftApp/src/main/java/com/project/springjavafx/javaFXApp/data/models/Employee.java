package com.project.springjavafx.javaFXApp.data.models;

import java.sql.Date;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String position;
    private String email;
    private Date birthDate;
    private Date hireDate;
    private int hourlyWage;

    // Constructor
    public Employee(int id, String firstName, String lastName, String position, String email, Date birthDate, Date hireDate, int hourlyWage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.email = email;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.hourlyWage = hourlyWage;
    }

    // Getters and Setters
    // ... Add getter and setter methods here ...
}
