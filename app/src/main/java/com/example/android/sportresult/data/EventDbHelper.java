package com.example.android.sportresult.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.sportresult.data.EventContract.EventEntry;
/**
 * Created by Hp on 13/10/2017.
 */

public class EventDbHelper extends SQLiteOpenHelper {

    // Name of the database file
    private static final String DATABASE_NAME = "inventory.db";

    // Database version
    private static final int DATABASE_VERSION = 1;

    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the table
        String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " + EventEntry.TABLE_NAME + " ("
                + EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EventEntry.COLUMN_EVENT_NAME + " TEXT NOT NULL, "
                + EventEntry.COLUMN_EVENT_LOCATION + " TEXT NOT NULL, "
                + EventEntry.COLUMN_EVENT_DURATION + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
