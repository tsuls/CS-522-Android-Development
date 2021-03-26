package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.databases.ChatDatabase;
import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;
import edu.stevens.cs522.chatserver.ui.MessageAdapter;
import edu.stevens.cs522.chatserver.ui.TextAdapter;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends FragmentActivity {

    public static final String PEER_KEY = "peer";

    private ChatDatabase chatDatabase;

    private LiveData<List<Message>> messages;

    private TextAdapter<Message> messageAdapter;

    private RecyclerView messagesList;

    private Peer peer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        peer = getIntent().getParcelableExtra(PEER_KEY);
        System.out.println(peer.toString());
        if (peer == null) {
            throw new IllegalArgumentException("Expected peer id as intent extra");
        }

        messagesList = findViewById(R.id.view_messages);
        messageAdapter = new TextAdapter<Message>(messagesList, null);

        chatDatabase = ChatDatabase.getInstance(getApplicationContext());


        messagesList.setAdapter(messageAdapter);
        messagesList.setLayoutManager(new LinearLayoutManager(this));

        messages = chatDatabase.messageDao().fetchMessagesFromPeer(peer.id);

        Observer<List<Message>> observer = messages1 -> {
            System.out.println(messages1);
            messageAdapter.setDataset(messages1);
            messageAdapter.notifyDataSetChanged();
        };

        messages.observe(this, observer);


        // TODO init the UI
        TextView userName = findViewById(R.id.view_user_name);
        TextView timeStamp = findViewById(R.id.view_timestamp);
        TextView address = findViewById(R.id.view_address);
        TextView port = findViewById(R.id.view_port);
        TextView latitude = findViewById(R.id.view_latitude);
        TextView longitude = findViewById(R.id.view_longitude);


        userName.setText(peer.name);
        timeStamp.setText(peer.timestamp.toString());
        address.setText(peer.address.toString().replace("/", ""));
        port.setText(String.format(Locale.US,"%d", peer.port));
        latitude.setText(String.format(Locale.US,"%f", peer.latitude));
        longitude.setText(String.format(Locale.US,"%f", peer.longitude));


    }

}
