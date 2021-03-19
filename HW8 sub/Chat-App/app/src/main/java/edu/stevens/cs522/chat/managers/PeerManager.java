package edu.stevens.cs522.chat.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import edu.stevens.cs522.chat.async.AsyncContentResolver;
import edu.stevens.cs522.chat.async.IContinue;
import edu.stevens.cs522.chat.async.IEntityCreator;
import edu.stevens.cs522.chat.async.IQueryListener;
import edu.stevens.cs522.chat.async.IQueryListener;
import edu.stevens.cs522.chat.contracts.PeerContract;
import edu.stevens.cs522.chat.entities.Peer;


/**
 * Created by dduggan.
 */

public class PeerManager extends Manager<Peer> {

    private static final int LOADER_ID = 2;

    private static final IEntityCreator<Peer> creator = new IEntityCreator<Peer>() {
        @Override
        public Peer create(Cursor cursor) {
            return new Peer(cursor);
        }
    };

    public PeerManager(Context context) {
        super(context, creator, LOADER_ID);
    }

    public void getAllPeersAsync(IQueryListener<Peer> listener) {
        // TODO use QueryBuilder to complete this
        executeQuery(PeerContract.CONTENT_URI, listener);
    }

    public void getPeerAsync(final long id, final IContinue<Cursor> callback) {
        // TODO need to check that peer is not null (not in database)
        getAsyncResolver().queryAsync(PeerContract.CONTENT_URI, null, PeerContract._ID + "=?", new String[]{id+""}, null, callback);
    }

    public void persistAsync(Peer peer, final IContinue<Long> callback) {
        // TODO need to ensure the peer is not already in the database
        ContentValues values = new ContentValues();
        peer.writeToProvider(values);
        getAsyncResolver().insertAsync(PeerContract.CONTENT_URI, values, new IContinue<Uri>() {
            @Override
            public void kontinue(Uri uri) {
                callback.kontinue(PeerContract.getId(uri));
            }
        });
    }

    public long persist(Peer peer) {
        // TODO synchronous version that executes on background thread (in service)
        ContentValues contentValues = new ContentValues();
        peer.writeToProvider(contentValues);
        getSyncResolver().insert(PeerContract.CONTENT_URI,contentValues);
        return peer.id;
    }

}
