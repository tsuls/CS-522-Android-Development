package edu.stevens.cs522.chat.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

/**
 * Created by dduggan.
 */

public class MessageContract extends BaseContract {

    public static final Uri CONTENT_URI = CONTENT_URI(AUTHORITY, "Message");

    public static final Uri CONTENT_URI(long id) {
        return CONTENT_URI(Long.toString(id));
    }

    public static final Uri CONTENT_URI(String id) {
        return withExtendedPath(CONTENT_URI, id);
    }

    public static final String CONTENT_PATH = CONTENT_PATH(CONTENT_URI);

    public static final String CONTENT_PATH_ITEM = CONTENT_PATH(CONTENT_URI("#"));


    public static final String ID = _ID;

    public static final String SEQUENCE_NUMBER = "sequence_number";

    public static final String MESSAGE_TEXT = "message_text";

    public static final String CHAT_ROOM = "chat_room";

    public static final String TIMESTAMP = "timestamp";

    public static final String LATITUDE = "latitude";

    public static final String LONGITUDE = "longitude";

    public static final String SENDER = "sender";

    public static final String SENDER_ID = "sender_id";

    public static final String FOREIGN_KEY = "peer_fk";

    public static final String[] COLUMNS = {ID, SEQUENCE_NUMBER, MESSAGE_TEXT, CHAT_ROOM, TIMESTAMP, LATITUDE, LONGITUDE, SENDER, SENDER_ID};

    // TODO remaining columns in Messages table


    private int sequenceNumberColumn = -1;
    private static int fkColumn = -1;
    private static int IDColumn = -1;

    public String getSequenceNumber(Cursor cursor) {
        if (sequenceNumberColumn < 0) {
            sequenceNumberColumn = cursor.getColumnIndexOrThrow(SEQUENCE_NUMBER);
        }
        return cursor.getString(sequenceNumberColumn);
    }

    public void putSequenceNumberColumn(ContentValues out, String messageText) {
        out.put(SEQUENCE_NUMBER, messageText);
    }

    private int messageTextColumn = -1;

    public String getMessageText(Cursor cursor) {
        if (messageTextColumn < 0) {
            messageTextColumn = cursor.getColumnIndexOrThrow(MESSAGE_TEXT);
        }
        return cursor.getString(messageTextColumn);
    }

    public static void putMessageText(ContentValues out, String messageText) {
        out.put(MESSAGE_TEXT, messageText);
    }

    // TODO remaining getter and putter operations for other columns
    public static Date getTimeStamp(Cursor cursor) {
        return new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TIMESTAMP)));
    }

    public static int getFK(Cursor cursor) {
        if (fkColumn < 0) {
            fkColumn = cursor.getColumnIndexOrThrow(FOREIGN_KEY);
        }
        return cursor.getInt(fkColumn);
    }

    public static void putFK(ContentValues out, int fk) {
        out.put(FOREIGN_KEY, fk);
    }

    public static void putTimestamp(ContentValues contentValues, Date timeStamp) {
        contentValues.put(TIMESTAMP, timeStamp.getTime());
    }

    public static String getSender(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(SENDER));
    }

    public static void putSender(ContentValues contentValues, String sender) {
        contentValues.put(SENDER, sender);
    }

    public static long getSenderId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(SENDER_ID));
    }

    public static void putSenderId(ContentValues values, Long senderId) {
        values.put(SENDER_ID, senderId);
    }

    public static Double getLongitude(Cursor cursor){
        return cursor.getDouble(cursor.getColumnIndexOrThrow(LONGITUDE));
    }

    public static void putLongitude(ContentValues values, Double longitude){
        values.put(LONGITUDE, longitude);
    }

    public static Double getLatitude(Cursor cursor){
        return cursor.getDouble(cursor.getColumnIndexOrThrow(LATITUDE));
    }

    public static void putLatitude(ContentValues values, Double latitude){
        values.put(LATITUDE, latitude);
    }

    public static long getSeqNum(Cursor cursor){
        return cursor.getInt(cursor.getColumnIndexOrThrow(SEQUENCE_NUMBER));
    }

    public static void putSeqNum(ContentValues values, long messageSequenceNumber){
        values.put(SEQUENCE_NUMBER, messageSequenceNumber);
    }

    public static String getChatRoom(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(CHAT_ROOM));
    }

    public static void putChatRoom(ContentValues values, String chatRoom){
        values.put(CHAT_ROOM, chatRoom);
    }
    public static void putID(ContentValues out, long id){out.put(ID, id);}

    public static long getID(Cursor cursor) {
        if (IDColumn < 0) {
            IDColumn = cursor.getColumnIndexOrThrow(ID);
        }
        return cursor.getInt(IDColumn);
    }
}
