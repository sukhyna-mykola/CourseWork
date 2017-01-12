package com.coursework.mykola.coursework;

import java.util.ArrayList;

/**
 * Created by kolas on 11.09.16.
 */
public class Proc {

     Task executeTask;

     int sendTime;
     int fromTask;

    public int getFromTask() {
        return fromTask;
    }

    public void setFromTask(int fromTask) {
        this.fromTask = fromTask;
    }

    public int getSendTime() {

        return sendTime;
    }

    public void setSendTime(int sendTime) {
        this.sendTime = sendTime;
    }

     ArrayList<Task> executedTasks = new ArrayList<Task>();

    public Proc(boolean Free, boolean markered) {
        isFree = Free;
    }

    public boolean isFree() {
        return isFree;
    }
    boolean isFree;
    public void setFree(boolean Free) {
        isFree = Free;
    }


    public Task getExecuteTask() {
        return executeTask;
    }

    public void setExecuteTask(Task executeTask) {
        this.executeTask = executeTask;
    }

    public ArrayList<Task> getExecutedTasks() {
        return executedTasks;
    }

    public void addExecutedTasks() {
        this.executedTasks.add(this.executeTask);
    }

    public boolean pretends(Task task, ArrayList<Task> allExecutedTasks) {
        for (int i = 0; i < executedTasks.size(); i++) {
            ArrayList<Task> taskArrayList = executedTasks.get(i).getDependentTasks();
            for (int j = 0; j < allExecutedTasks.size(); j++) {
                if (taskArrayList.contains(allExecutedTasks.get(j))) {
                    taskArrayList.remove(allExecutedTasks.get(j));
                }
            }
            if (taskArrayList.contains(task)) {
                return true;
            }
        }
        return false;
    }

    public  void sendTimeDec(){
        --sendTime;
    }
}
