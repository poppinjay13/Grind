package com.poppinjay13.grind;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.poppinjay13.grind.Adapters.EventsAdapter;
import com.poppinjay13.grind.Database.GrindRoomDatabase;
import com.poppinjay13.grind.Entities.Event;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton create;
    private EditText search;
    private View contextView;
    private LinearLayout yes_events, no_events;
    private List<Event> events;
    TextView username;
    RecyclerView eventsRecycler;
    GrindRoomDatabase grindRoomDatabase;
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grindRoomDatabase = GrindRoomDatabase.getDatabase(MainActivity.this);
        preferences = new Preferences();
        preferences.prefConfig(MainActivity.this);
        contextView = findViewById(R.id.viewSnack);

        username = findViewById(R.id.username);
        yes_events = findViewById(R.id.events);
        no_events = findViewById(R.id.no_events);
        eventsRecycler = findViewById(R.id.events_recycler);

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NameActivity.class));
            }
        });
        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateActivity.class));
            }
        });

        search = findViewById(R.id.search);
        search.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch() {
        Toast.makeText(this, "Searching", Toast.LENGTH_SHORT).show();
    }

    private void loadEvents(){
        events = grindRoomDatabase.EventDAO().getEvents();
        if(events.size()<1){
            no_events.setVisibility(View.VISIBLE);
            yes_events.setVisibility(View.GONE);
        }else{
            no_events.setVisibility(View.GONE);
            yes_events.setVisibility(View.VISIBLE);
            //
            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            eventsRecycler.setHasFixedSize(true);
            eventsRecycler.setLayoutManager(layoutManager);
            EventsAdapter mAdapter = new EventsAdapter(events, MainActivity.this, contextView);
            eventsRecycler.setAdapter(mAdapter);
            ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new Swipe(mAdapter));
            itemTouchHelper.attachToRecyclerView(eventsRecycler);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();
        setName();
    }

    private void setName() {
        username.setText(preferences.readName());
    }
}
