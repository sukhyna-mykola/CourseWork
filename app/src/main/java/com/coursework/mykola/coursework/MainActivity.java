package com.coursework.mykola.coursework;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, ChangeWeightFragment.OnSendWeight {

    private FrameLayout frame;
    private ImageButton modeNode;
    private ImageButton modeArrow;
    private Model model;
    private Context context;
    private MySurfaceView mySurfaceView;

    private boolean arrowMode;

    public static final String DIALOG_WEIGHT = "DIALOG_WEIGHT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frame = (FrameLayout) findViewById(R.id.frame);
        modeNode = (ImageButton) findViewById(R.id.mode_node);
        modeArrow = (ImageButton) findViewById(R.id.mode_arrow);
        model = new Model();
        setNodeMode();

        context = this;
        mySurfaceView = new MySurfaceView(this, model);
        mySurfaceView.setOnTouchListener(this);
        mySurfaceView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frame.addView(mySurfaceView);
    }

    private void setNodeMode() {
        modeNode.animate().scaleX(1.25f).scaleY(1.25f).start();
        modeArrow.animate().scaleX(1f).scaleY(1f).start();
        modeNode.setEnabled(false);
        modeArrow.setEnabled(true);
        arrowMode = false;
        if (model.getNodes().size() > 0) {
            model.setCurrNode(model.getNodes().get(model.getNodes().size() - 1));
        } else
            model.setCurrNode(null);
    }

    private void setArrowMode() {
        modeArrow.animate().scaleX(1.25f).scaleY(1.25f).start();
        modeNode.animate().scaleX(1f).scaleY(1f).start();
        modeNode.setEnabled(true);
        modeArrow.setEnabled(false);
        arrowMode = true;
        model.setCurrNode(null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (arrowMode) {


        } else {
            if (model.getCurrNode() != null) {
                model.checkCollision(x, y);
                model.setPosForCurrentNode(x, y);
            }

        }
        mySurfaceView.drawing();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new: {
                if (arrowMode) {
                    //
                } else {
                    model.addNewNode();
                    FragmentManager manager = getSupportFragmentManager();
                    ChangeWeightFragment dialog = ChangeWeightFragment.newInstance(model.getCurrNode().getWeight());
                    dialog.show(manager, DIALOG_WEIGHT);
                    break;
                }
                break;
            }
            case R.id.change_weight: {
                if (arrowMode) {
                    //
                } else {
                    if (model.getCurrNode() != null) {
                        FragmentManager manager = getSupportFragmentManager();
                        ChangeWeightFragment dialog = ChangeWeightFragment.newInstance(model.getCurrNode().getWeight());
                        dialog.show(manager, DIALOG_WEIGHT);
                    }
                }
                break;
            }
            case R.id.remove: {
                if (arrowMode) {
                    //
                } else {
                    if (model.getCurrNode() != null) {
                        model.removeNode();
                    }
                }
                break;
            }
            case R.id.mode_node: {
                setNodeMode();
                break;
            }
            case R.id.mode_arrow: {
                setArrowMode();
                break;
            }
            case R.id.clear_all: {
                model.clear();
                break;
            }
        }

        mySurfaceView.drawing();
    }


    @Override
    public void setNewWeight(int newWeight) {
        if (arrowMode) {
        } else {
            model.getCurrNode().setWeight(newWeight);
        }
        mySurfaceView.drawing();
    }
}
