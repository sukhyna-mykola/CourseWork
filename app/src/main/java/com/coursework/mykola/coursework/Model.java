package com.coursework.mykola.coursework;


import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by kolas on 10.09.16.
 */

public class Model {


    private String[][] matrixS;
    private ArrayList<NodeGraph> nodes;
    private ArrayList<EdgeGraph> edges;

    private NodeGraph currNode;
    private EdgeGraph currEdge;

    public static final String NULL = "";

    private boolean subModeAddNewEdge;

    public boolean isSubModeAddNewEdge() {
        return subModeAddNewEdge;
    }

    public void setSubModeAddNewEdge(boolean subModeAddNewEdge) {
        this.subModeAddNewEdge = subModeAddNewEdge;
    }

    public String[][] getMatrixS() {
        return matrixS;
    }

    public ArrayList<NodeGraph> getNodes() {
        return nodes;
    }

    public NodeGraph getCurrNode() {
        return currNode;
    }

    public EdgeGraph getCurrEdge() {
        return currEdge;
    }

    public ArrayList<EdgeGraph> getEdges() {
        return edges;
    }

    public void setCurrEdge(EdgeGraph currEdge) {

        this.currEdge = currEdge;
    }

    public Model() {

        nodes = new ArrayList<>();
        edges = new ArrayList<>();

        matrixS = new String[0][0];

        currNode = null;
        currEdge = null;

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
            for (int j = 0; j < oldSize; j++) {
                tmpMatrix[i][j] = matrixS[i][j];
            }

        }

        for (int i = 0; i < newSize; i++) {
            tmpMatrix[i][oldSize] = NULL;
        }
        for (int i = 0; i < newSize; i++) {
            tmpMatrix[oldSize][i] = NULL;

        }

        matrixS = tmpMatrix;

        setCurrNode(nodes.get(nodes.size() - 1));

    }

    public void addNewEdge(int newWeight) {

        int i = -1;
        int j = -1;

        for (int k = 0; k < nodes.size(); k++) {
            if (nodes.get(k).getId() == currEdge.getIdFrom())
                i = k;
            if (nodes.get(k).getId() == currEdge.getIdTo())
                j = k;

        }

        if (i != -1 && j != -1) {
            matrixS[i][j] = String.valueOf(newWeight);

        }

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

            ArrayList<EdgeGraph> newEdges = new ArrayList<>();
            for (EdgeGraph edge : edges) {
                if (edge.getIdFrom() != removeIndex && edge.getIdTo() != removeIndex) {
                    newEdges.add(edge);
                }
            }
            edges = newEdges;

            nodes.remove(removeIndex);

            if (nodes.size() > 0) {
                setCurrNode(nodes.get(nodes.size() - 1));
            } else setCurrNode(null);
        }
        return removeIndex;
    }

    public void removeEdge() {

        int i = -1;
        int j = -1;

        for (int k = 0; k < nodes.size(); k++) {
            if (nodes.get(k).getId() == currEdge.getIdFrom())
                i = k;
            if (nodes.get(k).getId() == currEdge.getIdTo())
                j = k;

        }


        if (i != -1 && j != -1) {
            matrixS[i][j] = NULL;

            edges.remove(currEdge);

            if (edges.size() > 0) {
                setCurrEdge(edges.get(edges.size() - 1));
            } else setCurrEdge(null);
        }

    }


    public void setPosForCurrentNode(float x, float y) {
        currNode.setCenter_x(x);
        currNode.setCenter_y(y);
    }

    public boolean checkCollisionNode(float x, float y) {
        for (NodeGraph obj : nodes) {
            if (x > obj.getCenter_x() - NodeGraph.RADIUS_NODE && x < obj.getCenter_x() + NodeGraph.RADIUS_NODE &&
                    y > obj.getCenter_y() - NodeGraph.RADIUS_NODE && y < obj.getCenter_y() + NodeGraph.RADIUS_NODE) {
                currNode = obj;
                return true;
            }
        }
        return false;
    }

    public boolean checkCollisionEdge(float x, float y) {
        for (EdgeGraph obj : edges) {
            if (searchColisium(obj, x, y)) {
                currEdge = obj;
                return true;
            }
        }
        return false;
    }
    //допуск для площі дотику
    public static final int DELTA = 10;

    private boolean searchColisium(EdgeGraph obj, float x, float y) {
        NodeGraph from = getNodeById(obj.getIdFrom());
        NodeGraph to = getNodeById(obj.getIdTo());
        Line2D b = new Line2D(from.getCenter_x(), from.getCenter_y(), to.getCenter_x(), to.getCenter_y());
        //1
        Line2D a = new Line2D(x - DELTA, y - DELTA, x - DELTA, y + DELTA);
        boolean res = getIntersectionPoint(a, b);

        if (res)
            return res;
        //2
        a = new Line2D(x - DELTA, y + DELTA, x + DELTA, y + DELTA);
        res = getIntersectionPoint(a, b);

        if (res)
            return res;
        //3
        a = new Line2D(x - DELTA, y - DELTA, x + DELTA, y - DELTA);
        res = getIntersectionPoint(a, b);

        if (res)
            return res;
        //4
        a = new Line2D(x + DELTA, y - DELTA, x + DELTA, y + DELTA);
        res = getIntersectionPoint(a, b);

        if (res)
            return res;

        return res;
    }

    public void clear() {
        nodes.clear();
        edges.clear();

        matrixS = new String[0][0];

        NodeGraph.NUMBER_NODE = 0;
        EdgeGraph.NUMBER_EDGE = 0;

        setCurrEdge(null);
        setCurrNode(null);
    }


    public void addingNewEdge() {
        currEdge = null;
        currNode = null;
        setSubModeAddNewEdge(true);

    }

    public void setFromNode() {
        if (currEdge == null) {
            currEdge = new EdgeGraph();
            currEdge.setIdFrom(currNode.getId());
        }
    }

    public void setToNode() {
        //перевірка на зациклення
        if (currNode.getId() != currEdge.getIdFrom()) {
            currEdge.setIdTo(currNode.getId());
            edges.add(currEdge);
        }
    }


    private NodeGraph getNodeById(int id) {
        for (NodeGraph node : nodes) {
            if (node.getId() == id)
                return node;
        }
        return null;
    }

    private boolean getIntersectionPoint(Line2D lineA, Line2D lineB) {
        //Метод площ трикутників
        double v1 = (lineB.getX2() - lineB.getX1()) * (lineA.getY1() - lineB.getY1()) - (lineB.getY2() - lineB.getY1()) * (lineA.getX1() - lineB.getX1());
        double v2 = (lineB.getX2() - lineB.getX1()) * (lineA.getY2() - lineB.getY1()) - (lineB.getY2() - lineB.getY1()) * (lineA.getX2() - lineB.getX1());
        double v3 = (lineA.getX2() - lineA.getX1()) * (lineB.getY1() - lineA.getY1()) - (lineA.getY2() - lineA.getY1()) * (lineB.getX1() - lineA.getX1());
        double v4 = (lineA.getX2() - lineA.getX1()) * (lineB.getY2() - lineA.getY1()) - (lineA.getY2() - lineA.getY1()) * (lineB.getX2() - lineA.getX1());

        return (v1*v2<0) && (v3*v4<0);
    }


    private class Line2D {
        private double X1;
        private double X2;
        private double Y1;
        private double Y2;

        public Line2D(double x1, double y1, double x2, double y2) {
            X1 = x1;
            X2 = x2;
            Y1 = y1;
            Y2 = y2;
        }

        public double getX1() {
            return X1;
        }

        public double getX2() {
            return X2;
        }

        public double getY1() {
            return Y1;
        }


        public double getY2() {
            return Y2;
        }

    }

}