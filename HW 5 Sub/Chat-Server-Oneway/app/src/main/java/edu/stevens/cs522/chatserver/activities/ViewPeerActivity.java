package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.async.IQueryListener;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;
import edu.stevens.cs522.chatserver.managers.MessageManager;
import edu.stevens.cs522.chatserver.managers.TypedCursor;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity implements IQueryListener<Message> {

    public static final String PEER_KEY = "peer";

    private SimpleCursorAdapter peerAdapter;

    private MessageManager messageManager;

    private SimpleCursorAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        Peer peer = getIntent().getParcelableExtra(PEER_KEY);
        if (peer == null) {
            throw new IllegalArgumentException("Expected peer as intent extra");
        }
        String[] from = new String[] {MessageContract.SENDER, MessageContract.MESSAGE_TEXT};
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        messagesAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, from, to, 0);
        peerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, null, null, 0);
        messageManager = new MessageManager(this);


        // TODO init the UI and initiate query of message database
        TextView userName = findViewById(R.id.view_user_name);
        TextView timeStamp = findViewById(R.id.view_timestamp);
        TextView address = findViewById(R.id.view_address);
        ListView messages = findViewById(R.id.view_messages);

        userName.setText(peer.name);
        timeStamp.setText(peer.timestamp.toString());
        address.setText(peer.address.toString());
        messages.setAdapter(messagesAdapter);

        messageManager.getMessagesByPeerAsync(peer,this);

    }

    @Override
    public void handleResults(TypedCursor<Message> results) {
        // TODO
        messagesAdapter.swapCursor(results.getCursor());
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeResults() {
        // TODO
        messagesAdapter.swapCursor(null);
        messagesAdapter.notifyDataSetChanged();
    }


}
