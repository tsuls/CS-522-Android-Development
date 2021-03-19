package edu.stevens.cs522.chat.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import edu.stevens.cs522.chat.contracts.MessageContract;

/**
 * Created by dduggan.
 */

public class ChatMessage implements Parcelable, Persistable {

    // Primary key in the database
    public long id;

    // Global id provided by the server
    public long seqNum;

    public String messageText;

    public String chatRoom;

    // When and where the message was sent
    public Date timestamp;

    public Double longitude;

    public Double latitude;

    // Sender username and FK (in local database)
    public String sender;

    public long senderId;

    public ChatMessage(){};

    public ChatMessage(Cursor cursor) {
        // TODO
        id = cursor.getLong(cursor.getColumnIndex(MessageContract._ID));
        messageText = cursor.getString(cursor.getColumnIndex(MessageContract.MESSAGE_TEXT));
        timestamp = new Date (cursor.getLong(cursor.getColumnIndex(MessageContract.TIMESTAMP)));
        sender = cursor.getString(cursor.getColumnIndex(MessageContract.SENDER));
        senderId = cursor.getLong(cursor.getColumnIndex(MessageContract.FOREIGN_KEY));
    }

    public ChatMessage(Parcel in) {
        // TODO
        id = in.readLong();
        messageText = in.readString();
        timestamp = (java.util.Date) in.readSerializable();
        sender = in.readString();
        senderId = in.readLong();
    }

    @Override
    public void writeToProvider(ContentValues out) {
        // TODO
        MessageContract.putID(out, id);
        MessageContract.putMessageText(out, messageText);
        MessageContract.putTimestamp(out, timestamp.getTime());
        MessageContract.putSender(out, sender);
        MessageContract.putFK(out, (int) senderId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO
        dest.writeLong(id);
        dest.writeString(messageText);
        dest.writeSerializable(timestamp);
        dest.writeString(sender);
        dest.writeLong(senderId);
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {

        @Override
        public ChatMessage createFromParcel(Parcel source) {
            // TODO
            return new ChatMessage(source);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            // TODO
            return new ChatMessage[size];
        }

    };

}
