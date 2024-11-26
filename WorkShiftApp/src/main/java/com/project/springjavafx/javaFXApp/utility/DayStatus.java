package com.project.springjavafx.javaFXApp.utility;

import com.project.springjavafx.javaFXApp.data.models.LeaveRequest;
import com.project.springjavafx.javaFXApp.data.models.WorkShift;

import java.util.List;

public class DayStatus {
    public int NumOfNormalHours;
    public int NumOfExcessHours;
    public int NumOfOffHours;
    public int NumOfSickLeaveHours;
    public int NumOfSicknessPensionHours;

    public DayStatus(List<WorkShift> workshift, List<LeaveRequest> leaverequest) {
        NumOfOffHours = leaverequest.stream().filter((x)-> x.getLeaveType().equalsIgnoreCase("Holiday")).toList().size()*8;
        NumOfSickLeaveHours = leaverequest.stream().filter((x)-> x.getLeaveType().equalsIgnoreCase("Sick Leave")).toList().size()*8;
        NumOfSicknessPensionHours = leaverequest.size()*8-(NumOfOffHours+NumOfSickLeaveHours);
        NumOfNormalHours = (int)workshift.stream().filter((x)-> x.getShiftType().equalsIgnoreCase("Normal")).mapToDouble(WorkShift::getWorkHour).sum();
        NumOfExcessHours = (int)workshift.stream().filter((x)-> x.getShiftType().equalsIgnoreCase("Extra")).mapToDouble(WorkShift::getWorkHour).sum();

    }
}

