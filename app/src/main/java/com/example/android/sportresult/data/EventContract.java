package com.example.android.sportresult.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hp on 13/10/2017.
 */

final public class EventContract {

    // Name of the Content Provider
    public static final String CONTENT_AUTHORITY = "com.example.android.sportresult";
    // Base Uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Possible path
    public static final String PATH_EVENTS = "events";

    // To prevent someone from accidentally instantiating the contract class
    private EventContract() {
        throw new AssertionError("No instances for you!");
    }

    public static final class EventEntry implements BaseColumns {

        //The content URI to access the event data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EVENTS);

        // The MIME type of the CONTENT URI for a list of event
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;

        // The MIME type of the CONTENT URI for a single event
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;


        // Name of database table
        public final static String TABLE_NAME = "events";

        // Product's fields
        public final static String COLUMN_EVENT_NAME = "name";
        public final static String COLUMN_EVENT_LOCATION = "location";
        public final static String COLUMN_EVENT_DURATION = "duration";

    }

}
