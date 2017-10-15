package com.example.android.sportresult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

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
    private final String EVENT_NAME = "event";
    private final String EVENT_LOCATION = "location";
    private final String EVENT_DURATION = "duration";
    EventFirebaseAdapter firebaseAdapter;
    View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.list_events_fragments, container, false);



        final ArrayList<Event> eventItemList = new ArrayList<Event>();
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
                    String locationTest = newSnapshot.child("location").getValue().toString();
                    String durationTest = newSnapshot.child("duration").getValue().toString();
                    Event eventTest = new Event(nameTest, locationTest, durationTest);
                    eventItemList.add(eventTest);
                //    System.out.println(eventTest);


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
                firebaseAdapter = new EventFirebaseAdapter(eventItemList);
                mRecyclerView.setAdapter(firebaseAdapter);
              //  System.out.println(Arrays.toString(eventItemList.toArray()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return rootView;
    }

    private void readEventFromFirebase() {

        final ArrayList<Event> eventItemList = new ArrayList<Event>();
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
                    String locationTest = newSnapshot.child("location").getValue().toString();
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
                firebaseAdapter = new EventFirebaseAdapter(eventItemList);
                mRecyclerView.setAdapter(firebaseAdapter);
                System.out.println(Arrays.toString(eventItemList.toArray()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
