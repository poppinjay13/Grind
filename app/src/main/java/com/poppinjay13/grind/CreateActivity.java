package com.poppinjay13.grind;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class CreateActivity extends AppCompatActivity {

    LinearLayout dateLayout, timeLayout;
    ImageView back, set_date, set_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dateLayout = findViewById(R.id.date_data);
        timeLayout = findViewById(R.id.time_data);
        set_date = findViewById(R.id.set_date);
        set_date.setTag("no_show");
        set_time = findViewById(R.id.set_time);
        set_time.setTag("no_show");
        setListeners();
    }

    private void setListeners() {
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
