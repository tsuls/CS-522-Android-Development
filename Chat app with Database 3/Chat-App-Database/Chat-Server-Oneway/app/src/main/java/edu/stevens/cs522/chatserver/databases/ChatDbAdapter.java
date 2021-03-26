package edu.stevens.cs522.chatserver.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;

import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.contracts.PeerContract;
import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class   ChatDbAdapter {

    private static final String DATABASE_NAME = "messages.db";

    private static final String MESSAGE_TABLE = "Messages";

    private static final String PEER_TABLE = "Peers";

    private static final int DATABASE_VERSION = 1;

    private static final String[] MESSAGES_PROJECTION = {MessageContract._ID, MessageContract.MESSAGE_TEXT, MessageContract.TIMESTAMP, MessageContract.SENDER, MessageContract.CHAT_ROOM, MessageContract.LATITUDE, MessageContract.LONGITUDE, MessageContract.FOREIGN_KEY};

    private static final String[] PEERS_PROJECTION = {PeerContract._ID, PeerContract.NAME, PeerContract.TIMESTAMP, PeerContract.ADDRESS, PeerContract.PORT, PeerContract.LATITUDE, PeerContract.LONGITUDE};

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;


    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String MESSAGES_CREATE = "CREATE TABLE " + MESSAGE_TABLE + " ("                                                 +
                MessageContract._ID +  " INTEGER PRIMARY KEY,"                            +
                MessageContract. MESSAGE_TEXT + " TEXT NOT NULL,"                         +
                MessageContract.TIMESTAMP + " TEXT NOT NULL,"                             +
                MessageContract.SENDER + " TEXT NOT NULL,"                                +
                MessageContract.SENDER_ID + " TEXT NOT NULL,"                             +
                MessageContract.FOREIGN_KEY +  " INTEGER NOT NULL,"                       +
                MessageContract.CHAT_ROOM + " TEXT NOT NULL,"                             +
                MessageContract.LATITUDE + " FLOAT NOT NULL,"                             +
                MessageContract.LONGITUDE + " FLOAT NOT NULL,"                            +
                "FOREIGN KEY (" + MessageContract.FOREIGN_KEY + ") REFERENCES Peers(" + PeerContract._ID + ") ON DELETE CASCADE"          +
                ")";

        private static final String PEERS_CREATE = "CREATE TABLE " + PEER_TABLE + " (" +
                PeerContract._ID + " INTEGER PRIMARY KEY,"  +
                PeerContract.NAME + " TEXT NOT NULL,"       +
                PeerContract.TIMESTAMP + " LONG NOT NULL,"  +
                PeerContract.ADDRESS + " TEXT NOT NULL,"    +
                PeerContract.PORT + " INTEGER NOT NULL,"    +
                PeerContract.LATITUDE + " FLOAT NOT NULL,"  +
                PeerContract.LONGITUDE + " FLOAT NOT NULL"  +
                ")";

        private static final String MESSAGES_PEER_INDEX = "CREATE INDEX MessagesPeerIndex ON Messages(" + MessageContract.FOREIGN_KEY + ")";
        private static final String PEER_NAME_INDEX = "CREATE INDEX PeerNameIndex ON Peers(" + PeerContract.NAME + ")";

        private static final String MESSAGES_DELETE = "DROP TABLE IF EXISTS " + MESSAGE_TABLE;
        private static final String PEERS_DELETE = "DROP TABLE IF EXISTS " + PEER_TABLE;


        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(PEERS_CREATE);
            db.execSQL(MESSAGES_CREATE);
            db.execSQL(MESSAGES_PEER_INDEX);
            db.execSQL(PEER_NAME_INDEX);
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
            db.execSQL(MESSAGES_DELETE);
            db.execSQL(PEERS_DELETE);
            onCreate(db);
        }
    }


    public ChatDbAdapter(Context _context) {
        dbHelper = new DatabaseHelper(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        // TODONlNo
        db = dbHelper.getWritableDatabase();
    }

    public Cursor fetchAllMessages() {
        // TODO
        return db.query(MESSAGE_TABLE, null, null, null, null, null, null);
    }

    public Cursor fetchAllPeers() {
        // TODO
        return db.query(PEER_TABLE, null, null, null, null, null, null);
    }

    public Peer fetchPeer(long peerId) {
        // TODO
        String selection = PeerContract._ID + "=" + peerId;
        Cursor cursor =  db.query(PEER_TABLE, PEERS_PROJECTION, selection, null, null, null, null);
        cursor.moveToFirst();
        Peer peer = new Peer(cursor);
        cursor.close();
        return peer;
    }

    public Cursor fetchMessagesFromPeer(Peer peer) {
        // TODO
        long theId = peer.id;
        String selection = MessageContract.FOREIGN_KEY + "=" + theId;

        return db.query(MESSAGE_TABLE, null, selection, null,null,null, null);
    }

    public long persist(Message message) throws SQLException {
        // TODO
        ContentValues cv = new ContentValues();
        message.writeToProvider(cv);
        return db.insert(MESSAGE_TABLE, null, cv);
    }

    /**
     * Add a peer record if it does not already exist; update information if it is already defined.
     */
    public long persist(Peer peer) throws SQLException {
        // TODO
        ContentValues cv = new ContentValues();
        peer.writeToProvider(cv);
        try {
            Peer thePeer = fetchPeer(peer.id);
            db.update(PEER_TABLE, cv, null, null); //will throw an exception if data is not found
            return thePeer.id;
        } catch (Exception e) {
            //Add since it does not exist
            return db.insert(PEER_TABLE, null, cv);
        }


    }

    public void close(){
        // TODO
        db.close();
    }
}