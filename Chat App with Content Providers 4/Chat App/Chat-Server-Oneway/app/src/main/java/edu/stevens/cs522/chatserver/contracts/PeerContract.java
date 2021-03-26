package edu.stevens.cs522.chatserver.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.InetAddress;

import static edu.stevens.cs522.chatserver.contracts.BaseContract.withExtendedPath;

/**
 * Created by dduggan.
 */

public class PeerContract extends BaseContract {

    public static final Uri CONTENT_URI = CONTENT_URI(AUTHORITY, "Peer");

    public static final Uri CONTENT_URI(long id) {
        return CONTENT_URI(Long.toString(id));
    }

    public static final Uri CONTENT_URI(String id) {
        return withExtendedPath(CONTENT_URI, id);
    }

    public static final String PORT = "Port";

    public static final String LATITUDE = "Latitude";

    public static final String LONGITUDE = "Longitude";

    public static final String CONTENT_PATH = CONTENT_PATH(CONTENT_URI);

    public static final String CONTENT_PATH_ITEM = CONTENT_PATH(CONTENT_URI("#"));

    public static final String NAME = "name";

    public static final String TIMESTAMP = "timestamo";

    public static final String ADDRESS = "address";


    // TODO define column names, getters for cursors, setters for contentvalues
    private static int nameColumn = -1;
    private static int timestampColumn = -1;
    private static int addressColumn = -1;
    private static int portColumn = -1;
    private static int latitudeColumn = -1;
    private static int longitudeColumn = -1;
    private static int IDColumn = -1;



    public static String getName(Cursor cursor) {
        if (nameColumn < 0) {
            nameColumn = cursor.getColumnIndexOrThrow(NAME);
        }
        return cursor.getString(nameColumn);
    }

    public static void putName(ContentValues out, String name) {
        out.put(NAME, name);
    }

    public static Long getTimestamp(Cursor cursor) {
        if(timestampColumn < 0) {
            timestampColumn = cursor.getColumnIndexOrThrow(TIMESTAMP);
        }
        return cursor.getLong(timestampColumn);
    }

    public static void putTimestamp(ContentValues out, long timestamp) {
        out.put(TIMESTAMP, timestamp);
    }

    public static String getAddress(Cursor cursor) {
        if(addressColumn < 0) {
            addressColumn = cursor.getColumnIndexOrThrow(ADDRESS);
        }
        return cursor.getString(addressColumn);
    }

    public static void putAddres(ContentValues out, String address) {
        out.put(ADDRESS, address);
    }
    public static long getId(Cursor cursor) {
        if (IDColumn < 0) {
            IDColumn = cursor.getColumnIndexOrThrow(_ID);
        }
        return cursor.getInt(IDColumn);
    }

    public static void putId(ContentValues out, long id) {
        out.put(_ID, id);
    }

    public static String getPort(Cursor cursor) {
        if (portColumn < 0) {
            portColumn = cursor.getColumnIndexOrThrow(PORT);
        }
        return cursor.getString(nameColumn);
    }

    public static void putPort(ContentValues out, int name) {
        out.put(PORT, name);
    }

    public static String getLatitude(Cursor cursor) {
        if (latitudeColumn < 0) {
            latitudeColumn = cursor.getColumnIndexOrThrow(LATITUDE);
        }
        return cursor.getString(latitudeColumn);
    }

    public static void putLatitude(ContentValues out, Double name) {
        out.put(LATITUDE, name);
    }

    public static String getLongitude(Cursor cursor) {
        if (longitudeColumn < 0) {
            longitudeColumn = cursor.getColumnIndexOrThrow(LONGITUDE);
        }
        return cursor.getString(longitudeColumn);
    }

    public static void putLongitude(ContentValues out, Double name) {
        out.put(LONGITUDE, name);
    }


}
