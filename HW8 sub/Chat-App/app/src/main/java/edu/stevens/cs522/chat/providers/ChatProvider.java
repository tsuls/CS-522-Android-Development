package edu.stevens.cs522.chat.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import edu.stevens.cs522.chat.contracts.BaseContract;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.contracts.PeerContract;
import edu.stevens.cs522.chat.entities.Peer;

public class ChatProvider extends ContentProvider {

    public ChatProvider() {
    }

    private static final String AUTHORITY = BaseContract.AUTHORITY;

    private static final String MESSAGE_CONTENT_PATH = MessageContract.CONTENT_PATH;

    private static final String MESSAGE_CONTENT_PATH_ITEM = MessageContract.CONTENT_PATH_ITEM;

    private static final String PEER_CONTENT_PATH = PeerContract.CONTENT_PATH;

    private static final String PEER_CONTENT_PATH_ITEM = PeerContract.CONTENT_PATH_ITEM;


    private static final String DATABASE_NAME = "chat2333.db";

    private static final int DATABASE_VERSION = 1;

    private static final String MESSAGES_TABLE = "messages";

    private static final String PEERS_TABLE = "view_peers";

    // Create the constants used to differentiate between the different URI  requests.
    private static final int MESSAGES_ALL_ROWS = 1;
    private static final int MESSAGES_SINGLE_ROW = 2;
    private static final int PEERS_ALL_ROWS = 3;
    private static final int PEERS_SINGLE_ROW = 4;

    public static final String PEER_CREATE = "CREATE TABLE view_peers(" +
            PeerContract._ID + " INTEGER PRIMARY KEY, " +
            PeerContract.NAME + " TEXT, " +
            PeerContract.TIMESTAMP + " TEXT, " +
            PeerContract.LONGITUDE + " REAL, " +
            PeerContract.LATITUDE + " REAL" +
            ");";
    public static final String MESSAGE_CREATE = "CREATE TABLE messages(" +
            MessageContract.ID + " INTEGER PRIMARY KEY, " +
            MessageContract.MESSAGE_TEXT + " TEXT, " +
            MessageContract.SENDER_ID + " TEXT, " +
            MessageContract.CHAT_ROOM + " TEXT, " +
            MessageContract.TIMESTAMP + " TEXT, " +
            MessageContract.SENDER + " text, " +
            "peer_fk INTEGER NOT NULL, " +
            MessageContract.LONGITUDE + " REAL, " +
            MessageContract.LATITUDE + " REAL, " +
            MessageContract.SEQUENCE_NUMBER + " INTEGER, " +
            "FOREIGN KEY(peer_fk) REFERENCES view_peers(_id) ON DELETE CASCADE" +
            ");";

    private static final String MESSAGES_PEER_INDEX = "CREATE INDEX PeerNameIndex ON view_peers(name);";
    private static final String PEER_NAME_INDEX = "CREATE INDEX MessagesPeerIndex ON Messages(peer_fk);";

    public static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO initialize database tables
            db.execSQL(PEER_CREATE);
            db.execSQL(MESSAGE_CREATE);
            db.execSQL(PEER_NAME_INDEX);
            db.execSQL(MESSAGES_PEER_INDEX);
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO upgrade database if necessary
        }
    }

    private DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        // Initialize your content provider on startup.
        dbHelper = new DbHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        return true;
    }

    // Used to dispatch operation based on URI
    private static final UriMatcher uriMatcher;

    // uriMatcher.addURI(AUTHORITY, CONTENT_PATH, OPCODE)
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MESSAGE_CONTENT_PATH, MESSAGES_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, MESSAGE_CONTENT_PATH_ITEM, MESSAGES_SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, PEER_CONTENT_PATH, PEERS_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PEER_CONTENT_PATH_ITEM, PEERS_SINGLE_ROW);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        if (uriMatcher.match(uri) == MESSAGES_ALL_ROWS) {
            return "vmd.android.cursor.dir/vnd." + MessageContract.AUTHORITY + "." + MESSAGES_TABLE;
        }
        else if (uriMatcher.match(uri) == PEERS_ALL_ROWS) {
            return "vmd.android.cursor.dir/vnd." + MessageContract.AUTHORITY + "." + PEERS_TABLE;
        }
        else if (uriMatcher.match(uri) == MESSAGES_SINGLE_ROW) {
            return "vmd.adroid.cursor.dir/vmd." + MessageContract.AUTHORITY + "." + MESSAGES_TABLE;
        }
        else if (uriMatcher.match(uri) == PEERS_SINGLE_ROW) {
            return  "vmd.android.cursor.dir/vmd." + MessageContract.AUTHORITY + "." + PEERS_TABLE;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALL_ROWS:
                // TODO: Implement this to handle requests to insert a new message.
                // Make sure to notify any observers
                long mId = db.insert(MESSAGES_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, mId);
            case PEERS_ALL_ROWS:
                // TODO: Implement this to handle requests to insert a new peer.
                // Make sure to notify any observers
                long pId = db.insert(PEERS_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, pId);
            default:
                throw new IllegalStateException("insert: bad case");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALL_ROWS:
                // TODO: Implement this to handle query of all messages.
                Cursor cursorMesagesAllRows = db.query(MESSAGES_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursorMesagesAllRows.setNotificationUri(getContext().getContentResolver(), uri);
                return cursorMesagesAllRows;
            case PEERS_ALL_ROWS:
                // TODO: Implement this to handle query of all peers.
                Cursor cursorPeersAllRows = db.query(PEERS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursorPeersAllRows.setNotificationUri(getContext().getContentResolver(), uri);
                return cursorPeersAllRows;
            case MESSAGES_SINGLE_ROW:
                // TODO: Implement this to handle query of a specific message.
                Cursor cursorMessagesSingleRow = db.query(MESSAGES_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursorMessagesSingleRow.setNotificationUri(getContext().getContentResolver(), uri);
                return cursorMessagesSingleRow;
            case PEERS_SINGLE_ROW:
                // TODO: Implement this to handle query of a specific peer.
                Cursor cursorPeerSingleRow = db.query(PEERS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursorPeerSingleRow.setNotificationUri(getContext().getContentResolver(), uri);
                return cursorPeerSingleRow;
            default:
                throw new IllegalStateException("insert: bad case");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Implement this to handle requests to update one or more rows.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numUpdate = 0;
        if(uriMatcher.match(uri) == MESSAGES_ALL_ROWS || uriMatcher.match(uri) == MESSAGES_SINGLE_ROW){
            numUpdate = db.update(MESSAGES_TABLE, values, selection, selectionArgs);
        }
        else if(uriMatcher.match(uri) == PEERS_ALL_ROWS || uriMatcher.match(uri) == PEERS_SINGLE_ROW){
            numUpdate = db.update(PEERS_TABLE, values, selection, selectionArgs);
        }
        return numUpdate;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numDel = 0;
        if(uriMatcher.match(uri) == MESSAGES_ALL_ROWS || uriMatcher.match(uri) == MESSAGES_SINGLE_ROW){
            numDel = db.delete(MESSAGES_TABLE, selection, selectionArgs);
        }
        else if(uriMatcher.match(uri) == PEERS_ALL_ROWS || uriMatcher.match(uri) == PEERS_SINGLE_ROW){
            numDel = db.delete(PEERS_TABLE, selection, selectionArgs);
        }
        return numDel;
    }

}
