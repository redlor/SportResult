package com.example.android.sportresult;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android.sportresult.data.EventContract.EventEntry;


/**
 * Created by Hp on 13/10/2017.
 */

public class AddEventFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final int EXISTING_EVENT_LOADER = 0;
    private EditText mNameEditText;
    private EditText mLocationEditText;
    private EditText mDurationEditText;
    private Uri mCurrentProductUri;
    private RadioButton mSQLiteRadio;
    private RadioButton mFirebaseRadio;
    private Button mAddButton;
    FrameLayout addFrame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_event_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        addFrame = (FrameLayout) rootView.findViewById(R.id.frame_container_add);
        addFrame.setVisibility(View.VISIBLE);
        final Intent intent =  getActivity().getIntent();
        mCurrentProductUri = intent.getData();

        mNameEditText = (EditText) rootView.findViewById(R.id.edit_event_name);
        mLocationEditText = (EditText) rootView.findViewById(R.id.edit_event_location);
        mDurationEditText = (EditText) rootView.findViewById(R.id.edit_event_duration);
        mSQLiteRadio = (RadioButton) rootView.findViewById(R.id.sqlite_radio_button);
        mFirebaseRadio = (RadioButton) rootView.findViewById(R.id.firebase_radio_button);
        mAddButton = (Button) rootView.findViewById(R.id.button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

        if (mCurrentProductUri != null) {
            getLoaderManager().initLoader(EXISTING_EVENT_LOADER, null, this);
        }
        return rootView;

    }

    private void saveEvent() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String locationString = mLocationEditText.getText().toString().trim();
        String durationString = mDurationEditText.getText().toString().trim();

        // Create a ContentValues object
        ContentValues values = new ContentValues();
if (mSQLiteRadio.isChecked()) {
    if (TextUtils.isEmpty(nameString)) {
        Toast.makeText(getActivity(), R.string.name_needed_text, Toast.LENGTH_SHORT).show();
        return;
    } else {
        values.put(EventEntry.COLUMN_EVENT_NAME, nameString);
    }

    if (TextUtils.isEmpty(locationString)) {
        Toast.makeText(getActivity(), R.string.location_needed_text, Toast.LENGTH_SHORT).show();
        return;
    } else {
        values.put(EventEntry.COLUMN_EVENT_LOCATION, locationString);
    }

    if (TextUtils.isEmpty(durationString)) {
        Toast.makeText(getActivity(), R.string.duration_needed_text, Toast.LENGTH_SHORT).show();
        return;
    } else {
        values.put(EventEntry.COLUMN_EVENT_DURATION, durationString);
    }

    Uri newUri = getActivity().getContentResolver().insert(EventEntry.CONTENT_URI, values);

    if (newUri == null) {
        Toast.makeText(getActivity(), getString(R.string.editor_insert_event_failed),
                Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(getActivity(), getString(R.string.editor_insert_event_successful),
                Toast.LENGTH_SHORT).show();
    }
} else if (mFirebaseRadio.isChecked()) {

} else {
    Toast.makeText(getActivity(), getString(R.string.incomplete_event),
            Toast.LENGTH_SHORT).show();
}

     /*   Fragment fragment = new ListEventsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_container_add, fragment).addToBackStack(null);
        fragmentTransaction.commit();
       fragmentTransaction.addToBackStack(null);*/

        addFrame.setVisibility(View.INVISIBLE);

    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                EventEntry._ID,
                EventEntry.COLUMN_EVENT_NAME,
                EventEntry.COLUMN_EVENT_LOCATION,
                EventEntry.COLUMN_EVENT_DURATION};

        return new android.support.v4.content.CursorLoader(getActivity(),
                mCurrentProductUri,
                projection,
                null,
                null,
                null );

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            // Find the columns of event attributes
            int nameColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_NAME);
            int locationColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_LOCATION);
            int durationColumnIndex = cursor.getColumnIndex(EventEntry.COLUMN_EVENT_DURATION);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String location = cursor.getString(locationColumnIndex);
            String duration =  cursor.getString(durationColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mLocationEditText.setText(location);
            mDurationEditText.setText(duration);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mNameEditText.setText("");
        mLocationEditText.setText("");
        mDurationEditText.setText("");
    }

       public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
