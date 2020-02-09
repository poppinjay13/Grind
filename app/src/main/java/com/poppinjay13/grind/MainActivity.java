package com.poppinjay13.grind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.golovin.fluentstackbar.FluentSnackbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
    RecyclerView eventsRecycler, searchRecycler;
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
        searchRecycler = findViewById(R.id.search_recycler);

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
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==0){
                    searchRecycler.setVisibility(View.GONE);
                    eventsRecycler.setVisibility(View.VISIBLE);
                }else{
                    performSearch();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void performSearch() {
        List<Event> filter = grindRoomDatabase.EventDAO().findEventLike(search.getText().toString());
        if(filter.size()<1){
            FluentSnackbar fluentSnackbar = FluentSnackbar.create(contextView);
            fluentSnackbar.create("No results found")
                    .maxLines(1)
                    .backgroundColorRes(R.color.colorPrimary)
                    .textColorRes(R.color.colorWhite)
                    .duration(Snackbar.LENGTH_SHORT)
                    .important(true)
                    .show();
        }else{
            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            searchRecycler.setHasFixedSize(true);
            searchRecycler.setLayoutManager(layoutManager);
            EventsAdapter mAdapter = new EventsAdapter(filter, MainActivity.this, contextView);
            searchRecycler.setAdapter(mAdapter);
            ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new Swipe(mAdapter));
            itemTouchHelper.attachToRecyclerView(searchRecycler);
            eventsRecycler.setVisibility(View.GONE);
            searchRecycler.setVisibility(View.VISIBLE);
            no_events.setVisibility(View.GONE);
            yes_events.setVisibility(View.VISIBLE);
            search.clearFocus();
        }
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
