package com.project.springjavafx.springApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkShifts {
    @Id
    @GeneratedValue
    long id;
    int employeeId;
    Date workDay;
    double workHours;
    String shiftType;
}
