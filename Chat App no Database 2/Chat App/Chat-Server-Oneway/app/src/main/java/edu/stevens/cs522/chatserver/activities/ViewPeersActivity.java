package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.entities.Peer;


public class ViewPeersActivity extends Activity implements AdapterView.OnItemClickListener {

    public static final String PEERS_KEY = "peers";

    private ArrayList<Peer> peers = new ArrayList<Peer>();
    private ArrayList<String> peerByName;
    private ListView peerView;
    private ArrayAdapter peerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peers);

        peers = getIntent().getParcelableArrayListExtra(PEERS_KEY);
       // peerByName = loadArrayList(peers);


       peerAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,peers);

        // TODO display the list of peers, set this activity as onClick listener

        peerView = findViewById(R.id.peer_list);
        peerView.setAdapter(peerAdapter);
        peerView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
         * Clicking on a peer brings up details
         */
        Peer peer = peers.get(position);
        Intent intent = new Intent(this, ViewPeerActivity.class);
        intent.putExtra(ViewPeerActivity.PEER_KEY, peer);
        startActivity(intent);
    }

    private ArrayList<String> loadArrayList(ArrayList<Peer> peers) {
        ArrayList<String> names = new ArrayList<String>();
        for (Peer p : peers) {
            String peerByName = p.name;
            names.add(peerByName);
        }
        return names;
    }
}
