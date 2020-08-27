package com.francescobertamini.app_individuale.ui.signup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(), day, month, year);

        datePickerDialog.getDatePicker().setMaxDate(new Date(102,0,1 ).getTime());
        datePickerDialog.getDatePicker().setMinDate(new Date(0,0,1).getTime());

        return datePickerDialog;
    }
}
