package edu.stevens.cs522.chat.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import edu.stevens.cs522.chat.async.IContinue;
import edu.stevens.cs522.chat.async.IEntityCreator;
import edu.stevens.cs522.chat.async.IQueryListener;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.contracts.PeerContract;
import edu.stevens.cs522.chat.entities.Message;


/**
 * Created by dduggan.
 */

public class MessageManager extends Manager<Message> {

    private static final int LOADER_ID = 1;

    private static final IEntityCreator<Message> creator = new IEntityCreator<Message>() {
        @Override
        public Message create(Cursor cursor) {
            return new Message(cursor);
        }
    };


    public MessageManager(Context context) {
        super(context, creator, LOADER_ID);
    }

    public void getAllMessagesAsync(IQueryListener<Message> listener) {
        // TODO use QueryBuilder to complete this
        executeQuery(MessageContract.CONTENT_URI, listener);
    }

    public void persistAsync(Message message) {
        // TODO
        ContentValues contentValues = new ContentValues();
        message.writeToProvider(contentValues);
        getAsyncResolver().insertAsync(MessageContract.CONTENT_URI, contentValues, new IContinue<Uri>() {
            @Override
            public void kontinue(Uri uri) {
                //callback.kontinue(PeerContract.getId(uri));
            }
        });
    }

    public long persist(Message message) {
        // Synchronous version, executed on background thread
        ContentValues contentValues = new ContentValues();
        message.writeToProvider(contentValues);
        getSyncResolver().insert(MessageContract.CONTENT_URI, contentValues);
        return message.id;
    }


}
