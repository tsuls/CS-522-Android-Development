/*********************************************************************

    Chat server: accept chat messages from clients.
    
    Sender chatName and GPS coordinates are encoded
    in the messages, and stripped off upon receipt.

    Copyright (c) 2017 Stevens Institute of Technology

**********************************************************************/
package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.async.IContinue;
import edu.stevens.cs522.chat.async.IQueryListener;
import edu.stevens.cs522.chat.async.QueryBuilder;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.entities.Peer;
import edu.stevens.cs522.chat.managers.MessageManager;
import edu.stevens.cs522.chat.managers.PeerManager;
import edu.stevens.cs522.chat.managers.TypedCursor;
import edu.stevens.cs522.chat.rest.ChatHelper;
import edu.stevens.cs522.chat.rest.ServiceManager;
import edu.stevens.cs522.chat.services.ResultReceiverWrapper;
import edu.stevens.cs522.chat.settings.Settings;
import edu.stevens.cs522.chat.services.ResultReceiverWrapper;

public class ChatActivity extends Activity implements OnClickListener, IQueryListener<ChatMessage>, ResultReceiverWrapper.IReceive {

	final static public String TAG = ChatActivity.class.getCanonicalName();
		
    /*
     * UI for displaying received messages
     */
	private SimpleCursorAdapter messages;
	
	private ListView messageList;

    private SimpleCursorAdapter messagesAdapter;

    private MessageManager messageManager;

    private PeerManager peerManager;

    private ServiceManager serviceManager;

    /*
     * Widgets for dest address, message text, send button.
     */
    private EditText chatRoomName;

    private EditText messageText;

    private Button sendButton;


    /*
     * Helper for Web service
     */
    private ChatHelper helper;

    /*
     * For receiving ack when message is sent.
     */
    private ResultReceiverWrapper sendResultReceiver;
	
	/*
	 * Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.messages);
        messageList =  findViewById(R.id.message_list);
        messageText = findViewById(R.id.message_text);
        sendButton = findViewById(R.id.send_button);
        chatRoomName = findViewById(R.id.chat_room);
        sendButton.setOnClickListener(this);

        // TODO use SimpleCursorAdapter to display the messages received.
        String[] from = new String[] { MessageContract.SENDER, MessageContract.MESSAGE_TEXT };
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        messagesAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, from, to, 0);
        messageList = findViewById(R.id.message_list);
        messageList.setAdapter(messagesAdapter);


        // TODO create the message and peer managers, and initiate a query for all messages
        messageManager = new MessageManager(this);
        peerManager = new PeerManager(this);
        messageManager.getAllMessagesAsync(this);



        // TODO instantiate helper for service
        helper=new ChatHelper(this);

        // TODO initialize sendResultReceiver
        sendResultReceiver = new ResultReceiverWrapper(new Handler());


        // TODO (SYNC) initialize serviceManager
        serviceManager = new ServiceManager(this);

        /**
         * Initialize settings to default values.
         */
        if (!Settings.isRegistered(this)) {
            // TODO launch registration activity
            Settings.getAppId(this);
            startActivity(new Intent(this, RegisterActivity.class));
        }

    }

	public void onResume() {
        super.onResume();
        sendResultReceiver.setReceiver(this);
        if (Settings.SYNC) {
            serviceManager.scheduleBackgroundOperations();
        }
    }

    public void onPause() {
        super.onPause();
        sendResultReceiver.setReceiver(null);
        if (Settings.SYNC) {
            serviceManager.cancelBackgroundOperations();
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // TODO inflate a menu with PEERS and SETTINGS options
        getMenuInflater().inflate(R.menu.chatserver_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {

            // TODO PEERS provide the UI for viewing list of peers
            case R.id.peers:
                Intent intent = new Intent(this, ViewPeersActivity.class);
                startActivity(intent);
                break;

            // TODO PEERS provide the UI for registering
            case R.id.register:
                Intent intent2 = new Intent(this, RegisterActivity.class);
                startActivity(intent2);
                break;

            // TODO SETTINGS provide the UI for settings
            case R.id.settings:
                Intent intent3 = new Intent(this, SettingsActivity.class);
                startActivity(intent3);
                break;
            default:
        }
        return false;
    }



    /*
     * Callback for the SEND button.
     */
    public void onClick(View v) {
        if (helper != null) {

            final String chatRoom = chatRoomName.getText().toString();

            // TODO get chatRoom and message from UI, and use helper to post a message

            final String theMessage = messageText.getText().toString();

            //final ChatMessage message = new ChatMessage();
            final Random r = new Random();

            final ChatMessage message = new ChatMessage();
            message.sender = Settings.getChatName(this);
            message.timestamp = new Date();
            message.messageText = theMessage;

            Log.i(TAG, "Received from " + message.sender + ": " + message.messageText);

            final Peer sender = new Peer();
            sender.name = message.sender;
            sender.timestamp = message.timestamp;
            //sender.address = receivePacket.getAddress();
            sender.id = r.nextInt(10000);

            // TODO upsert the peer and message into the content provider.
            // For this assignment, must use managers to do this asynchronously

            peerManager.persistAsync(sender, new IContinue<Long>() {
                @Override
                public void kontinue(Long value) {
                    message.senderId = sender.id;
                    message.id = r.nextInt(10000);
                    messageManager.persistAsync(message);
                }
            });

            helper.postMessage(chatRoom, theMessage, sendResultReceiver);


            // End todo

            Log.i(TAG, "Sent message: " + message);

            messageText.setText("");
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode) {
            case RESULT_OK:
                // TODO show a success toast message
                Toast.makeText(this, "The message was successfully posted!.", Toast.LENGTH_LONG).show();
                break;
            default:
                // TODO show a failure toast message
                Toast.makeText(this, "The message post failed!!.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void handleResults(TypedCursor<ChatMessage> results) {
        // TODO

    }

    @Override
    public void closeResults() {
        // TODO
    }

}