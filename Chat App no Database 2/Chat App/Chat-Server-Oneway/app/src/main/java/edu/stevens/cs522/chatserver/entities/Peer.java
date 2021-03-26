package edu.stevens.cs522.chatserver.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ListView;

import java.net.InetAddress;
import java.util.Date;

import edu.stevens.cs522.base.DateUtils;
import edu.stevens.cs522.base.InetAddressUtils;

/**
 * Created by dduggan.
 */

public class Peer implements Parcelable {

    // Will be database key
    public long id;

    public String name;

    // Last time we heard from this peer.
    public Date timestamp;

    // Where we heard from them
    public Double latitude;

    public Double longitude;

    // Where they sent message from
    public InetAddress address;

    public int port;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // TODO
        out.writeLong(id);
        out.writeString(name);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        DateUtils.writeDate(out, timestamp);
        InetAddressUtils.writeAddress(out, address);
        out.writeInt(port);
    }

    public Peer() {
    }

    public Peer(Parcel in) {
        // TODO
        id = in.readLong();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        timestamp = DateUtils.readDate(in);
        address = InetAddressUtils.readAddress(in);
        port = in.readInt();
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
