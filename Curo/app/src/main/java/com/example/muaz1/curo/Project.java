package com.example.muaz1.curo;

/**
 * Created by muaz1 on 2017/09/30.
 */



public class Project {
    private String projectID;
    private String name;

    private String description;
    private String startDate;
    private String endDate;
    private String numofSprints;
    private String currentSprint;
    private  String Master;

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNumofSprints() {
        return numofSprints;
    }

    public void setNumofSprints(String numofSprints) {
        this.numofSprints = numofSprints;
    }

    public String getCurrentSprint() {
        return currentSprint;
    }

    public void setCurrentSprint(String currentSprint) {
        this.currentSprint = currentSprint;
    }

    public String getMaster() {
        return Master;
    }

    public void setMaster(String master) {
        Master = master;
    }
}
