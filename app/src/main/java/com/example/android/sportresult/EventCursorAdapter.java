package com.example.android.sportresult;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sportresult.data.EventContract.EventEntry;

/**
 * Created by Hp on 13/10/2017.
 */

public class EventCursorAdapter extends CursorRecyclerAdapter<EventCursorAdapter.ViewHolder>  {


    public EventCursorAdapter (Context context, Cursor c) {
        super(context, c);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(EventCursorAdapter.ViewHolder viewHolder, Cursor cursor) {
        final Uri currentUri = EventEntry.CONTENT_URI;

        // Find the columns of events attributes that we're interested in
        final int eventId = cursor.getInt(cursor.getColumnIndex(EventEntry._ID));
        int nameColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_NAME);
        int locationColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_LOCATION);
        int durationColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_DURATION);

        // Read the event attributes from the Cursor for the current event
        String eventName = cursor.getString(nameColumnIndex);
        String eventLocation = cursor.getString(locationColumnIndex);
        String eventDuration = cursor.getString(durationColumnIndex);

        // Update the TextViews with the attributes for the current event
        viewHolder.nameTextView.setText(eventName);
        viewHolder.locationTextView.setText(eventLocation);
        viewHolder.durationTextView.setText(eventDuration);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected CardView cardView;
        protected TextView nameTextView;
        protected TextView locationTextView;
        protected TextView durationTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            locationTextView = (TextView) itemView.findViewById(R.id.location);
            durationTextView = (TextView) itemView.findViewById(R.id.duration);

        }
    }
}
