package com.coursework.mykola.coursework;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import java.util.zip.Inflater;

/**
 * Created by mykola on 08.01.17.
 */

public class ChangeWeightFragment extends DialogFragment {

    public static final int MAX_WEIGHT = 10;
    public static final int MIN_WEIGHT = 0;

    public static final String CURRENT_WEIGHT = "CURRENT_WEIGHT";

    private OnSendWeight dataWeight;

    public static ChangeWeightFragment newInstance(int weight) {

        Bundle args = new Bundle();
        args.putInt(CURRENT_WEIGHT, weight);

        ChangeWeightFragment fragment = new ChangeWeightFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.change_weight_fragment, null);
        final NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.number_picker);
        numberPicker.setMaxValue(MAX_WEIGHT);
        numberPicker.setMinValue(MIN_WEIGHT);
        int weight = getArguments().getInt(CURRENT_WEIGHT);
        numberPicker.setValue(weight);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataWeight.setNewWeight(numberPicker.getValue());
                        dismiss();
                    }
                })
                .create();
    }

    public interface OnSendWeight {
        public void setNewWeight(int newWeight);
    }


    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        dataWeight = (OnSendWeight) a;
    }

}
