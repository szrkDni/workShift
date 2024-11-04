package com.project.springjavafx.springApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue
    long id;
    String name;
    String description;
    Date startDate;
    Date endDate;
    int projectManagerId;


    //Valami hibát jelez
    //ArrayList<Integer> projectWorkersId;

}
