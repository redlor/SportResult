package com.example.android.sportresult;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hp on 14/10/2017.
 */

public class EventFirebaseAdapter extends RecyclerView.Adapter<EventFirebaseAdapter.ExampleViewHolder> {

    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textLocation, textDuration;
        CardView cardView;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            textName = (TextView) itemView.findViewById(R.id.name);
            textLocation = (TextView) itemView.findViewById(R.id.location);
            textDuration = (TextView) itemView.findViewById(R.id.duration);
        }
    }

    private ArrayList<Event> mCustomObjects;

    public EventFirebaseAdapter(ArrayList<Event> arrayList) {
        mCustomObjects = arrayList;
    }

    @Override
    public int getItemCount() {
        return mCustomObjects.size();
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ExampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        final  Event object = mCustomObjects.get(position);

        holder.textName.setText(object.getName());
        holder.textLocation.setText(object.getLocation());
        holder.textDuration.setText(object.getDuration());
        AddEventFragment fragment = new AddEventFragment();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

