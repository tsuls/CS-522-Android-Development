package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.databases.ChatDatabase;
import edu.stevens.cs522.chatserver.databases.PeerDAO;
import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;
import edu.stevens.cs522.chatserver.ui.TextAdapter;


public class ViewPeersActivity extends FragmentActivity implements TextAdapter.OnItemClickListener {

    /*
     * TODO See ChatServer for example of what to do, query peers database instead of messages database.
     */

    private ChatDatabase chatDatabase;

    private LiveData<List<Peer>> peers;

    private TextAdapter<Peer> peerAdapter;

    private RecyclerView peersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peers);

        peersList = findViewById(R.id.peer_list);

        peerAdapter = new TextAdapter<>(peersList, this);

        chatDatabase = ChatDatabase.getInstance(getApplicationContext());

        // TODO initialize peerAdapter with result of asynchronous DB query
        PeerDAO peerDao = chatDatabase.peerDao();

       // RecyclerView recyclerView = (RecyclerView)  peersList;
        peersList.setAdapter(peerAdapter);
        peersList.setLayoutManager(new LinearLayoutManager(this));

        peers = peerDao.fetchAllPeers();

        Observer<List<Peer>> observer = peer1 -> {
            System.out.println(peer1);
            peerAdapter.setDataset(peer1);
            peerAdapter.notifyDataSetChanged();
        };

        peers.observe(this, observer);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatDatabase.isOpen()) {
            chatDatabase.close();
            chatDatabase = null;
        }
    }

    /*
     * Callback interface defined in TextAdapter, for responding to clicks on rows.
     */
    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        /*
         * Clicking on a peer brings up details
         */
        Peer peer = peers.getValue().get(position);

        Intent intent = new Intent(this, ViewPeerActivity.class);
        intent.putExtra(ViewPeerActivity.PEER_KEY, peer);
        startActivity(intent);

    }
}
