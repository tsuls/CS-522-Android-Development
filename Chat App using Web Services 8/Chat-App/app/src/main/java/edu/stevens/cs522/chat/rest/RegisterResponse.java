package edu.stevens.cs522.chat.rest;

import android.net.Uri;
import android.os.Parcel;

import java.io.IOException;
import java.net.HttpURLConnection;

import edu.stevens.cs522.chat.settings.Settings;

/**
 * Created by dduggan.
 */

public class RegisterResponse extends Response {

    private final static String LOCATION = "Location";
    private final static String ID = "X-Response-Id";

    private long senderId;

    public RegisterResponse(HttpURLConnection connection) throws IOException {
        super(connection);
        // TODO String location = connection.getHeaderField(LOCATION);
        String location = connection.getHeaderField(LOCATION);
        //String id = connection.getHeaderField(ID);

        if (location != null) {
            Uri uri = Uri.parse(location);
            senderId = Long.parseLong((uri.getLastPathSegment()));
        }

       /* if(id != null){
            senderId = Long.parseLong(id);
        }
        else{
            System.out.println("Check ID response header");
        }*/

    }

    @Override
    public boolean isValid() { return true; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(senderId);
    }

    public RegisterResponse(Parcel in) {
        super(in);
        in.writeLong(senderId);
    }

    public static Creator<RegisterResponse> CREATOR = new Creator<RegisterResponse>() {
        @Override
        public RegisterResponse createFromParcel(Parcel source) {
            return new RegisterResponse(source);
        }

        @Override
        public RegisterResponse[] newArray(int size) {
            return new RegisterResponse[size];
        }
    };
}
