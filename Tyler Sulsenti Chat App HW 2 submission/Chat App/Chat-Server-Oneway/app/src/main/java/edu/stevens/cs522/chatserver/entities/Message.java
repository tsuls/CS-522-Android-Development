package edu.stevens.cs522.chatserver.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import edu.stevens.cs522.base.DateUtils;

/**
 * Created by dduggan.
 */

public class Message implements Parcelable {

    public long id;

    public String chatRoom;

    public String messageText;

    public Date timestamp;

    public Double latitude;

    public Double longitude;

    public String sender;

    public long senderId;

    public Message(){
    }
    public Message(Parcel in){
        id = in.readLong();
        chatRoom = in.readString();
        messageText = in.readString();
        timestamp = DateUtils.readDate(in);
        latitude = in.readDouble();
        longitude = in.readDouble();
        sender = in.readString();
        senderId = in.readLong();
    }

    @Override
    public String toString() {
        return sender + ":" + messageText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO
        dest.writeLong(id);
        dest.writeString(chatRoom);
        DateUtils.writeDate(dest, timestamp);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(sender);
        dest.writeLong(senderId);
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

