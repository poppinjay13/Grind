package com.poppinjay13.grind.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.poppinjay13.grind.Entities.Event;

import java.util.List;

@Dao
public interface EventDAO {

    @Query("SELECT * FROM events")
    List<Event> getEvents();

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    Event getEvent(int id);

    @Query("DELETE FROM events")
    void nukeTable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertEvent(Event event);
}