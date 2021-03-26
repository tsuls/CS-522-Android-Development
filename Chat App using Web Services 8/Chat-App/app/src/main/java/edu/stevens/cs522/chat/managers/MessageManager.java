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
import edu.stevens.cs522.chat.entities.ChatMessage;


/**
 * Created by dduggan.
 */

public class MessageManager extends Manager<ChatMessage> {

    private static final int LOADER_ID = 1;

    private static final IEntityCreator<ChatMessage> creator = new IEntityCreator<ChatMessage>() {
        @Override
        public ChatMessage create(Cursor cursor) {
            return new ChatMessage(cursor);
        }
    };


    public MessageManager(Context context) {
        super(context, creator, LOADER_ID);
    }

    public void getAllChatMessagesAsync(IQueryListener<ChatMessage> listener) {
        // TODO use QueryBuilder to complete this
        executeQuery(MessageContract.CONTENT_URI, listener);
    }

    public void persistAsync(ChatMessage message, final IContinue<Long> callback) {
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

    public long persist(ChatMessage ChatMessage) {
        // Synchronous version, executed on background thread
        ContentValues contentValues = new ContentValues();
        ChatMessage.writeToProvider(contentValues);
        getSyncResolver().insert(MessageContract.CONTENT_URI, contentValues);
        return ChatMessage.id;
    }


}
