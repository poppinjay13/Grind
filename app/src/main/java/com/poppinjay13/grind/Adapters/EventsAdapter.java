package com.poppinjay13.grind.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.golovin.fluentstackbar.FluentSnackbar;
import com.google.android.material.snackbar.Snackbar;
import com.poppinjay13.grind.Database.GrindRoomDatabase;
import com.poppinjay13.grind.Entities.Event;
import com.poppinjay13.grind.EventActivity;
import com.poppinjay13.grind.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {
    private List<Event> myEvents;
    private Context mContext;
    private View mSnackbar;
    private Event deleted;
    private int deletePos;
    private GrindRoomDatabase grindRoomDatabase;
    private String myFormat = "dd/MM/yyyy";
    private SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.UK);
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView month, date, title, description;
        CardView card;
        MyViewHolder(View v) {
            super(v);
            grindRoomDatabase = GrindRoomDatabase.getDatabase(mContext);
            month = v.findViewById(R.id.month);
            date = v.findViewById(R.id.date);
            title = v.findViewById(R.id.title);
            description = v.findViewById(R.id.description);
            card = v.findViewById(R.id.event);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int eventId = myEvents.get(getAdapterPosition()).getId();
            Intent intent = new Intent(mContext, EventActivity.class);
            intent.putExtra("EventId",eventId);
            mContext.startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventsAdapter(Context context, View snackbar) {
        mContext = context;
        mSnackbar = snackbar;
    }

    public void setEvents(List<Event> myEvents){
        this.myEvents = myEvents;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public EventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            Event event = myEvents.get(position);
            holder.title.setText(event.getTitle());
            if(event.getStatus() == 1){
                holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.title.setTextColor(mContext.getResources().getColor(R.color.colorHints));
            }
            holder.description.setText(event.getDescription());
            holder.month.setText("---");
            holder.date.setText("--");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdformat.parse(myEvents.get(position).getStart_date()));
            holder.month.setText(new SimpleDateFormat("MMM", Locale.UK).format(calendar.getTime()));
            holder.date.setText(new SimpleDateFormat("dd", Locale.UK).format(calendar.getTime()));
        } catch (Exception ex) {
            //Log.e("ADAPTER",""+ex);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myEvents.size();
    }

    public void deleteEvent(int position) {
        try {
            deleted = myEvents.get(position);
            deletePos = position;
            grindRoomDatabase.EventDAO().deleteEvent(myEvents.get(position).getId());
            myEvents.remove(position);
            notifyItemRemoved(position);

            FluentSnackbar fluentSnackbar = FluentSnackbar.create(mSnackbar);
            fluentSnackbar.create("Event Deleted")
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
                            grindRoomDatabase.EventDAO().InsertEvent(deleted);
                            myEvents.add(deletePos, deleted);
                            notifyItemInserted(deletePos);
                        }
                    })
                    .show();
        }catch (Exception ex){
            //Log.e("ADAPTER",""+ex);
        }
    }

    public void completeEvent(final int position) {

        try {
            FluentSnackbar fluentSnackbar = FluentSnackbar.create(mSnackbar);
            if(myEvents.get(position).getStatus() == 1){
                fluentSnackbar.create("Event Already Complete :)")
                        .maxLines(1)
                        .backgroundColorRes(R.color.colorPrimary)
                        .textColorRes(R.color.colorWhite)
                        .duration(Snackbar.LENGTH_LONG)
                        .important(true)
                        .show();
                notifyItemChanged(position);
                return;
            }

            grindRoomDatabase.EventDAO().completeEvent(myEvents.get(position).getId());
            //mainActivity.notifyChange(position);
            //notifyItemChanged(position);
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
                            grindRoomDatabase.EventDAO().completeEventUndo(myEvents.get(position).getId());
                            notifyItemChanged(position);
                        }
                    })
                    .show();
        }catch (Exception ex){
        //Log.e("ADAPTER",""+ex);
        }
    }

    public Context getContext(){
        return mContext;
    }
}