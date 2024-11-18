package com.project.springjavafx.javaFXApp.data.models;


public class LoginData {

    private long loginID;
    private String username;
    private String password;
    private int employeeID;

    public long getLoginID() {
        return loginID;
    }

    public void setLoginID(long loginID) {
        this.loginID = loginID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public LoginData(long loginID, String username, String password, int employeeID) {
        this.loginID = loginID;
        this.username = username;
        this.password = password;
        this.employeeID = employeeID;
    }

}
