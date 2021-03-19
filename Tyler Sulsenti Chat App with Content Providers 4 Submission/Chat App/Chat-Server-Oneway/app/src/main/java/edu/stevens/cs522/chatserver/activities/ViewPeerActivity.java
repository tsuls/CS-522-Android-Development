package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Locale;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.contracts.PeerContract;
import edu.stevens.cs522.chatserver.entities.Peer;
import edu.stevens.cs522.chatserver.providers.ChatProvider;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String PEER_KEY = "peer";

    private static final int LOADER_ID = 3;

    private SimpleCursorAdapter messagesAdapter;
    private ListView viewMessages;

    private static long peerId;
    private static String peerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);
        viewMessages = findViewById(R.id.view_messages);

        Peer peer = getIntent().getParcelableExtra(PEER_KEY);
        if (peer == null) {
            throw new IllegalArgumentException("Expected peer as intent extra");
        }
        String[] from = new String[] {MessageContract.MESSAGE_TEXT, MessageContract.TIMESTAMP};
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        messagesAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_2, null, from, to, 0);
        viewMessages.setAdapter(messagesAdapter);

        // TODO init the UI
        TextView userName = findViewById(R.id.view_user_name);
        TextView timeStamp = findViewById(R.id.view_timestamp);
        TextView address = findViewById(R.id.view_address);
        TextView port = findViewById(R.id.view_port);
        TextView latitude = findViewById(R.id.view_latitude);
        TextView longitude = findViewById(R.id.view_longitude);


        userName.setText(peer.name);
        timeStamp.setText(peer.timestamp.toString());
        address.setText(peer.address.toString());
        port.setText(String.format(Locale.US,"%d", peer.port));
        latitude.setText(String.format(Locale.US,"%f", peer.latitude));
        longitude.setText(String.format(Locale.US,"%f", peer.longitude));
        peerId = peer.id;
        peerName = peer.name;
        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        // TODO use a CursorLoader to initiate a query on the database
        // Filter messages with the sender id
        String selection = "(" + MessageContract.SENDER + "='" + peerName + "')";
        return new CursorLoader(this, MessageContract.CONTENT_URI(ChatProvider.MESSAGES_SINGLE_ROW), null, selection, null, null);
    }

        @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        // TODO populate the UI with the result of querying the provider
        messagesAdapter.swapCursor(data);
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // TODO reset the UI when the cursor is empty
        messagesAdapter.swapCursor(null);
        messagesAdapter.notifyDataSetChanged();
    }

}
