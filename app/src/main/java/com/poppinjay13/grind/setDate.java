package com.poppinjay13.grind;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class setDate implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView textView;
    private Calendar myCalendar;
    private Context ctx;
    private Date minDate;
    private String myFormat = "dd/MM/yyyy";
    private SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.UK);

    setDate(TextView textView, Context ctx, Date min){
        this.textView = textView;
        this.ctx = ctx;
        this.textView.setOnClickListener(this);
        this.minDate = min;
        myCalendar = Calendar.getInstance();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)     {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        textView.setText(sdformat.format(myCalendar.getTime()));
        textView.clearFocus();
    }

    @Override
    public void onClick(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, this, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        if(minDate!=null){
            try {
                if (sdformat.parse(minDate.toString()).before(sdformat.parse(sdformat.format(Calendar.getInstance().getTime())))){
                    datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                }else{
                    datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                }
            } catch (ParseException e) {
                e.printStackTrace();
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            }
        }else{
            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        }
        datePickerDialog.show();

    }

}
