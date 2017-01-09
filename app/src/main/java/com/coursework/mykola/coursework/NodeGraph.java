package com.coursework.mykola.coursework;

import java.util.UUID;

/**
 * Created by mykola on 08.01.17.
 */

public class NodeGraph {

    private float center_x;
    private float center_y;
    private int weight;
    private int id;


    public static final int RADIUS_NODE = 45;
    public static int NUMBER_NODE;

    public NodeGraph() {
        this.id = ++NUMBER_NODE;

        center_x = RADIUS_NODE ;
        center_y = RADIUS_NODE ;

    }

    public void setCenter_x(float center_x) {
        this.center_x = center_x;
    }

    public void setCenter_y(float center_y) {
        this.center_y = center_y;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public float getCenter_x() {

        return center_x;
    }

    public float getCenter_y() {
        return center_y;
    }

    public int getWeight() {
        return weight;
    }

    public int getId() {
        return id;
    }
}
