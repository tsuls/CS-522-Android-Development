package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.entities.Peer;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity {

    public static final String PEER_KEY = "peer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        Peer peer = getIntent().getParcelableExtra(PEER_KEY);
        if (peer == null) {
            throw new IllegalArgumentException("Expected peer as intent extra");
        }

        // TODO init the UI
        // TODO init the UI
        TextView userName = findViewById(R.id.view_user_name);
        TextView timeStamp = findViewById(R.id.view_timestamp);
        TextView longitude = findViewById(R.id.view_longitude);
        TextView lat = findViewById(R.id.view_latitude);
        //TextView address = findViewById(R.id.v);
        ListView messages = findViewById(R.id.view_messages);

        userName.setText(peer.name);
        timeStamp.setText(peer.timestamp.toString());
        longitude.setText(peer.longitude.toString());
        lat.setText(peer.latitude.toString());
        // address.setText(peer.address.toString());
       // messages.setAdapter(messagesAdapter);
    }

}
