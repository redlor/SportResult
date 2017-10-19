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
import android.widget.ImageView;
import android.widget.TextView;

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
    View rootView;
    ArrayList<Event> eventsListSQLite = new ArrayList<>();
    ArrayList<Event> eventItemListFirebase = new ArrayList<Event>();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DynamicListAdapter mDynamicListAdapter;
    ImageView imageView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.list_events_fragments, container, false);
        getLoaderManager().initLoader(EVENT_LOADER, null, this);

        imageView = (ImageView) getActivity().findViewById(R.id.main_image);
        imageView.setVisibility(View.GONE);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imageView.setVisibility(View.VISIBLE);
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
// Retrieve SQLite data and store in an ArryList
        if (cursor != null) {
            cursor.moveToFirst();
            Event event;
            for (int i = 0; i < cursor.getCount(); i++) {
                int nameColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_NAME);
                int locationColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_LOCATION);
                int durationColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_DURATION);
                String name = cursor.getString(nameColumnIndex);
                String location = cursor.getString(locationColumnIndex);
                String duration = cursor.getString(durationColumnIndex);
                event = new Event(name, location, duration);
                eventsListSQLite.add(event);
                cursor.moveToNext();
                System.out.println(event.toString());
            }

            // Retrive Firebase data and store in an ArryList
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myFirebaseRef = database.getReference("events");
            myFirebaseRef.orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot newSnapshot : dataSnapshot.getChildren()) {

                        String nameTest = newSnapshot.child("event").getValue().toString();
                        String locationTest = newSnapshot.child("location").getValue().toString();
                        String durationTest = newSnapshot.child("duration").getValue().toString();
                        Event eventTest = new Event(nameTest, locationTest, durationTest);
                        eventItemListFirebase.add(eventTest);
                    }

                    System.out.println("List from Firebase: " + Arrays.toString(eventItemListFirebase.toArray()));

                    // Find the RecyclerView which will be populated with the product data
                    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
                    mDynamicListAdapter = new DynamicListAdapter();
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    // Set the adapter
                    mRecyclerView.setAdapter(mDynamicListAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        System.out.println("List from SQLite :" + Arrays.toString(eventsListSQLite.toArray()));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // Inner dynamic adapter to show both the lists in the RecyclerView
    private class DynamicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int FOOTER_VIEW = 1;
        private static final int FIRST_LIST_ITEM_VIEW = 2;
        private static final int FIRST_LIST_HEADER_VIEW = 3;
        private static final int SECOND_LIST_ITEM_VIEW = 4;
        private static final int SECOND_LIST_HEADER_VIEW = 5;

        public DynamicListAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v;

            if (viewType == FOOTER_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_footer, parent, false);
                FooterViewHolder vh = new FooterViewHolder(v);
                return vh;

            } else if (viewType == FIRST_LIST_ITEM_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                FirstListItemViewHolder vh = new FirstListItemViewHolder(v);
                return vh;

            } else if (viewType == FIRST_LIST_HEADER_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sqlite_header, parent, false);
                FirstListHeaderViewHolder vh = new FirstListHeaderViewHolder(v);
                return vh;

            } else if (viewType == SECOND_LIST_HEADER_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_firebase_header, parent, false);
                SecondListHeaderViewHolder vh = new SecondListHeaderViewHolder(v);
                return vh;

            } else {
                // SECOND_LIST_ITEM_VIEW
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_firebase, parent, false);
                SecondListItemViewHolder vh = new SecondListItemViewHolder(v);
                return vh;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            try {
                if (holder instanceof SecondListItemViewHolder) {
                    SecondListItemViewHolder vh = (SecondListItemViewHolder) holder;
                    vh.bindViewSecondList(position);

                } else if (holder instanceof FirstListHeaderViewHolder) {
                    FirstListHeaderViewHolder vh = (FirstListHeaderViewHolder) holder;

                } else if (holder instanceof FirstListItemViewHolder) {
                    FirstListItemViewHolder vh = (FirstListItemViewHolder) holder;
                    vh.bindViewFirstList(position);

                } else if (holder instanceof SecondListHeaderViewHolder) {
                    SecondListHeaderViewHolder vh = (SecondListHeaderViewHolder) holder;

                } else if (holder instanceof FooterViewHolder) {
                    FooterViewHolder vh = (FooterViewHolder) holder;
                    vh.bindViewFooter(position);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {

            int firstListSize = 0;
            int secondListSize = 0;

            if (eventItemListFirebase == null && eventsListSQLite == null) return 0;

            if (eventItemListFirebase != null)
                secondListSize = eventItemListFirebase.size();
            if (eventsListSQLite != null)
                firstListSize = eventsListSQLite.size();

            if (secondListSize > 0 && firstListSize > 0)
                return 1 + firstListSize + 1 + secondListSize + 1;   // first list header, first list size, second list header , second list size, footer
            else if (secondListSize > 0 && firstListSize == 0)
                return 1 + secondListSize + 1;                       // second list header, second list size, footer
            else if (secondListSize == 0 && firstListSize > 0)
                return 1 + firstListSize;                            // first list header , first list size
            else return 0;
        }

        @Override
        public int getItemViewType(int position) {

            int firstListSize = 0;
            int secondListSize = 0;

            if (eventItemListFirebase == null && eventsListSQLite == null)
                return super.getItemViewType(position);

            if (eventItemListFirebase != null)
                secondListSize = eventItemListFirebase.size();
            if (eventsListSQLite != null)
                firstListSize = eventsListSQLite.size();

            if (secondListSize > 0 && firstListSize > 0) {
                if (position == 0) return FIRST_LIST_HEADER_VIEW;
                else if (position == firstListSize + 1)
                    return SECOND_LIST_HEADER_VIEW;
                else if (position == secondListSize + 1 + firstListSize + 1)
                    return FOOTER_VIEW;
                else if (position > firstListSize + 1)
                    return SECOND_LIST_ITEM_VIEW;
                else return FIRST_LIST_ITEM_VIEW;

            } else if (secondListSize > 0 && firstListSize == 0) {
                if (position == 0) return SECOND_LIST_HEADER_VIEW;
                else if (position == secondListSize + 1) return FOOTER_VIEW;
                else return SECOND_LIST_ITEM_VIEW;

            } else if (secondListSize == 0 && firstListSize > 0) {
                if (position == 0) return FIRST_LIST_HEADER_VIEW;
                else return FIRST_LIST_ITEM_VIEW;
            }

            return super.getItemViewType(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView nameTextView;
            private TextView locationTextView;
            private TextView durationTextView;

            private TextView nameTextViewFirebase;
            private TextView locationTextViewFirebase;
            private TextView durationTextViewFirebase;

            // Element of footer view
            private TextView footerTextView;


            public ViewHolder(View itemView) {
                super(itemView);
                nameTextView = (TextView) itemView.findViewById(R.id.name);
                locationTextView = (TextView) itemView.findViewById(R.id.location);
                durationTextView = (TextView) itemView.findViewById(R.id.duration);

                nameTextViewFirebase = (TextView) itemView.findViewById(R.id.name_firebase);
                locationTextViewFirebase = (TextView) itemView.findViewById(R.id.location_firebase);
                durationTextViewFirebase = (TextView) itemView.findViewById(R.id.duration_firebase);

                // Get the view of the footer elements
                footerTextView = (TextView) itemView.findViewById(R.id.footer);

            }

            public void bindViewSecondList(int pos) {

                if (eventsListSQLite == null) pos = pos - 1;
                else {
                    if (eventsListSQLite.size() == 0) pos = pos - 1;
                    else pos = pos - eventsListSQLite.size() - 2;
                }

                final String name = eventItemListFirebase.get(pos).getName();
                final String location = eventItemListFirebase.get(pos).getLocation();
                final String duration = eventItemListFirebase.get(pos).getDuration();

                nameTextViewFirebase.setText(name);
                locationTextViewFirebase.setText(location);
                durationTextViewFirebase.setText(duration);
            }

            public void bindViewFirstList(int pos) {

                // Decrease pos by 1 as there is a header view now.
                pos = pos - 1;

                System.out.println("first: " + pos);
                final String name = eventsListSQLite.get(pos).getName();
                final String location = eventsListSQLite.get(pos).getLocation();
                final String duration = eventsListSQLite.get(pos).getDuration();

                nameTextView.setText(name);
                locationTextView.setText(location);
                durationTextView.setText(duration);

            }

            public void bindViewFooter(int pos) {

            }
        }

        public class FooterViewHolder extends ViewHolder {
            public FooterViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class FirstListHeaderViewHolder extends ViewHolder {
            public FirstListHeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class FirstListItemViewHolder extends ViewHolder {
            public FirstListItemViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class SecondListHeaderViewHolder extends ViewHolder {
            public SecondListHeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class SecondListItemViewHolder extends ViewHolder {
            public SecondListItemViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}


