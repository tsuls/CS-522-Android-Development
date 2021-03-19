package edu.stevens.cs522.chat.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.util.Date;

import edu.stevens.cs522.chat.contracts.PeerContract;
import edu.stevens.cs522.chat.entities.Persistable;

/**
 * Created by dduggan.
 */

public class Peer implements Parcelable, Persistable {

    // Will be database key
    public long id;

    public String name;

    // Last time we heard from this peer.
    public Date timestamp;

    // Where we heard from them
    public InetAddress address;

    public Peer() {
    }

    public Peer(Cursor cursor) {
        // TODO
        id = cursor.getLong(cursor.getColumnIndex(PeerContract._ID));
        name = cursor.getString(cursor.getColumnIndex(PeerContract.NAME));
        timestamp = new Date(cursor.getLong(cursor.getColumnIndex(PeerContract.TIMESTAMP)));
        try {
            String addressString = cursor.getString(cursor.getColumnIndex(PeerContract.ADDRESS)).substring(1);
            address = InetAddress.getByName(addressString);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Peer(Parcel in) {
        // TODO
        id = in.readLong();
        name = in.readString();
        timestamp = (java.util.Date) in.readSerializable();
        address =  (java.net.InetAddress) in.readSerializable();
    }

    @Override
    public void writeToProvider(ContentValues out) {
        // TODO
        PeerContract.putId(out, id);
        PeerContract.putName(out, name);
        PeerContract.putTimestamp(out, timestamp.getTime());
        PeerContract.putAddres(out, address.toString());
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
