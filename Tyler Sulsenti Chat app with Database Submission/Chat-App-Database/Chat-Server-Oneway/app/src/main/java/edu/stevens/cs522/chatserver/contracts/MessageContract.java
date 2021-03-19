package edu.stevens.cs522.chatserver.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by dduggan.
 */

public class MessageContract implements BaseColumns {

    public static final String MESSAGE_TEXT = "message_text";

    public static final String TIMESTAMP = "timestamp";

    public static final String SENDER = "sender";

    public static final String SENDER_ID = "sender_ID";

    public static final String FOREIGN_KEY = "peer_fk";

    public static final String CHAT_ROOM = "Chat_Room";

    public static final String LATITUDE = "Latitude";

    public static final String LONGITUDE = "Longitude";

    // TODO remaining columns in Messages table

    private static int messageTextColumn = -1;
    private static int timestampColumn = -1;
    private static int senderColumn = -1;
    private static int senderIDColumn = - 1;
    private static int fkColumn = -1;
    private static int chatRoomColumn = -1;
    private static int latitudeColumn = -1;
    private static int longitudeColumn = -1;

    public static String getMessageText(Cursor cursor) {
        if (messageTextColumn < 0) {
            messageTextColumn = cursor.getColumnIndexOrThrow(MESSAGE_TEXT);
        }
        return cursor.getString(messageTextColumn);
    }

    public static void putMessageText(ContentValues out, String messageText) {
        out.put(MESSAGE_TEXT, messageText);
    }

    public static String getChatRoom(Cursor cursor) {
        if (chatRoomColumn < 0) {
            chatRoomColumn = cursor.getColumnIndexOrThrow(CHAT_ROOM);
        }
        return cursor.getString(chatRoomColumn);
    }

    public static void putChatRoom(ContentValues out, String messageText) {
        out.put(CHAT_ROOM, messageText);
    }

    public static String getLatitude(Cursor cursor) {
        if (latitudeColumn < 0) {
            latitudeColumn = cursor.getColumnIndexOrThrow(LATITUDE);
        }
        return cursor.getString(latitudeColumn);
    }

    public static void putLatitude(ContentValues out, Double messageText) {
        out.put(LATITUDE, messageText);
    }

    public static String getLongitude(Cursor cursor) {
        if (longitudeColumn < 0) {
            longitudeColumn = cursor.getColumnIndexOrThrow(LONGITUDE);
        }
        return cursor.getString(longitudeColumn);
    }

    public static void putLongitude(ContentValues out, Double messageText) {
        out.put(LONGITUDE, messageText);
    }

    // TODO remaining getter and putter operations for other columns
    public static Long getTimestamp(Cursor cursor) {
        if(timestampColumn < 0) {
            timestampColumn = cursor.getColumnIndexOrThrow(TIMESTAMP);
        }
        return cursor.getLong(timestampColumn);
    }

    public static void putTimestamp(ContentValues out, long timestamp) {
        out.put(TIMESTAMP, timestamp);
    }

    public static String getSender(Cursor cursor) {
        if(senderColumn < 0) {
            senderColumn = cursor.getColumnIndexOrThrow(SENDER);
        }
        return cursor.getString(senderColumn);
    }

    public static void putSender(ContentValues out, String sender){
        out.put(SENDER, sender);
    }

    public static long getSenderId(Cursor cursor) {
        if(senderIDColumn < 0) {
            senderIDColumn = cursor.getColumnIndexOrThrow(SENDER_ID);
        }
        return cursor.getLong(senderIDColumn);
    }

    public static void putSenderId(ContentValues out, long senderId){
        out.put(SENDER_ID, senderId);
    }

    public static int getFK(Cursor cursor) {
        if (fkColumn < 0) {
            fkColumn = cursor.getColumnIndexOrThrow(FOREIGN_KEY);
        }
        return cursor.getInt(fkColumn);
    }

    public static void putFK(ContentValues out, long fk) {
        out.put(FOREIGN_KEY, fk);
    }
}
