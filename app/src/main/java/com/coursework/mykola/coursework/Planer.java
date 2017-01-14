package com.coursework.mykola.coursework;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kolas on 11.09.16.
 */
public class Planer {

    public static final String T = "t";
    public static final String SEND = "Передача";
    public static final String P = "P";


     private int COUNT;


     int columnNumber;
     int numberOfTasks;

     Proc[] allProc;

     ArrayList<Task> computeTasks = new ArrayList<Task>();
     ArrayList<Task> endedTasks = new ArrayList<Task>();

     ArrayList<Link> links;

    public int getColumnNumber() {
        return columnNumber;
    }
    public Planer(int[][] matrix, int[] tasksWeights, int processorCount) {

        COUNT = processorCount;
        columnNumber = COUNT + 2;

        numberOfTasks = matrix.length;

        setSystemParametres(matrix, tasksWeights);
        allProc = new Proc[COUNT];
        for (int i = 0; i < COUNT; i++) {
            Proc Proc = new Proc(false, false);
            allProc[i] = Proc;
        }
    }
     ArrayList<Task> prepeatTasks = new ArrayList<Task>();
     HashMap<Integer, Task> tasks;

    public ArrayList<String> modeling() {
        ArrayList<String> resultList = new ArrayList<String>();
        int takt = 0;
        
        String tmpStr = "";
       
        resultList.add(T);
        for (int i = 1; i <= COUNT; i++) {
            tmpStr = P + i;
            resultList.add(tmpStr);
        }
        
        resultList.add(SEND);

        while (endedTasks.size() != numberOfTasks) {
            takt++;
            for (int i = 0; i < allProc.length; i++) {
                if ((allProc[i].isFree()) && (allProc[i].getExecuteTask().gettimeToExecuted() == 0)) {
                    allProc[i].setFree(false);
                    allProc[i].addExecutedTasks();


                    endedTasks.add(allProc[i].getExecuteTask());
                    computeTasks.remove(allProc[i].getExecuteTask());
                    for (int j = 0; j < numberOfTasks; j++) {
                        if ((!endedTasks.contains(tasks.get(j))) && (!computeTasks.contains(tasks.get(j)))) {
                            if (tasks.get(j).getfatherTasksBefore().contains(allProc[i].getExecuteTask())) {
                                tasks.get(j).getfatherTasksBefore().remove(allProc[i].getExecuteTask());
                            }
                            if ((tasks.get(j).getfatherTasksBefore().size() == 0) && (!prepeatTasks.contains(tasks.get(j)))) {
                                prepeatTasks.add(tasks.get(j));
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < COUNT; i++) {
                if (!allProc[i].isFree()) {
                    if (prepeatTasks.size() != 0) {
                        for (int j = 0; j < prepeatTasks.size() - 1; j++) {
                            for (int k = +1; k < j; k++) {
                                if (prepeatTasks.get(k).gettimeExecut() > prepeatTasks.get(j).gettimeExecut()) {
                                    prepeatTasks.add(j, prepeatTasks.get(k));
                                    prepeatTasks.remove(k + i);
                                }
                            }
                        }
                        Task taskForExecute = null;
                        for (int j = 0; j < prepeatTasks.size(); j++) {
                            if (allProc[i].pretends(prepeatTasks.get(j), computeTasks)) {
                                if (taskForExecute != null) {
                                    if (allProc[i].getExecuteTask() != null) {
                                        if (getConnectionWeight(prepeatTasks.get(j), allProc[i].getExecuteTask()) > getConnectionWeight(taskForExecute, allProc[i].getExecuteTask())) {
                                            taskForExecute = prepeatTasks.get(j);
                                        }
                                    }
                                } else {
                                    taskForExecute = prepeatTasks.get(j);
                                }
                            }
                        }

                        if (taskForExecute != null) {
                            allProc[i].setExecuteTask(taskForExecute);

                            computeTasks.add(taskForExecute);
                            prepeatTasks.remove(taskForExecute);
                        } else {
                            allProc[i].setExecuteTask(prepeatTasks.get(0));
                            computeTasks.add(prepeatTasks.get(0));
                            prepeatTasks.remove(0);
                        }
                        allProc[i].setFree(true);
                        setSendTimeOnProcessor(allProc[i]);

                    }
                }
            }

            for (int i = 0; i < allProc.length; i++) {
                if (allProc[i].getSendTime() == 0)
                    if (allProc[i].isFree())
                        allProc[i].getExecuteTask().execute();
                    else
                        allProc[i].sendTimeDec();

            }

            tmpStr = String.valueOf(takt);
            resultList.add(tmpStr);
            for (int i = 0; i < COUNT; i++) {
                Task aTask = null;
                if (allProc[i].isFree()) {
                    aTask = allProc[i].getExecuteTask();
                }
                if (aTask != null) {
                    tmpStr = String.valueOf(Model.nodes.get(allProc[i].getExecuteTask().getId()).getId());
                } else {
                    tmpStr = "-";
                }

                resultList.add(tmpStr);
            }

            tmpStr = "";
            for (Proc p : allProc) {
                if (p.getSendTime() != 0&&p.getExecuteTask()!=null) {
                    tmpStr += links.get(p.getFromTask()).getFromTask().getId() + " -> " ;
                          links.get(p.getExecuteTask().getId()).getToTask().getId();

                }
            }
            resultList.add(tmpStr);
            tmpStr = "";

        }

        return resultList;
    }
    boolean isTopTask(int[][] matrix, Task task) {
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][task.getId()] != 0) {
                return false;
            }
        }
        return true;
    }

     void setSystemParametres(int[][] matrix, int[] tasksWeights) {
        tasks = new HashMap<Integer, Task>();
        for (int i = 0; i < numberOfTasks; i++) {
            Task Task = new Task(i, tasksWeights[i]);
            tasks.put(i, Task);
        }

        for (int i = 0; i < numberOfTasks; i++) {
            ArrayList<Task> dependentTasks = new ArrayList<Task>();
            for (int j = 0; j < numberOfTasks; j++) {
                if (matrix[i][j] != 0) {
                    dependentTasks.add(tasks.get(j));
                }
            }
            tasks.get(i).setDependentTasks(dependentTasks);
        }
        for (int i = 0; i < numberOfTasks; i++) {
            ArrayList<Task> fatherTasks = new ArrayList<Task>();
            for (int j = 0; j < numberOfTasks; j++) {
                if (matrix[j][i] != 0) {
                    fatherTasks.add(tasks.get(j));
                }
            }
            tasks.get(i).setFatherTasks(fatherTasks);
        }
        links = new ArrayList<Link>();
        for (int i = 0; i < numberOfTasks; i++) {
            for (int j = 0; j < numberOfTasks; j++) {
                if (matrix[i][j] != 0) {
                    Link Link = new Link(tasks.get(i), tasks.get(j), matrix[i][j]);
                    links.add(Link);
                }
            }

        }
        for (int i = 0; i < numberOfTasks; i++) {
            if (isTopTask(matrix, tasks.get(i))) {
                prepeatTasks.add(tasks.get(i));
            }
        }
    }


     int getConnectionWeight(Task task1, Task task2) {
        for (int i = 0; i < links.size(); i++) {
            if ((links.get(i).getFromTask().equals(task1)) && (links.get(i).getToTask().equals(task2))) {
                return links.get(i).getWeight();
            }
            if ((links.get(i).getFromTask().equals(task2)) && (links.get(i).getToTask().equals(task1))) {
                return links.get(i).getWeight();
            }
        }
        return -1;
    }


    public void setSendTimeOnProcessor(Proc proc) {
        int numberProc = -1;
        Task from = null;
        for (int i = 0; i < allProc.length; i++) {
            for (Task task : allProc[i].getExecutedTasks()) {
                if (proc.getExecuteTask().getFatherTasks().contains(task)) {
                    numberProc = i;
                    from = task;
                    break;
                }
            }
            if (numberProc != -1)
                break;
        }
        if (numberProc != -1)
            if (proc != allProc[numberProc]) {
                proc.setSendTime(getConnectionWeight(from, proc.getExecuteTask()));
                proc.setFromTask(from.getId());
            }


    }
}
