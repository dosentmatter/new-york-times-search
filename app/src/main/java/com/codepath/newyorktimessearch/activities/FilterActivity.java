package com.codepath.newyorktimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.newyorktimessearch.R;
import com.codepath.newyorktimessearch.fragments.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FilterActivity extends AppCompatActivity
                            implements DatePickerDialog.OnDateSetListener {

    private EditText etDate;
    private Spinner spSort;
    private CheckBox cbArts;
    private CheckBox cbFashion;
    private CheckBox cbSports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();
    }

    public void setupViews() {
        etDate = (EditText)findViewById(R.id.etDate);
        spSort = (Spinner)findViewById(R.id.spSort);
        cbArts = (CheckBox)findViewById(R.id.cbArts);
        cbFashion = (CheckBox)findViewById(R.id.cbFashion);
        cbSports = (CheckBox)findViewById(R.id.cbSports);

        etDate.setText(getIntent().getStringExtra("date"));
        spSort.setSelection(getIntent().getIntExtra("sortPosition", 0));
        cbArts.setChecked(getIntent().getBooleanExtra("arts", false));
        cbFashion.setChecked(getIntent().getBooleanExtra("fashion", false));
        cbSports.setChecked(getIntent().getBooleanExtra("sports", false));
    }

    public void onDatePick(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month,
                          int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = format.format(calendar.getTime());
        etDate.setText(formattedDate);
    }

    public void onSave(View view) {
        Intent saveIntent = new Intent();
        saveIntent.putExtra("date", etDate.getText().toString());
        saveIntent.putExtra("sort", spSort.getSelectedItem().toString());
        saveIntent.putExtra("sortPosition", spSort.getSelectedItemPosition());
        saveIntent.putExtra("arts", cbArts.isChecked());
        saveIntent.putExtra("fashion", cbFashion.isChecked());
        saveIntent.putExtra("sports", cbSports.isChecked());
        setResult(RESULT_OK, saveIntent);
        finish();
    }
}
