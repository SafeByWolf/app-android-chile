package com.safebywolf.safebywolf.Model;

public class AlertGroup {
    String groupName;
    String alertGroupName;

    public AlertGroup() {
    }

    public AlertGroup(String groupName, String alertGroupName) {
        this.groupName = groupName;
        this.alertGroupName = alertGroupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAlertGroupName() {
        return alertGroupName;
    }

    public void setAlertGroupName(String alertGroupName) {
        this.alertGroupName = alertGroupName;
    }
}
