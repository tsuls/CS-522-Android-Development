package edu.stevens.cs522.chatserver.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import edu.stevens.cs522.chatserver.async.IContinue;
import edu.stevens.cs522.chatserver.async.IEntityCreator;
import edu.stevens.cs522.chatserver.async.IQueryListener;
import edu.stevens.cs522.chatserver.async.QueryBuilder;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.contracts.PeerContract;
import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;


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

    public void getMessagesByPeerAsync(Peer peer, IQueryListener<Message> listener) {
        // TODO use QueryBuilder to complete this
        // Remember to reset the loader!
        long theId = peer.id;
        String selection = MessageContract.FOREIGN_KEY + "=" + theId;
        executeQuery(MessageContract.CONTENT_URI, null, selection, null, null, listener);
    }

    public void persistAsync(Message message, final IContinue<Long> callback) {
        // TODO use AsyncContentResolver to complete this
        ContentValues contentValues = new ContentValues();
        message.writeToProvider(contentValues);
        getAsyncResolver().insertAsync(MessageContract.CONTENT_URI, contentValues, new IContinue<Uri>() {
            @Override
            public void kontinue(Uri uri) {
                callback.kontinue(PeerContract.getId(uri));
            }
        });
    }

}
