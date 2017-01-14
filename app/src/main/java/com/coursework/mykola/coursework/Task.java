package com.coursework.mykola.coursework;

import java.util.ArrayList;


/**
 * Created by kolas on 11.09.16.
 */
public class Task {

     int timeExecut;
     int timeToExecuted;
     ArrayList<Task> dependentTasks;
     ArrayList<Task> fatherTasks;  
     ArrayList<Task> fatherTasksBefore;
     int id;


    public int getId() {
        return id;
    }

    public int gettimeExecut() {
        return timeExecut;
    }

    public int gettimeToExecuted() {
        return timeToExecuted;
    }

    public void setDependentTasks(ArrayList<Task> dependentTasks) {
        this.dependentTasks = dependentTasks;
    }

    public ArrayList<Task> getDependentTasks() {
        return dependentTasks;
    }

    public ArrayList<Task> getfatherTasksBefore() {
        return fatherTasksBefore;
    }
    boolean hightPriority = false;

    public Task(int id, int timeExecut) {
        this.id = id;
        this.timeExecut = timeExecut;
        this.timeToExecuted = timeExecut;
    }

    public void setFatherTasks(ArrayList<Task> fatherTasks) {
        this.fatherTasks = fatherTasks;
        this.fatherTasksBefore = (ArrayList<Task>) fatherTasks.clone();
    }

    public boolean hightPriority() {
        return hightPriority;
    }
    public void execute() {
        timeToExecuted--;
    }
    public void setPretended(boolean pretended) {
        hightPriority = pretended;
    }

    public ArrayList<Task> getFatherTasks() {
        return fatherTasks;
    }


}
