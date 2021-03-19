package edu.stevens.cs522.chat.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.util.Date;

import edu.stevens.cs522.chat.contracts.PeerContract;

/**
 * Created by dduggan.
 */

public class Peer implements Parcelable, Persistable {

    public long id;

    public String name;

    // Last time we heard from this peer.
    public Date timestamp;

    public Double longitude;

    public Double latitude;

    public Peer() {
    }

    public Peer(Cursor cursor) {
        // TODO
        this.id = PeerContract.getId(cursor);
        this.name = PeerContract.getName(cursor);
        this.timestamp = PeerContract.getTimeStamp(cursor);
        this.longitude = PeerContract.getLongitude(cursor);
        this.latitude = PeerContract.getLatitude(cursor);
    }

    public Peer(Parcel in) {
        // TODO
        this.id = in.readLong();
        this.name = in.readString();
        this.timestamp = (Date) in.readSerializable();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
    }

    @Override
    public void writeToProvider(ContentValues out) {
        // TODO
        PeerContract.putName(out, this.name);
        PeerContract.putTimeStamp(out, this.timestamp);
        PeerContract.putLongitude(out, this.longitude);
        PeerContract.putLatitude(out, this.latitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // TODO
        out.writeLong(this.id);
        out.writeString(this.name);
        out.writeSerializable(this.timestamp);
        out.writeDouble(this.longitude);
        out.writeDouble(this.latitude);
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
