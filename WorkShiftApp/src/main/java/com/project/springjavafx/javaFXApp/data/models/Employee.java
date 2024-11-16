package com.project.springjavafx.javaFXApp.data.models;

import lombok.Getter;
import lombok.Setter;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public int getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(int hourlyWage) {
        this.hourlyWage = hourlyWage;
    }



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

    public boolean isManager() {
            return position.equalsIgnoreCase("Manager") || position.equalsIgnoreCase("Boss");
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", position='" + position + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", hireDate=" + hireDate +
                ", hourlyWage=" + hourlyWage +
                '}';
    }
}