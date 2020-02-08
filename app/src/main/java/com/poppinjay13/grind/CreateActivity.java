package com.poppinjay13.grind;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class CreateActivity extends AppCompatActivity {

    TextView start_date, end_date, start_time, end_time;
    LinearLayout dateLayout, timeLayout;
    ImageView back, set_date, set_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        back = findViewById(R.id.back);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);

        dateLayout = findViewById(R.id.date_data);
        dateLayout.setVisibility(View.GONE);
        timeLayout = findViewById(R.id.time_data);
        timeLayout.setVisibility(View.GONE);
        set_date = findViewById(R.id.set_date);
        set_date.setTag("no_show");
        set_time = findViewById(R.id.set_time);
        set_time.setTag("no_show");
        setListeners();
    }

    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String image = (String) set_date.getTag();
                if (image.equals("no_show")) {
                    set_date.setImageResource(R.drawable.calendar_selected);
                    dateLayout.setVisibility(View.VISIBLE);
                    set_date.setTag("show");
                } else{
                    set_date.setImageResource(R.drawable.calendar);
                    dateLayout.setVisibility(View.GONE);
                    set_date.setTag("no_show");
                }
            }
        });

        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String image = (String) set_time.getTag();
                if (image.equals("no_show")) {
                    set_time.setImageResource(R.drawable.clock_selected);
                    timeLayout.setVisibility(View.VISIBLE);
                    set_time.setTag("show");
                } else{
                    set_time.setImageResource(R.drawable.clock);
                    timeLayout.setVisibility(View.GONE);
                    set_time.setTag("no_show");
                }
            }
        });

        new setDate(start_date, CreateActivity.this,null);
        new setDate(end_date, CreateActivity.this, null);
        new setTime(start_time, CreateActivity.this);
        new setTime(end_time, CreateActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }
}
