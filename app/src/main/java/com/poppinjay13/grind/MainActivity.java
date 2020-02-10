package com.poppinjay13.grind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton create;
    private EditText search;
    private View contextView;
    private LinearLayout yes_events, no_events;
    private List<Event> events;
    Spinner sort;
    TextView username;
    RecyclerView eventsRecycler;
    GrindRoomDatabase grindRoomDatabase;
    Preferences preferences;
    private EventsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grindRoomDatabase = GrindRoomDatabase.getDatabase(MainActivity.this);
        preferences = new Preferences();
        preferences.prefConfig(MainActivity.this);
        contextView = findViewById(R.id.viewSnack);
        mAdapter = new EventsAdapter(MainActivity.this, contextView);

        username = findViewById(R.id.username);
        yes_events = findViewById(R.id.events);
        no_events = findViewById(R.id.no_events);
        eventsRecycler = findViewById(R.id.events_recycler);

        sort = findViewById(R.id.sort_by);
        String[] sorters = new String[]{"Date asc","Date desc","Title asc","Title desc","Status"};
        ArrayAdapter<CharSequence> accountAdapter = new ArrayAdapter<CharSequence>(MainActivity.this, R.layout.spinner_texts, sorters );
        accountAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sort.setAdapter(accountAdapter);
        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortBy(events, sort.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                    events = grindRoomDatabase.EventDAO().getEvents();
                    loadEvents(events);
                }else{
                    performSearch();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        eventsRecycler.setHasFixedSize(true);
        eventsRecycler.setLayoutManager(layoutManager);
    }

    private void sortBy(List<Event> events, int selectedItemPosition) {
        if (selectedItemPosition == 0) {
            if (events != null) {
                Collections.sort(events, new Comparator<Event>() {
                    @Override
                    public int compare(Event lhs, Event rhs) {
                        return lhs.getStart_date().compareTo(rhs.getStart_date());
                    }
                });
            }
        }else if (selectedItemPosition == 1) {
            if (events != null) {
                Collections.sort(events, new Comparator<Event>() {
                    @Override
                    public int compare(Event lhs, Event rhs) {
                        return rhs.getStart_date().compareTo(lhs.getStart_date());
                    }
                });
            }
        }else if(selectedItemPosition == 2){
            if (events != null) {
                Collections.sort(events, new Comparator<Event>() {
                    @Override
                    public int compare(Event lhs, Event rhs) {
                        return lhs.getTitle().compareTo(rhs.getTitle());
                    }
                });
            }
        }else if (selectedItemPosition == 3) {
            if (events != null) {
                Collections.sort(events, new Comparator<Event>() {
                    @Override
                    public int compare(Event lhs, Event rhs) {
                        return rhs.getTitle().compareTo(lhs.getTitle());
                    }
                });
            }
        }else{
            if (events != null) {
                Collections.sort(events, new Comparator<Event>() {
                    @Override
                    public int compare(Event lhs, Event rhs) {
                        return rhs.getStatus() - (lhs.getStatus());
                    }
                });
            }
        }
        sort.setSelection(selectedItemPosition);
        assert events != null;
        loadEvents(events);
    }

    private void performSearch() {
        List<Event> filter = grindRoomDatabase.EventDAO().findEventLike(search.getText().toString());
        if(filter.size()<1){
            FluentSnackbar fluentSnackbar = FluentSnackbar.create(contextView);
            fluentSnackbar.create("No results found for that keyword")
                    .maxLines(2)
                    .backgroundColorRes(R.color.colorPrimary)
                    .textColorRes(R.color.colorWhite)
                    .duration(Snackbar.LENGTH_SHORT)
                    .important(true)
                    .show();
        }
        loadEvents(filter);
    }

    private void loadEvents(List<Event> events){
        this.events = events;
        if(events.size()<1){
            no_events.setVisibility(View.VISIBLE);
            yes_events.setVisibility(View.GONE);
        }else{
            no_events.setVisibility(View.GONE);
            yes_events.setVisibility(View.VISIBLE);
            //
            mAdapter.setEvents(events);
            eventsRecycler.setAdapter(mAdapter);
            ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new Swipe(mAdapter));
            itemTouchHelper.attachToRecyclerView(eventsRecycler);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        events = grindRoomDatabase.EventDAO().getEvents();
        loadEvents(events);
        setName();
    }

    private void setName() {
        username.setText(preferences.readName());
    }
}
