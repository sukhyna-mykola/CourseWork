package com.coursework.mykola.coursework;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class GraphGantaActivity extends AppCompatActivity {

    TableLayout gvMain;
    public static final String NUMBER_OF_PROCESSORS = "NUMBER_OF_PROCESSORS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_ganta);
        getSupportActionBar().setTitle(R.string.planer_title);
        //throw new NullPointerException();

        int processorNumber = getIntent().getIntExtra(NUMBER_OF_PROCESSORS,1);
        Planer Planer = new Planer(convertMatrix(Model.matrixS), getWeightNode(Model.nodes),processorNumber);

        ArrayList<String> res = Planer.modeling();
        gvMain = (TableLayout) findViewById(R.id.table_layout);


        TableLayout.LayoutParams layoutRow = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        for (int i = 0; i < res.size() /Planer.getColumnNumber(); i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(layoutRow);
            tableRow.setGravity(Gravity.CENTER);
            TableRow.LayoutParams layoutHistory = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            for (int j = 0; j < Planer.getColumnNumber(); j++) {
                TextView textView = new TextView(this);
                textView.setTextSize(18);
                textView.setTypeface(Typeface.MONOSPACE);
                textView.setLayoutParams(layoutHistory);
                textView.setBackgroundResource(R.drawable.bg_for_grid_item);
                textView.setText(res.get(i * Planer.getColumnNumber() + j));
                // TableRow is the parent view
                //textView.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(10,10,10,10);

                tableRow.addView(textView);

            }
            gvMain.addView(tableRow);
        }
    }


    private int[][] convertMatrix(String[][] inMarr) {
        int result[][] = new int[inMarr.length][inMarr.length];
        for (int i = 0; i < inMarr.length; i++) {
            for (int j = 0; j < inMarr.length; j++) {
                if (inMarr[i][j].equals(Model.NULL))
                    result[i][j] = 0;
                else result[i][j] = Integer.parseInt(inMarr[i][j]);
            }

        }
        return result;
    }

    private int[] getWeightNode(ArrayList<NodeGraph> nodes) {
        int[] result = new int[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            result[i] = nodes.get(i).getWeight();
        }
        return result;
    }
}
