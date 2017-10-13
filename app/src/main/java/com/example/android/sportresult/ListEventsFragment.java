package com.example.android.sportresult;

import android.content.Context;
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

/**
 * Created by Hp on 13/10/2017.
 */

public class ListEventsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EVENT_LOADER = 0;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    EventCursorAdapter mCursorAdapter;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_events_fragments, container, false);

        // Find the RecyclerView which will be populated with the product data
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(getActivity());



        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set the adapter
        mCursorAdapter = new EventCursorAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(EVENT_LOADER, null, this);
        return view;
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
