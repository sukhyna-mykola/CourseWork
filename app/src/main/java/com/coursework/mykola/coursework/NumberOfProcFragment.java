package com.coursework.mykola.coursework;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by mykola on 08.01.17.
 */

public class NumberOfProcFragment extends DialogFragment {

    public static final int MAX_WEIGHT = 10;
    public static final int MIN_WEIGHT = 0;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.change_weight_fragment, null);
        final NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.number_picker);
        numberPicker.setMaxValue(MAX_WEIGHT);
        numberPicker.setMinValue(MIN_WEIGHT);
        numberPicker.setValue(3);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.number_of_proc_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getContext(), GraphGantaActivity.class));
                        dismiss();
                    }
                })
                .create();
    }



}
