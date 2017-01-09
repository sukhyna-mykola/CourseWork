package com.coursework.mykola.coursework;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, ChangeWeightFragment.OnSendWeight {

    private FrameLayout frame;
    private ImageButton modeNode;
    private ImageButton modeArrow;
    private LinearLayout mainActionsMenu;
    private Model model;
    private MySurfaceView mySurfaceView;

    private boolean arrowMode;

    public static final String DIALOG_WEIGHT = "DIALOG_WEIGHT";

    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frame = (FrameLayout) findViewById(R.id.frame);
        modeNode = (ImageButton) findViewById(R.id.mode_node);
        modeArrow = (ImageButton) findViewById(R.id.mode_arrow);
        mainActionsMenu = (LinearLayout) findViewById(R.id.main_actions_menu);
        model = new Model();
        setNodeMode();

        mySurfaceView = new MySurfaceView(this, model);
        mySurfaceView.setOnTouchListener(this);
        mySurfaceView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frame.addView(mySurfaceView);
    }

    private void setNodeMode() {
        getSupportActionBar().setTitle(R.string.node_mode_title);

        modeNode.animate().scaleX(1.25f).scaleY(1.25f).start();
        modeArrow.animate().scaleX(1f).scaleY(1f).start();
        mainActionsMenu.animate().rotationXBy(360).start();
        modeNode.setEnabled(false);
        modeArrow.setEnabled(true);
        arrowMode = false;
        if (model.getNodes().size() > 0) {
            model.setCurrNode(model.getNodes().get(model.getNodes().size() - 1));
        } else
            model.setCurrNode(null);

        model.setCurrEdge(null);
    }

    private void setArrowMode() {
        getSupportActionBar().setTitle(R.string.edge_mode_title);
        modeArrow.animate().scaleX(1.25f).scaleY(1.25f).start();
        modeNode.animate().scaleX(1f).scaleY(1f).start();
        mainActionsMenu.animate().rotationXBy(-360).start();
        modeNode.setEnabled(true);
        modeArrow.setEnabled(false);
        arrowMode = true;
        if (model.getEdges().size() > 0) {
            model.setCurrEdge(model.getEdges().get(model.getEdges().size() - 1));
        } else
            model.setCurrEdge(null);
        model.setCurrNode(null);



    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (arrowMode) {
            if (model.isSubModeAddNewEdge()) {
                if (model.checkCollisionNode(x, y)) {
                    if (model.getCurrEdge() == null) {
                        model.setFromNode();
                        Snackbar.make(v, R.string.choose_to_node,Snackbar.LENGTH_LONG).show();
                    } else

                    if (model.getCurrEdge().getIdFrom() != -1) {
                        model.setToNode();
                        if(model.getCurrEdge().getIdTo() != -1) {
                            FragmentManager manager = getSupportFragmentManager();
                            ChangeWeightFragment dialog = ChangeWeightFragment.newInstance(model.getCurrEdge().getWeight());
                            dialog.show(manager, DIALOG_WEIGHT);
                        }
                    }

                }
            } else if (model.getCurrEdge() != null) {
                model.checkCollisionEdge(x, y);
                Log.d(TAG, String.valueOf(model.getCurrEdge().getId()));
            }

        } else {
            if (model.getCurrNode() != null) {
                model.checkCollisionNode(x, y);
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
                    model.addingNewEdge();
                    Snackbar.make(v, R.string.choose_from_node,Snackbar.LENGTH_LONG).show();
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
                    if (model.getCurrEdge() != null) {
                        FragmentManager manager = getSupportFragmentManager();
                        ChangeWeightFragment dialog = ChangeWeightFragment.newInstance(model.getCurrEdge().getWeight());
                        dialog.show(manager, DIALOG_WEIGHT);
                    }
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
                    if (model.getCurrEdge() != null) {
                        model.removeEdge();
                    }
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
            model.addNewEdge(newWeight);
            model.getCurrEdge().setWeight(newWeight);
        } else {
            model.getCurrNode().setWeight(newWeight);
        }
        mySurfaceView.drawing();
    }
}
