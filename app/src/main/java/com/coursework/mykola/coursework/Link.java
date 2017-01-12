package com.coursework.mykola.coursework;

/**
 * Created by kolas on 11.09.16.
 */
public class Link {
    private Task fromTask;
    private Task toTask;
    private int weight;

    public Link(Task fromTask, Task toTask, int weight) {
            this.fromTask = fromTask;
            this.toTask = toTask;
            this.weight = weight;
    }

    public Task getFromTask() {
        return fromTask;
    }

    public Task getToTask() {
        return toTask;
    }

    public int getWeight() {
        return weight;
    }
}
