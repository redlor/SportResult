package com.example.android.sportresult;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sportresult.data.EventContract.EventEntry;
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

public class ListEventsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EVENT_LOADER = 0;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    EventCursorAdapter mCursorAdapter;
    EventFirebaseAdapter eventFirebaseAdapter;
    View rootView;
    ArrayList<Event> eventsList = new ArrayList<>();
    EventFirebaseAdapter firebaseAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.list_events_fragments, container, false);
        getLoaderManager().initLoader(EVENT_LOADER, null, this);


            /*    // Find the RecyclerView which will be populated with the product data
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set the adapter
        mCursorAdapter = new EventCursorAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mCursorAdapter);*/

        return rootView;
    }

@Override
public void onStart() {
    super.onStart();


}
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                EventEntry._ID,
                EventEntry.COLUMN_EVENT_NAME,
                EventEntry.COLUMN_EVENT_LOCATION,
                EventEntry.COLUMN_EVENT_DURATION};

        return new CursorLoader(getActivity(),
                EventEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

if (cursor != null) {
    cursor.moveToFirst();
   Event event;
    for (int i=0; i<cursor.getCount(); i++) {
        int nameColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_NAME);
        int locationColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_LOCATION);
        int durationColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_DURATION);
        String name = cursor.getString(nameColumnIndex);
        String location = cursor.getString(locationColumnIndex);
        String duration =  cursor.getString(durationColumnIndex);
        event = new Event(name, location, duration);
        eventsList.add(event);
        cursor.moveToNext();
        System.out.println(event.toString());
    }
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myFirebaseRef = database.getReference("events");
    myFirebaseRef.orderByKey().addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<Event> eventItemList = new ArrayList<Event>();
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
            System.out.println("List from Firebase: " + Arrays.toString(eventItemList.toArray()));
            // Find the RecyclerView which will be populated with the product data
            eventItemList.addAll(eventsList);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
            mLayoutManager = new LinearLayoutManager(getActivity());

            mRecyclerView.setLayoutManager(mLayoutManager);

            // Set the adapter
            firebaseAdapter = new EventFirebaseAdapter(eventItemList);
            mRecyclerView.setAdapter(firebaseAdapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}
        System.out.println("List from SQLite :" + Arrays.toString(eventsList.toArray()));

    //    mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
     //   mCursorAdapter.swapCursor(null);
    }

}
