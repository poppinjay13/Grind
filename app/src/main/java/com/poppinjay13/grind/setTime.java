package com.poppinjay13.grind;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class setTime implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private TextView textView;
    private Calendar myCalendar;
    private Context ctx;

    setTime(TextView textView, Context ctx){
        this.textView = textView;
        this.ctx = ctx;
        this.textView.setOnClickListener(this);
        this.myCalendar = Calendar.getInstance();

    }

    @Override
    public void onClick(View v) {
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        new TimePickerDialog(ctx, this, hour, minute, true).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.textView.setText( hourOfDay + ":" +String.format("%02d", minute));
        this.textView.clearFocus();
    }

}

