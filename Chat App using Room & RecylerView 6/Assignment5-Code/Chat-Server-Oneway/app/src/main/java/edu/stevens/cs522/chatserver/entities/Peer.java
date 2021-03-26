package edu.stevens.cs522.chatserver.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;

/**
 * Created by dduggan.
 */

// TODO
@Entity(indices = {@Index(value = {"name"}, unique = true)})
public class Peer implements Parcelable {

    // TODO
    @PrimaryKey
    public long id;

    @ColumnInfo
    public String name;

    // Last time we heard from this peer.
    @ColumnInfo
    public Date timestamp;

    // Where we heard from them
    @ColumnInfo
    public Double latitude;

    @ColumnInfo
    public Double longitude;

    // Where they sent message from
    @ColumnInfo
    public InetAddress address;

    @ColumnInfo
    public int port;

    @Override
    public String toString() {
        return name;
    }

    public Peer() {
    }

    public Peer(Parcel in) {
        // TODO
        id = in.readLong();
        name = in.readString();
        timestamp = (java.util.Date) in.readSerializable();
        port = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address =  (java.net.InetAddress) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // TODO
        out.writeLong(id);
        out.writeString(name);
        out.writeSerializable(timestamp);
        out.writeInt(port);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeSerializable(address);
    }

    public static final Creator<Peer> CREATOR = new Creator<Peer>() {

        @Override
        public Peer createFromParcel(Parcel source) {
            // TODO
            return new Peer(source);
        }

        @Override
        public Peer[] newArray(int size) {
            // TODO
            return new Peer[size];
        }

    };
}
