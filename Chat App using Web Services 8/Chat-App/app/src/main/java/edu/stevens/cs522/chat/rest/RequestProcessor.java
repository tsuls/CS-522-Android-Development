package edu.stevens.cs522.chat.rest;

import android.content.Context;
import android.net.Uri;
import android.os.ResultReceiver;

import edu.stevens.cs522.chat.async.QueryBuilder;
import edu.stevens.cs522.chat.settings.Settings;

/**
 * Created by dduggan.
 */

public class RequestProcessor {

    private Context context;

    private RestMethod restMethod;

    public RequestProcessor(Context context) {
        this.context = context;
        this.restMethod = new RestMethod(context);
    }

    public Response process(Request request) {
        return request.process(this);
    }

    public Response perform(RegisterRequest request) {
        Response response = restMethod.perform(request);
        if (response instanceof RegisterResponse) {
            // TODO update the user name and sender id in settings, updated peer record PK
            Settings.saveSenderId(context, request.senderId);
        }
        return response;
    }

    public Response perform(PostMessageRequest request) {
        // TODO insert the message into the local database
        Response response = restMethod.perform(request);
        if (response instanceof PostMessageResponse) {
            // TODO update the message in the database with the sequence numbe
        }
        return response;
    }

}
