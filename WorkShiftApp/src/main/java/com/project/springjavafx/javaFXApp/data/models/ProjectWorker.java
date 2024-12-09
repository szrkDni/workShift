package com.project.springjavafx.javaFXApp.data.models;

public class ProjectWorker {
    private final Integer projectId;
    private final String projectName;
    private final Integer workerId;
    private final String workerName;

    public ProjectWorker(Integer projectId, String projectName, Integer workerId, String workerName) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.workerId = workerId;
        this.workerName = workerName;
    }

    public Integer getProjectId() { return projectId; }
    public String getProjectWorkerName() { return projectName; }

    public Integer getWorkerId() { return workerId; }
    public String getWorkerName() { return workerName; }
}
