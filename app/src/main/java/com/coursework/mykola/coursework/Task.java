package com.coursework.mykola.coursework;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: hadgehog
 * Date: 03.01.13
 * Time: 12:20
 * To change this template use File | Settings | File Templates.
 */
public class Task {
    private int id; //ідентифікатор задачі
    private int duration;//тривалість (тактів)
    private int remainder;//залишок виконання(тактів)
    private ArrayList<Task> dependentTasks;//залежні задачі(дочірні)
    private ArrayList<Task> fatherTasks;  // задачі, від яких залежить дана задача
    private ArrayList<Task> fatherTasksToDo;  // задачі, які треба виконати для початку виконання цієї задачі
    private boolean isPretended = false; // для задачі є ресурс для виконання (перетендує забрати дан задачу)

    public Task(int id, int duration) {
        this.id = id;
        this.duration = duration;
        this.remainder = duration;
    }

    public int getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public int getRemainder() {
        return remainder;
    }

    public void setDependentTasks(ArrayList<Task> dependentTasks) {
        this.dependentTasks = dependentTasks;
    }

    public ArrayList<Task> getDependentTasks() {
        return dependentTasks;
    }

    public ArrayList<Task> getFatherTasksToDo() {
        return fatherTasksToDo;
    }

    public void setFatherTasks(ArrayList<Task> fatherTasks) {
        this.fatherTasks = fatherTasks;
        this.fatherTasksToDo = (ArrayList<Task>) fatherTasks.clone();
    }

    public boolean isPretended() {
        return isPretended;
    }

    public void setPretended(boolean pretended) {
        isPretended = pretended;
    }

    public void execute() {
        remainder--;
    }
}
