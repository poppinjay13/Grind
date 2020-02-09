package com.poppinjay13.grind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.poppinjay13.grind.Database.GrindRoomDatabase;
import com.poppinjay13.grind.Entities.Event;

public class EventActivity extends AppCompatActivity {

    Event event;
    ImageView back;
    GrindRoomDatabase grindRoomDatabase;
    TextView title, description, date, time, status, delete, complete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        grindRoomDatabase = GrindRoomDatabase.getDatabase(EventActivity.this);

        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        status = findViewById(R.id.status);
        complete = findViewById(R.id.btnComplete);
        delete = findViewById(R.id.btnDelete);

        setListeners();
    }

    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, EditActivity.class);
                intent.putExtra("EventId",getEventId());
                EventActivity.this.startActivity(intent);
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grindRoomDatabase.EventDAO().completeEvent(getEventId());
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grindRoomDatabase.EventDAO().deleteEvent(getEventId());
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    private void setData() {
        int id = getEventId();
        event = grindRoomDatabase.EventDAO().getEvent(id);

        title.setText(event.getTitle());
        description.setText(event.getDescription());
        String date = event.getStart_date();//+"-"+event.getEnd_date();
        this.date.setText(date);
        String time = event.getStart_time();//+"-"+event.getEnd_time();
        this.time.setText(time);
        if(event.getStatus() == 0){
            status.setText(R.string.pending);
        }else{
            status.setText(R.string.complete);
            complete.setVisibility(View.GONE);
        }
    }

    private int getEventId() {
        int event_id;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            event_id = 0;
            Toast.makeText(EventActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            event_id = extras.getInt("EventId");
        }
        return event_id;
    }
}
