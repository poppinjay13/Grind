package com.poppinjay13.grind;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.golovin.fluentstackbar.FluentSnackbar;
import com.google.android.material.snackbar.Snackbar;
import com.poppinjay13.grind.Database.GrindRoomDatabase;
import com.poppinjay13.grind.Entities.Event;

public class EventActivity extends AppCompatActivity {

    Event event;
    ImageView back;
    View contextView;
    GrindRoomDatabase grindRoomDatabase;
    TextView title, description, date, time, status, delete, complete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        grindRoomDatabase = GrindRoomDatabase.getDatabase(EventActivity.this);

        contextView = findViewById(R.id.viewSnack);
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
                FluentSnackbar fluentSnackbar = FluentSnackbar.create(contextView);
                status.setText(R.string.complete);
                fluentSnackbar.create("Event Marked as Complete")
                        .maxLines(1)
                        .backgroundColorRes(R.color.colorPrimary)
                        .textColorRes(R.color.colorWhite)
                        .duration(Snackbar.LENGTH_LONG)
                        .actionText("Undo")
                        .actionTextColorRes(R.color.colorWhite)
                        .important(true)
                        .action(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                grindRoomDatabase.EventDAO().completeEventUndo(getEventId());
                                status.setText(R.string.pending);
                            }
                        })
                        .show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EventActivity.this)
                        .setTitle("Delete Event?")
                        .setMessage(event.getTitle()+" event will be permanently lost")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                grindRoomDatabase.EventDAO().deleteEvent(getEventId());
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
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
