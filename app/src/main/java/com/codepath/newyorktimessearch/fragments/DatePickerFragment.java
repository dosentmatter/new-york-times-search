package com.codepath.newyorktimessearch.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment {


    public DatePickerFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener
            = (DatePickerDialog.OnDateSetListener)getActivity();

        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}
