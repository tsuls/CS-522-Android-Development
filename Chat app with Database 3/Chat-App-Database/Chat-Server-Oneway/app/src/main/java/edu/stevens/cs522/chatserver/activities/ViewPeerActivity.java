package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.databases.ChatDbAdapter;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class  ViewPeerActivity extends Activity {

    public static final String PEER_ID_KEY = "peer-id";

    private ChatDbAdapter chatDbAdapter;

    private SimpleCursorAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        chatDbAdapter = new ChatDbAdapter(this);
        chatDbAdapter.open();

        long peerId = getIntent().getLongExtra(PEER_ID_KEY, -1);
        if (peerId < 0) {
            throw new IllegalArgumentException("Expected peer id as intent extra");
        }
        Peer thePeer = chatDbAdapter.fetchPeer(peerId);
        Cursor messagesCursor = chatDbAdapter.fetchMessagesFromPeer(thePeer);
        startManagingCursor(messagesCursor);
        ListView messages = findViewById(R.id.view_messages);

//        System.out.println(messagesCursor.getString(messagesCursor.getColumnIndex(MessageContract.MESSAGE_TEXT)));
        messagesAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_2, messagesCursor, new String[]{
                MessageContract.SENDER, MessageContract.MESSAGE_TEXT
        }, new int[] {android.R.id.text1, android.R.id.text2});

        messages.setAdapter(messagesAdapter);

        // TODO init the UI
        TextView userName = findViewById(R.id.view_user_name);
        TextView timeStamp = findViewById(R.id.view_timestamp);
        TextView address = findViewById(R.id.view_address);
        TextView port = findViewById(R.id.view_port);
        TextView latitude = findViewById(R.id.view_latitude);
        TextView longitude = findViewById(R.id.view_longitude);


        userName.setText(thePeer.name);
        timeStamp.setText(thePeer.timestamp.toString());
        address.setText(thePeer.address.toString());
        port.setText(String.format(Locale.US,"%d", thePeer.port));
        latitude.setText(String.format(Locale.US,"%f", thePeer.latitude));
        longitude.setText(String.format(Locale.US,"%f", thePeer.longitude));



    }

}
