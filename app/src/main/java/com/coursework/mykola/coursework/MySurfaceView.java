package com.coursework.mykola.coursework;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MySurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {

    private Model model;


    public MySurfaceView(Context context, Model model) {
        super(context);
        // TODO Auto-generated constructor stub
        getHolder().addCallback(this);
      //  setZOrderOnTop(true);
      //  this.setBackgroundColor(0Xffffffff);
        this.model = model;

    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
                               int arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }


    public void drawing() {
        Canvas c = null;
        try {
            c = getHolder().lockCanvas(null);
            synchronized (getHolder()) {
                c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                c.drawColor(Color.WHITE);

                drawGraph(c);
            }
        } finally {
            // do this in a finally so that if an exception is thrown
            // during the above, we don't leave the Surface in an
            // inconsistent state
            if (c != null) {
                getHolder().unlockCanvasAndPost(c);
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    synchronized private void drawGraph(Canvas c) {
        Paint p = new Paint();
        p.setTextAlign(Paint.Align.CENTER);


        // String[][] modernMatr = model.matrixLGraph.clone();

        for (NodeGraph node : model.getNodes()) {
            p.setColor(Color.BLACK);
            c.drawCircle(node.getCenter_x(), node.getCenter_y(), NodeGraph.RADIUS_NODE+2, p);
            if (model.getCurrNode() != null)
                if (node.getId() == (model.getCurrNode().getId()))
                    p.setColor(Color.GREEN);
                else
                    p.setColor(Color.WHITE);
            else
                p.setColor(Color.WHITE);
            c.drawCircle(node.getCenter_x(), node.getCenter_y(), NodeGraph.RADIUS_NODE, p);



            p.setColor(Color.BLACK);
            p.setTextSize(15);
            c.drawText(String.valueOf(node.getId()), node.getCenter_x(), node.getCenter_y() - NodeGraph.RADIUS_NODE / 2, p);
            p.setTextSize(25);
            c.drawText("—", node.getCenter_x(), node.getCenter_y(), p);
            p.setColor(Color.RED);
            c.drawText(String.valueOf(node.getWeight()), node.getCenter_x(), node.getCenter_y() + NodeGraph.RADIUS_NODE / 2, p);
        }

        for (int i = 0; i < model.getMatrixS().length; i++) {
            for (int j = 0; j < model.getMatrixS().length; j++) {
                if (!model.getMatrixS()[i][j].equals(Model.NULL)) {

                    if (model.getCurrEdge() != null) {
                        if (model.getNodes().get(i).getId() == model.getCurrEdge().getIdFrom() &&
                                model.getNodes().get(j).getId() == model.getCurrEdge().getIdTo())
                            p.setColor(Color.GREEN);
                        else
                            p.setColor(Color.BLACK);
                    } else
                        p.setColor(Color.BLACK);


                    Point p1 = new Point(model.getNodes().get(i).getCenter_x(), model.getNodes().get(i).getCenter_y());
                    Point p2 = new Point(model.getNodes().get(j).getCenter_x(), model.getNodes().get(j).getCenter_y());
                    Point p_from = getCircleLineIntersectionPoint(p1, p2, p1, NodeGraph.RADIUS_NODE).get(1);
                    Point p_to = getCircleLineIntersectionPoint(p1, p2, p2, NodeGraph.RADIUS_NODE).get(0);


                    c.drawLine(p_from.x, p_from.y, p_to.x, p_to.y, p);
                    fillArrow(p, c, p_from.x, p_from.y, p_to.x, p_to.y);

                    float center_x = ceneter(p_from.x, p_to.x);
                    float center_y = ceneter(p_from.y, p_to.y);

                    p.setColor(Color.WHITE);
                    p.setStyle(Paint.Style.FILL);
                    c.drawCircle(center_x, center_y, 20, p);


                    p.setColor(Color.RED);
                    c.drawText(model.getMatrixS()[i][j], center_x, center_y+10, p);


                }
            }
        }
    }

    Point getCollCircleWithAngle(Point center, Point point, float alpha) {
        float rx = point.x - center.x;
        float ry = point.y - center.y;
        float cos = (float) Math.cos(alpha);
        float sin = (float) Math.sin(alpha);
        float x1 = center.x + rx * cos - ry * sin;
        float y1 = center.y + rx * sin + ry * cos;
        return new Point(x1, y1);
    }

    float ceneter(float arg1, float arg2) {
        return (arg1 + arg2) / 2;
    }

    private void fillArrow(Paint paint, Canvas canvas, float x0, float y0, float x1, float y1) {
        paint.setStyle(Paint.Style.STROKE);

        int arrowHeadLenght = 15;
        int arrowHeadAngle = 45;
        float[] linePts = new float[]{x1 - arrowHeadLenght, y1, x1, y1};
        float[] linePts2 = new float[]{x1, y1, x1, y1 + arrowHeadLenght};
        Matrix rotateMat = new Matrix();

        //get the center of the line
        float centerX = x1;
        float centerY = y1;

        //set the angle
        double angle = Math.atan2(y1 - y0, x1 - x0) * 180 / Math.PI + arrowHeadAngle;

        //rotate the matrix around the center
        rotateMat.setRotate((float) angle, centerX, centerY);
        rotateMat.mapPoints(linePts);
        rotateMat.mapPoints(linePts2);

        canvas.drawLine(linePts[0], linePts[1], linePts[2], linePts[3], paint);
        canvas.drawLine(linePts2[0], linePts2[1], linePts2[2], linePts2[3], paint);
    }


    public static List<Point> getCircleLineIntersectionPoint(Point pointA,
                                                             Point pointB, Point center, double radius) {
        float baX = pointB.x - pointA.x;
        float baY = pointB.y - pointA.y;
        float caX = center.x - pointA.x;
        float caY = center.y - pointA.y;

        float a = baX * baX + baY * baY;
        float bBy2 = baX * caX + baY * caY;
        float c = (float) (caX * caX + caY * caY - radius * radius);

        float pBy2 = bBy2 / a;
        float q = c / a;

        float disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return Collections.emptyList();
        }
        // if disc == 0 ... dealt with later
        float tmpSqrt = (float) Math.sqrt(disc);
        float abScalingFactor1 = -pBy2 + tmpSqrt;
        float abScalingFactor2 = -pBy2 - tmpSqrt;

        Point p1 = new Point(pointA.x - baX * abScalingFactor1, pointA.y
                - baY * abScalingFactor1);
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return Collections.singletonList(p1);
        }
        Point p2 = new Point(pointA.x - baX * abScalingFactor2, pointA.y
                - baY * abScalingFactor2);
        return Arrays.asList(p1, p2);
    }

    static class Point {
        float x, y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point [x=" + x + ", y=" + y + "]";
        }
    }

}
