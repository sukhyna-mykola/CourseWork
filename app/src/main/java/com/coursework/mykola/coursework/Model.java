package com.coursework.mykola.coursework;


import java.util.ArrayList;
import java.util.Arrays;

import java.util.UUID;

/**
 * Created by kolas on 10.09.16.
 */

public class Model {


    private String[][] matrixS;
    private ArrayList<NodeGraph> nodes;

    private NodeGraph currNode;


    public String[][] getMatrixS() {
        return matrixS;
    }

    public ArrayList<NodeGraph> getNodes() {
        return nodes;
    }

    public NodeGraph getCurrNode() {
        return currNode;
    }

    public Model() {

        nodes = new ArrayList<>();
        matrixS = new String[0][0];
        currNode = null;

    }


    public void setCurrNode(NodeGraph currNode) {
        this.currNode = currNode;
    }

    public void addNewNode() {
        int oldSize = matrixS.length;
        int newSize = oldSize + 1;
        NodeGraph newNodeGraph = new NodeGraph();
        nodes.add(newNodeGraph);

        String[][] tmpMatrix = new String[newSize][newSize];

        for (int i = 0; i < oldSize; i++) {
            tmpMatrix[i] = Arrays.copyOf(matrixS[i], newSize);
        }
        for (int i = 0; i < newSize; i++) {
            tmpMatrix[oldSize][i] = "";

        }

        matrixS = tmpMatrix;

        setCurrNode(nodes.get(nodes.size() - 1));

    }

    public int removeNode() {
        int removeIndex = -1;
        int oldSize = matrixS.length;
        int newSize = oldSize - 1;
        for (int i = 0; i < oldSize; i++) {
            if (nodes.get(i).getId() == currNode.getId()) {
                removeIndex = i;
                break;
            }

        }
        if (removeIndex != -1) {

            //корегування матриці суміжності(видалення рядка і стовпця)
            int rowIndex = -1;
            String[][] tmpMatrix = new String[newSize][newSize];
            for (int i = 0; i < oldSize; ) {

                if (i != removeIndex) {
                    rowIndex++;
                    int columnIndex = -1;
                    for (int j = 0; j < oldSize; ) {

                        if (j != removeIndex) {
                            columnIndex++;
                            tmpMatrix[rowIndex][columnIndex] = matrixS[i][j];
                        }
                        j++;

                    }
                }
                i++;

            }

            matrixS = tmpMatrix;
            nodes.remove(removeIndex);
            if (nodes.size() > 0) {
                setCurrNode(nodes.get(nodes.size() - 1));
            } else setCurrNode(null);
        }
        return removeIndex;
    }


    public void setPosForCurrentNode(float x, float y) {
        currNode.setCenter_x(x);
        currNode.setCenter_y(y);
    }

    public boolean checkCollision(float x, float y) {
        for (NodeGraph obj : nodes) {
            if (x > obj.getCenter_x() - NodeGraph.RADIUS_NODE && x < obj.getCenter_x() + NodeGraph.RADIUS_NODE &&
                    y > obj.getCenter_y() - NodeGraph.RADIUS_NODE && y < obj.getCenter_y() + NodeGraph.RADIUS_NODE) {
                currNode = obj;
                return true;
            }
        }
        return false;
    }

    public void clear() {
        nodes.clear();
        matrixS = new String[0][0];
        NodeGraph.NUMBER_NODE = 0;
        setCurrNode(null);
    }


}