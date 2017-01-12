package com.coursework.mykola.coursework;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hadgehog
 * Date: 03.01.13
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class Planer {
    private final int PROCESSOR_COUNT = 1;
    private int marker = 0;                                        //де знаходиться маркер
    private AProcessor[] aProcessors;                               //процесори
    private Map<Integer, Task> tasks;                              //індекс<->задача
    private boolean isBusFree = true;
    private ArrayList<AConnection> aConnections;
    private ArrayList<Task> readyTasks = new ArrayList<Task>();  //готові до виконання задачі
    private ArrayList<Task> executedTasks = new ArrayList<Task>();// задачі, що виконуються
    private int numberOfTasks;                                      //кількість задач
    private ArrayList<Task> doneTasks = new ArrayList<Task>();   //виконані задачі

    public Planer(int[][] graphMatrix, int[] tasksWeights) {
        numberOfTasks = graphMatrix.length;
        setSystemParametres(graphMatrix, tasksWeights);
        //створення процесорів
        aProcessors = new AProcessor[PROCESSOR_COUNT];
        for (int i = 0; i < PROCESSOR_COUNT; i++) {
            AProcessor AProcessor = new AProcessor(false, false);
            aProcessors[i] = AProcessor;
        }
    }

    //запуск моделюванння
    public void run() {
        marker = 0;//маркер у процесора №0
        int takt = 0;
        marker = 0;
        aProcessors[0].setMarkered(true);
        ArrayList<String> modelingResult = new ArrayList<String>();
        String taktLine = "";
        String split = ";";
        taktLine += "t" + split + "         ";
        for (int i = 1; i <= PROCESSOR_COUNT; i++) {
            taktLine += "P" + i + split + "     ";
        }
        taktLine += "      Bus";
        System.out.println(taktLine);
        modelingResult.add(taktLine);
        while (doneTasks.size() != numberOfTasks) {
            takt++;
            //перевіряємо чи виконали процесори свої задачі і чи не з'явились вільні задачі
            for (int i = 0; i < aProcessors.length; i++) {
                if ((aProcessors[i].isBusy()) && (aProcessors[i].getExecuteTask().getRemainder() == 0)) {
                    aProcessors[i].setBusy(false);
                    aProcessors[i].addExecutedTasks();
                    doneTasks.add(aProcessors[i].getExecuteTask());
                    executedTasks.remove(aProcessors[i].getExecuteTask());
                    //чи зявились вільні задачі
                    for (int j = 0; j < numberOfTasks; j++) {
                        if ((!doneTasks.contains(tasks.get(j))) && (!executedTasks.contains(tasks.get(j)))) {
                            if (tasks.get(j).getFatherTasksToDo().contains(aProcessors[i].getExecuteTask())) {
                                tasks.get(j).getFatherTasksToDo().remove(aProcessors[i].getExecuteTask());
                            }
                            if ((tasks.get(j).getFatherTasksToDo().size() == 0) && (!readyTasks.contains(tasks.get(j)))) {
                                readyTasks.add(tasks.get(j));
                            }
                        }
                    }
                }
            }
            //призначаємо вільним процесорам готові задачі (циклічно проходимо всі процесори, поки не буде конфліктів по призначенню задач)
            for (int i = 0; i < PROCESSOR_COUNT; i++) {
                if (!aProcessors[i].isBusy()) {
                    if (readyTasks.size() != 0) {
                        //сортуємо готові задачі за тривлістю по спаданню
                        for (int j = 0; j < readyTasks.size() - 1; j++) {
                            for (int k = +1; k < j; k++) {
                                if (readyTasks.get(k).getDuration() > readyTasks.get(j).getDuration()) {
                                    readyTasks.add(j, readyTasks.get(k));
                                    readyTasks.remove(k + i);
                                }
                            }
                        }
                        //вибираємо задачу для даного процесора
                        Task taskForExecute = null;
                        for (int j = 0; j < readyTasks.size(); j++) {
                            if (aProcessors[i].pretends(readyTasks.get(j), executedTasks)) {
                                if (taskForExecute != null) {
                                    //вибираємо задачу з більшим ваговим коефіціентом передачі
                                    if (aProcessors[i].getExecuteTask() != null) {
                                        if (getConnectionWeight(readyTasks.get(j), aProcessors[i].getExecuteTask()) > getConnectionWeight(taskForExecute, aProcessors[i].getExecuteTask())) {
                                            taskForExecute = readyTasks.get(j);
                                        }
                                    }
                                } else {
                                    taskForExecute = readyTasks.get(j);
                                }
                            }
                        }
                        //при можливості передаємо дані
                        if (aProcessors[i].isMarkered() & (!isBusFree)) {
                            isBusFree = false;
                        }
                        if (taskForExecute != null) {
                            aProcessors[i].setExecuteTask(taskForExecute);

                            executedTasks.add(taskForExecute);
                            readyTasks.remove(taskForExecute);
                        } else {//вибираємо задач з найбільшою тривалістю виконання
                            aProcessors[i].setExecuteTask(readyTasks.get(0));
                            executedTasks.add(readyTasks.get(0));
                            readyTasks.remove(0);
                        }
                        aProcessors[i].setBusy(true);
                    }
                }
            }
            //виконуємо один такт всіх процесорів
            for (int i = 0; i < aProcessors.length; i++) {
                if (aProcessors[i].isBusy()) {
                    aProcessors[i].getExecuteTask().execute();
                }
            }
            //шукаємо промаркований процесор і визначаємо чи можна передати маркер далі
            for (int i = 0; i < PROCESSOR_COUNT; i++) {
                if ((aProcessors[i].isMarkered()) & (isBusFree)) {
                    aProcessors[i].setMarkered(false);
                    if (i != PROCESSOR_COUNT - 1) {
                        aProcessors[i + 1].setMarkered(true);
                        marker = i + 1;
                        break;
                    } else {
                        aProcessors[0].setMarkered(true);
                        marker = 0;
                        break;
                    }
                }
            }
            //виводимо результат роботи такту в колнсоль
            taktLine = takt + split + "         ";
            for (int i = 0; i < PROCESSOR_COUNT; i++) {
                Task aTask = null;
                if (aProcessors[i].isBusy()) {
                    aTask = aProcessors[i].getExecuteTask();
                }
                if (aTask != null) {
                    taktLine += (aProcessors[i].getExecuteTask().getId() + 1);
                } else {
                    taktLine += "-";
                }
                //з маркером?
                if (aProcessors[i].isMarkered()) {
                    taktLine += "*" + split + "      ";
                } else {
                    taktLine += split + "      ";
                }
            }
            //що на шині
            if (!isBusFree) {
                taktLine += aConnections.get(marker).getFromTask().getId() + " -> " +
                        aConnections.get(marker).getToTask().getId() + split;
            }
            System.out.println(taktLine);
            modelingResult.add(taktLine);
        }
        //запис в csv файл
        writeToFile(modelingResult);
    }

    //встановлює параметри системи на основі вхідної матриці переходів та вагів задач
    private void setSystemParametres(int[][] graphMatrix, int[] tasksWeights) {
        //створюємо задачі
        tasks = new HashMap<Integer, Task>();
        for (int i = 0; i < numberOfTasks; i++) {
            Task Task = new Task(i, tasksWeights[i]);
            tasks.put(i, Task);
        }
        //встановлення дочірніх задач
        for (int i = 0; i < numberOfTasks; i++) {
            ArrayList<Task> dependentTasks = new ArrayList<Task>();
            for (int j = 0; j < numberOfTasks; j++) {
                if (graphMatrix[i][j] != 0) {
                    dependentTasks.add(tasks.get(j));
                }
            }
            tasks.get(i).setDependentTasks(dependentTasks);
        }
        //встановлення батьківських задач
        for (int i = 0; i < numberOfTasks; i++) {
            ArrayList<Task> fatherTasks = new ArrayList<Task>();
            for (int j = 0; j < numberOfTasks; j++) {
                if (graphMatrix[j][i] != 0) {
                    fatherTasks.add(tasks.get(j));
                }
            }
            tasks.get(i).setFatherTasks(fatherTasks);
        }
        //створюємо зв`язки
        aConnections = new ArrayList<AConnection>();
        for (int i = 0; i < numberOfTasks; i++) {
            for (int j = 0; j < numberOfTasks; j++) {
                if (graphMatrix[i][j] != 0) {
                    AConnection AConnection = new AConnection(tasks.get(i), tasks.get(j), graphMatrix[i][j]);
                    aConnections.add(AConnection);
                }
            }

        }
        //визначення задач, готових до виконання( задачі верхнього рівня)
        for (int i = 0; i < numberOfTasks; i++) {
            if (isTopTask(graphMatrix, tasks.get(i))) {
                readyTasks.add(tasks.get(i));
            }
        }
    }

    // визначає, чи на верхньому рівні знаходиться задача
    private boolean isTopTask(int[][] graphMatrix, Task task) {
        for (int i = 0; i < graphMatrix.length; i++) {
            if (graphMatrix[i][task.getId()] != 0) {
                return false;
            }
        }
        return true;
    }

    // повертає значення ваги переходу між задачами
    private int getConnectionWeight(Task task1, Task task2) {
        for (int i = 0; i < aConnections.size(); i++) {
            if ((aConnections.get(i).getFromTask().equals(task1)) && (aConnections.get(i).getToTask().equals(task2))) {
                return aConnections.get(i).getWeight();
            }
            if ((aConnections.get(i).getFromTask().equals(task2)) && (aConnections.get(i).getToTask().equals(task1))) {
                return aConnections.get(i).getWeight();
            }
        }
        return -1;
    }

    //записує результат моделювання в файл формату csv
    private void writeToFile(ArrayList<String> modelingResult) {
        String fileName = "result.csv";
        try {
            BufferedWriter newCSVFile = new BufferedWriter(new FileWriter(fileName));
            for (String s : modelingResult) {
                newCSVFile.write(s + "\n");
            }
            newCSVFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
