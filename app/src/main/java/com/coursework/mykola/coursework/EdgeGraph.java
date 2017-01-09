package com.coursework.mykola.coursework;

/**
 * Created by mykola on 08.01.17.
 */

public class EdgeGraph {

    private int id;
    private int weight;
    private int idFrom;
    private int idTo;

    public static int NUMBER_EDGE;



    public void setIdFrom(int idFrom) {
        this.idFrom = idFrom;
    }

    public void setIdTo(int idTo) {
        this.idTo = idTo;
    }

    public int getId() {

        return id;
    }

    public int getWeight() {
        return weight;
    }

    public int getIdFrom() {
        return idFrom;
    }

    public int getIdTo() {
        return idTo;
    }

    public void setWeight(int weight) {

        this.weight = weight;
    }

    public EdgeGraph() {
        this.id = ++NUMBER_EDGE;
        idFrom = -1;
        idTo = -1;

    }
}
