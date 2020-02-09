package com.poppinjay13.grind;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.poppinjay13.grind.Database.GrindRoomDatabase;
import com.poppinjay13.grind.Entities.Event;

import java.util.Calendar;


public class CreateActivity extends AppCompatActivity {

    TextView start_date, end_date, start_time, end_time, save;
    EditText title, description;
    LinearLayout dateLayout, timeLayout;
    ImageView back, set_date, set_time;
    GrindRoomDatabase grindRoomDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        grindRoomDatabase = GrindRoomDatabase.getDatabase(CreateActivity.this);

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

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        save = findViewById(R.id.btnCreate);
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check(title)&&check(description)){
                    save();
                }
            }
        });
    }

    private void save() {
        try{
            Event event = new Event(
                    grindRoomDatabase.EventDAO().getEvents().size()+1,
                    title.getText().toString(),
                    description.getText().toString(),
                    0,
                    start_date.getText().toString(),
                    end_date.getText().toString(),
                    start_time.getText().toString(),
                    end_time.getText().toString(),
                    Calendar.getInstance().getTime().toString(),
                    Calendar.getInstance().getTime().toString()
                    );
            grindRoomDatabase.EventDAO().InsertEvent(event);
            finish();
        }catch (Exception ex){
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
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

    public boolean check(EditText editText){
        if(editText.getText().toString().length()<1){
            editText.setError("Please provide this information");
            editText.requestFocus();
            return false;
        }else{
            return true;
        }
    }
}
