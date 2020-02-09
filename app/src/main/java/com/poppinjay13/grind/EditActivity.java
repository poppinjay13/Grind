package com.poppinjay13.grind;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.poppinjay13.grind.Database.GrindRoomDatabase;
import com.poppinjay13.grind.Entities.Event;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    TextView start_date, end_date, start_time, end_time, save;
    EditText title, description;
    CheckBox status;
    ImageView back;
    GrindRoomDatabase grindRoomDatabase;
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        grindRoomDatabase = GrindRoomDatabase.getDatabase(EditActivity.this);

        back = findViewById(R.id.back);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        status = findViewById(R.id.status);
        save = findViewById(R.id.btnUpdate);

        setListeners();
        setData();
    }

    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new setDate(start_date, EditActivity.this,null);
        new setDate(end_date, EditActivity.this, null);
        new setTime(start_time, EditActivity.this);
        new setTime(end_time, EditActivity.this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check(title)&&check(description)){
                    update();
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

    public boolean check(EditText editText){
        if(editText.getText().toString().length()<1){
            editText.setError("Please provide this information");
            editText.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    private void update() {
        try{
            int status;
            if(this.status.isChecked()){
                status = 1;
            }else{
                status = 0;
            }
            Event event = new Event(
                    getEventId(),
                    title.getText().toString(),
                    description.getText().toString(),
                    status,
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

    private void setData() {
        try {
            int id = getEventId();
            event = grindRoomDatabase.EventDAO().getEvent(id);

            title.setText(event.getTitle());
            description.setText(event.getDescription());
            this.start_date.setText(event.getStart_date());
            this.end_date.setText(event.getEnd_date());
            this.start_time.setText(event.getStart_time());
            this.end_time.setText(event.getEnd_time());
            if (event.getStatus() == 0) {
                status.setChecked(false);
            } else {
                status.setChecked(true);
            }
        }catch (Exception ex){
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            Log.e("Edit",""+ex);
        }
    }

    private int getEventId() {
        int event_id;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            event_id = 0;
            Toast.makeText(EditActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            event_id = extras.getInt("EventId");
        }
        return event_id;
    }
}
