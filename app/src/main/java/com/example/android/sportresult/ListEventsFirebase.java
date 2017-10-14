package com.example.android.sportresult;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hp on 13/10/2017.
 */

public class ListEventsFirebase extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    EventCursorAdapter mCursorAdapter;
    private EditText mNameEditText;
    private EditText mLocationEditText;
    private EditText mDurationEditText;
    private Uri mCurrentProductUri;
    private RadioButton mSQLiteRadio;
    private RadioButton mFirebaseRadio;
    private Button mAddButton;
    private final String EVENT_NAME = "event";
    private final String EVENT_LOCATION = "location";
    private final String EVENT_DURATION = "duration";
    ExampleAdapter exampleAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.list_events_fragments, container, false);

        mNameEditText = (EditText) rootView.findViewById(R.id.edit_event_name);
        mLocationEditText = (EditText) rootView.findViewById(R.id.edit_event_location);
        mDurationEditText = (EditText) rootView.findViewById(R.id.edit_event_duration);
        mSQLiteRadio = (RadioButton) rootView.findViewById(R.id.sqlite_radio_button);
        mFirebaseRadio = (RadioButton) rootView.findViewById(R.id.firebase_radio_button);
        mAddButton = (Button)rootView.findViewById(R.id.button);

       final ArrayList<Event> eventItemList =  new ArrayList<Event>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myFirebaseRef = database.getReference("events");
        myFirebaseRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot newSnapshot : dataSnapshot.getChildren()) {
            //      String event = newSnapshot.getValue().toString();
             //       values.add(event);
               //     System.out.println("Single event: " +event);
                    String nameTest = newSnapshot.child("event").getValue().toString();
                    String locationTest =  newSnapshot.child("location").getValue().toString();
                    String durationTest = newSnapshot.child("duration").getValue().toString();
                    Event eventTest = new Event(nameTest, locationTest, durationTest);
                    eventItemList.add(eventTest);
                    System.out.println(eventTest);


                /*    HashMap<String, Event> messageMap =
                            (HashMap<String, Event>) newSnapshot.getValue();
                    Collection<Event> eventItems = messageMap.values();
                    eventItemList.addAll(eventItems);*/

                }
                //Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
              //  List<Object> values = new ArrayList<>(td.values());

// Find the RecyclerView which will be populated with the product data
                mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
                mLayoutManager = new LinearLayoutManager(getActivity());

                mRecyclerView.setLayoutManager(mLayoutManager);

                // Set the adapter
                exampleAdapter = new ExampleAdapter(eventItemList);
                mRecyclerView.setAdapter(exampleAdapter);
                System.out.println(Arrays.toString(eventItemList.toArray()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return rootView;
    }

    private void readEventFromFirebase() {


        String nameString = mNameEditText.getText().toString().trim();
        String locationString = mLocationEditText.getText().toString().trim();
        String durationString = mDurationEditText.getText().toString().trim();


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myFirebaseRef = database.getReference("events");
                    List<String> values = new ArrayList<>();
                    DataSnapshot snapshot;
                   myFirebaseRef.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                           List<Object> values = new ArrayList<>(td.values());
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });


                //    myFirebaseRef.child(EVENT_NAME).setValue(nameString);
                //    myFirebaseRef.child(EVENT_LOCATION).setValue(locationString);
                //    myFirebaseRef.child(EVENT_DURATION).setValue(durationString);


            }

    private class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

        public class ExampleViewHolder extends RecyclerView.ViewHolder {

            TextView text1, text2, text3;

           public ExampleViewHolder(View itemView) {
                super(itemView);
                text1 = (TextView) itemView.findViewById(R.id.name2);
                text2 = (TextView) itemView.findViewById(R.id.location2);
                text3 = (TextView) itemView.findViewById(R.id.duration2);
            }
        }

        private ArrayList<Event> mCustomObjects;

        public ExampleAdapter(ArrayList<Event> arrayList) {
            mCustomObjects = arrayList;
        }

        @Override
        public int getItemCount() {
            return mCustomObjects.size();
        }

        @Override
        public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_firebase, parent, false);
            return new ExampleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ExampleViewHolder holder, int position) {
          final  Event object = mCustomObjects.get(position);

            holder.text1.setText(object.getName());
            holder.text2.setText(object.getLocation());
            holder.text3.setText(object.getDuration());
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }
}
