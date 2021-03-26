package edu.stevens.cs522.chatserver.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import edu.stevens.cs522.chatserver.contracts.MessageContract;

/**
 * Created by dduggan.
 */

public class Message implements Parcelable, Persistable {

    public long id;

    public String messageText;

    public Date timestamp;

    public String sender;

    public long senderId;

    public String chatRoom;

    public double latitude;

    public double longitude;

    public long fk;

    public Message() {
    }

    public Message(Cursor cursor) {
        // TODO
        id = cursor.getLong(cursor.getColumnIndex(MessageContract._ID));
        messageText = cursor.getString(cursor.getColumnIndex(MessageContract.MESSAGE_TEXT));
        timestamp = new Date (cursor.getLong(cursor.getColumnIndex(MessageContract.TIMESTAMP)));
        sender = cursor.getString(cursor.getColumnIndex(MessageContract.SENDER));
        chatRoom = cursor.getString(cursor.getColumnIndex(MessageContract.CHAT_ROOM));
        latitude = cursor.getDouble(cursor.getColumnIndex(MessageContract.LATITUDE));
        longitude = cursor.getDouble(cursor.getColumnIndex(MessageContract.LONGITUDE));
        senderId = cursor.getLong(cursor.getColumnIndex(MessageContract.FOREIGN_KEY));
        fk = cursor.getInt(cursor.getColumnIndex(MessageContract.FOREIGN_KEY));
    }

    public Message(Parcel in) {
        // TODO
        id = in.readLong();
        messageText = in.readString();
        timestamp = (java.util.Date) in.readSerializable();
        sender = in.readString();
        chatRoom = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        senderId = in.readLong();
        fk = in.readInt();
    }

    @Override
    public void writeToProvider(ContentValues out) {
        // TODO
        MessageContract.putMessageText(out, messageText);
        MessageContract.putTimestamp(out, timestamp.getTime());
        MessageContract.putSender(out, sender);
        MessageContract.putSenderId(out, senderId);
        MessageContract.putChatRoom(out, chatRoom);
        MessageContract.putLatitude(out, latitude);
        MessageContract.putLongitude(out, longitude);
        MessageContract.putFK(out, (int) senderId);
        MessageContract.putFK(out, fk);
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
        dest.writeString(chatRoom);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeLong(senderId);
        dest.writeLong(fk);
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {

        @Override
        public Message createFromParcel(Parcel source) {
            // TODO
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            // TODO
            return new Message[size];
        }

    };

}

