/*********************************************************************

 Chat server: accept chat messagesAdapter from clients.

 Sender name and GPS coordinates are encoded
 in the messagesAdapter, and stripped off upon receipt.

 Copyright (c) 2017 Stevens Institute of Technology

 **********************************************************************/
package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;

import edu.stevens.cs522.base.DatagramSendReceive;
import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.databases.ChatDbAdapter;
import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;

public class ChatServer extends Activity implements OnClickListener {

    public final static String TAG = ChatServer.class.getCanonicalName();

    public final static String SENDER_NAME = "name";

    public final static String CHATROOM = "room";

    public final static String MESSAGE_TEXT = "text";

    public final static String TIMESTAMP = "timestamp";

    public final static String LATITUDE = "latitude";

    public final static String LONGITUDE = "longitude";

    /*
     * Socket used both for sending and receiving
     */
    // private DatagramSocket serverSocket;
    private DatagramSendReceive serverSocket;

    /*
     * True as long as we don't get socket errors
     */
    private boolean socketOK = true;

    private ArrayList<Peer> peers;

    /*
     * TODO: Declare a listview for messagesAdapter, and an adapter for displaying messagesAdapter.
     */

    private ListView messageList;

    private SimpleCursorAdapter messagesAdapter;

    private ChatDbAdapter chatDbAdapter;

    private Button next;
    /*
     * End Todo
     */


    /*
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.view_messages);

        /**
         * Let's be clear, this is a HACK to allow you to do network communication on the view_messages thread.
         * This WILL cause an ANR, and is only provided to simplify the pedagogy.  We will see how to do
         * this right in a future assignment (using a Service managing background threads).
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            /*
             * Get port information from the resources.
             */
            int port = getResources().getInteger(R.integer.app_port);

            // serverSocket = new DatagramSocket(port);

            serverSocket = new DatagramSendReceive(port);

        } catch (Exception e) {
            throw new IllegalStateException("Cannot open socket", e);
        }

        // List of peers
        peers = new ArrayList<Peer>();


        setContentView(R.layout.messages);
        messageList = findViewById(R.id.message_list);
        next = findViewById(R.id.next);
        next.setOnClickListener(this);

        // TODO open the database using the database adapter
        chatDbAdapter = new ChatDbAdapter(this);
        chatDbAdapter.open();


        // TODO query the database using the database adapter, and manage the cursor on the messages thread
        Cursor cursor = chatDbAdapter.fetchAllMessages();
        startManagingCursor(cursor);


        // TODO use SimpleCursorAdapter to display the messages received.
        messagesAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_2, cursor, new String[]{
                MessageContract.SENDER, MessageContract.MESSAGE_TEXT
        }, new int[] {android.R.id.text1, android.R.id.text2});
        messageList.setAdapter(messagesAdapter);

        startManagingCursor(messagesAdapter.getCursor());


        // TODO bind the button for "next" to this activity as listener
       // next.setOnClickListener(this);

    }

    public void onClick(View v) {

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        try {


            serverSocket.receive(receivePacket);
            Log.d(TAG, "Received a packet");

            InetAddress address = receivePacket.getAddress();
            int port = receivePacket.getPort();
            Log.d(TAG, "Source IP Address: " + address + " , Port: " + port);

            String content = new String(receivePacket.getData(), 0, receivePacket.getLength());
            Log.d(TAG, "Message received: " + content);


            /*
             * Parse the JSON object
             */
            String sender = null;

            String room = null;

            String text = null;

            Date timestamp = null;

            Double latitude = null;

            Double longitude = null;

            JsonReader rd = new JsonReader(new StringReader(content));

            Log.d(TAG, "before json read");

            rd.beginObject();
            if (SENDER_NAME.equals(rd.nextName())) {
                sender = rd.nextString();
            }
            if (CHATROOM.equals(rd.nextName())) {
                room = rd.nextString();
            }
            if (MESSAGE_TEXT.equals((rd.nextName()))) {
                text = rd.nextString();
            }
            if (TIMESTAMP.equals(rd.nextName())) {
                timestamp = new Date(rd.nextLong());
            }
            if (LATITUDE.equals(rd.nextName())) {
                latitude = rd.nextDouble();
            }
            if (LONGITUDE.equals((rd.nextName()))) {
                longitude = rd.nextDouble();
            }
            rd.endObject();

            rd.close();

            Log.d(TAG, "before json close");


            /*
             * Add the sender to our list of senders
             */
            Peer peer = new Peer();
            peer.name = sender;
            peer.address = address;
            peer.port = port;
            peer.timestamp = timestamp;
            peer.latitude = latitude;
            peer.longitude = longitude;
            peer.id = chatDbAdapter.persist(peer);
            addPeer(peer);

            Log.d(TAG, "after peer add");

            Message message = new Message();
            message.messageText = text;
            message.chatRoom = room;
            message.sender = sender;
            message.timestamp = timestamp;
            message.latitude = latitude;
            message.longitude = longitude;
            message.senderId = peer.id;
            message.fk = peer.id;

            message.id = chatDbAdapter.persist(message);

            Log.d(TAG, "after message constrcution");

            messagesAdapter.getCursor().requery();
            messagesAdapter.notifyDataSetChanged();

        } catch (Exception e) {

            Log.e(TAG, "Problems receiving packet: ", e);
            socketOK = false;
        }

    }

    /*
     * Close the socket before exiting application
     */
    public void closeSocket() {
        if (serverSocket != null) {
            serverSocket.close();
            serverSocket = null;
        }
    }

    /*
     * If the socket is OK, then it's running
     */
    boolean socketIsOK() {
        return socketOK;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeSocket();
    }

    private void addPeer(Peer peer) {
        for (Peer p : peers) {
            if (p.name.equals(peer.name)) {
                p.address = peer.address;
                p.port = peer.port;
                p.timestamp = peer.timestamp;
                p.latitude = peer.latitude;
                p.longitude = peer.longitude;
                return;
            }
        }
        peers.add(peer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // TODO inflate a menu with PEERS option
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatserver_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {

            case R.id.peers:
                // TODO PEERS provide the UI for viewing list of peers
                // Send the list of peers to the subactivity as a parcelable list
                Intent intent = new Intent(this, ViewPeersActivity.class);
                intent.putParcelableArrayListExtra(ViewPeersActivity.PEERS_KEY, peers);
                startActivity(intent);
                break;

            default:
        }
        return false;
    }


}