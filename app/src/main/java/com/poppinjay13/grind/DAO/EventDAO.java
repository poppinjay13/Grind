package com.poppinjay13.grind.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.poppinjay13.grind.Entities.Event;

import java.util.List;

@Dao
public interface EventDAO {

    @Query("SELECT * FROM events ORDER BY start_date ASC")
    List<Event> getEvents();

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    Event getEvent(int id);

    @Query("DELETE FROM events WHERE id = :id")
    void deleteEvent(int id);

    @Query("UPDATE events SET status = 1 WHERE id = :id")
    void completeEvent(int id);

    @Query("UPDATE events SET status = 0 WHERE id = :id")
    void completeEventUndo(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertEvent(Event event);

    @Query("SELECT * FROM events " +
            "WHERE title LIKE '%'||:keyword||'&'" +
            " OR description LIKE '%'||:keyword||'%'" +
            " OR start_date LIKE '%'||:keyword||'%'" +
            " OR start_time LIKE '%'||:keyword||'%'")
    List<Event> findEventLike(String keyword);
}
