package com.project.springjavafx.javaFXApp.data.dto;


public class AfterLoginDTO {
    public static boolean isManager;
    public static int employeeId;

    private AfterLoginDTO() {}

    public static void clearData()
    {
        isManager = false;
        employeeId = 0;
    }

}
