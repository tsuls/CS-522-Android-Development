package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.contracts.PeerContract;
import edu.stevens.cs522.chatserver.entities.Peer;


public class  ViewPeersActivity extends Activity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    /*
     * TODO See ChatServer for example of what to do, query peers database instead of messages database.
     */

    private SimpleCursorAdapter peerAdapter;
    private ListView peerView;

    static final private int LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peers);

        peerView = findViewById(R.id.peer_list);

        // TODO initialize peerAdapter with empty cursor (null)

        // TODO use SimpleCursorAdapter (with flags=0) to display the messages received.
        String[] from = new String[] {PeerContract.NAME, PeerContract.TIMESTAMP, PeerContract.ADDRESS};
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        peerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, from, to, 0);
        peerView.setAdapter(peerAdapter);

        // TODO use loader manager to initiate a query of the database
        getLoaderManager().initLoader(LOADER_ID, null, this);
        peerView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
         * Clicking on a peer brings up details
         */
        Cursor cursor = peerAdapter.getCursor();
        if (cursor.moveToPosition(position)) {
            Intent intent = new Intent(this, ViewPeerActivity.class);
            Peer peer = new Peer(cursor);
            intent.putExtra(ViewPeerActivity.PEER_KEY, peer);
            startActivity(intent);
        } else {
            throw new IllegalStateException("Unable to move to position in cursor: " + position);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        // TODO use a CursorLoader to initiate a query on the database
        switch (id){
            case LOADER_ID:
                return new CursorLoader(this, PeerContract.CONTENT_URI, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        // TODO populate the UI with the result of querying the provider
        peerAdapter.swapCursor(data);
        peerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // TODO reset the UI when the cursor is empty
        peerAdapter.swapCursor(null);
        peerAdapter.notifyDataSetChanged();
    }

}
