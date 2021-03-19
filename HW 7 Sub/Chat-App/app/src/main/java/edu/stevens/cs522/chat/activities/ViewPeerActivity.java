package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.entities.Peer;
import edu.stevens.cs522.chat.managers.MessageManager;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity {

    public static final String PEER_KEY = "peer";

    private SimpleCursorAdapter messagesAdapter;

    private MessageManager messageManager;

    private SimpleCursorAdapter peerAdapter;

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

        // TODO init the UI
        TextView userName = findViewById(R.id.view_user_name);
        TextView timeStamp = findViewById(R.id.view_timestamp);
        TextView longitude = findViewById(R.id.view_longitude);
        TextView lat = findViewById(R.id.view_latitude);
        //TextView address = findViewById(R.id.v);
        ListView messages = findViewById(R.id.view_messages);

        userName.setText(peer.name);
        timeStamp.setText(peer.timestamp.toString());
        longitude.setText(peer.longi.toString());
        lat.setText(peer.lat.toString());
       // address.setText(peer.address.toString());
        messages.setAdapter(messagesAdapter);

        //  messageManager.getAllMessagesAsync(this);

    }

}
