package edu.stevens.cs522.chatserver.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Created by dduggan.
 */

// TODO annotate (including FK constraints)
@Entity(foreignKeys= @ForeignKey(entity=Peer.class, onDelete=ForeignKey.CASCADE, parentColumns="id", childColumns="senderId"))
public class Message implements Parcelable {

    // TODO
    @PrimaryKey(autoGenerate =  true)
    public long id;

    @ColumnInfo
    public String chatRoom;

    @ColumnInfo
    public String messageText;

    @ColumnInfo
    public Date timestamp;

    @ColumnInfo
    public Double latitude;

    @ColumnInfo
    public Double longitude;

    @ColumnInfo
    public String sender;

    @ColumnInfo
    public long senderId;

    @ColumnInfo
    public long fk;

    public Message() {
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
    public String toString() {
        return messageText;
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

