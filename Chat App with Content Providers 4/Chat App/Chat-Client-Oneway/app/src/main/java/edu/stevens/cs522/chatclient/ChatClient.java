/*********************************************************************

 Client for sending chat messages to the server..

 Copyright (c) 2012 Stevens Institute of Technology

 **********************************************************************/
package edu.stevens.cs522.chatclient;

import java.io.IOException;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import edu.stevens.cs522.base.DatagramSendReceive;
import edu.stevens.cs522.base.DateUtils;
import edu.stevens.cs522.base.InetAddressUtils;

/*
 * @author dduggan
 *
 */
public class ChatClient extends Activity implements OnClickListener {

	final static private String TAG = ChatClient.class.getCanonicalName();

	public final static String SENDER_NAME = "name";

	public final static String CHATROOM = "room";

	public final static String MESSAGE_TEXT = "text";

	public final static String TIMESTAMP = "timestamp";

	public final static String LATITUDE = "latitude";

	public final static String LONGITUDE = "longitude";

	/*
	 * Socket used for sending
	 */
//  private DatagramSocket clientSocket;
	private DatagramSendReceive clientSocket;

	/*
	 * Widgets for dest address, message text, send button.
	 */
	private EditText destinationHost;

	private EditText destinationPort;

	private EditText chatName;

	private EditText messageText;

	private Button sendButton;

	/*
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_client);

		/**
		 * Let's be clear, this is a HACK to allow you to do network communication on the chat_client thread.
		 * This WILL cause an ANR, and is only provided to simplify the pedagogy.  We will see how to do
		 * this right in a future assignment (using a Service managing background threads).
		 */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// TODO initialize the UI.
		destinationHost = findViewById(R.id.destination_host);
		destinationPort = findViewById(R.id.destination_port);
		chatName = findViewById(R.id.chat_name);
		messageText = findViewById(R.id.message_text);
		sendButton = findViewById(R.id.send_button);
		sendButton.setOnClickListener(this);
		// End todo

		try {

			int port = getResources().getInteger(R.integer.app_port);
			clientSocket = new DatagramSendReceive(port);
			// clientSocket = new DatagramSocket(port);

		} catch (IOException e) {
			IllegalStateException ex = new IllegalStateException("Cannot open socket");
			ex.initCause(e);
			throw ex;
		}

	}

	/*
	 * Callback for the SEND button.
	 */
	public void onClick(View v) {
		try {
			/*
			 * On the emulator, which does not support WIFI stack, we'll send to
			 * (an AVD alias for) the host loopback interface, with the server
			 * port on the host redirected to the server port on the server AVD.
			 */

			String destAddrString = null;

			int destPort = Integer.parseInt(destinationPort.getText().toString());//getResources().getInteger(R.integer.app_port);

			String clientName = null;

			String chatRoom = "_default";

			String text = null;

			Date timestamp = DateUtils.now();

			Double latitude = 44.523483;

			Double longitude = -89.574814;

			// TODO get data from UI (no-op if chat name is blank)
			destAddrString = destinationHost.getText().toString();
			clientName = chatName.getText().toString();
			text = messageText.getText().toString();
			// End todo

			if (destAddrString.isEmpty()) {
				return;
			}
			InetAddress destAddr = InetAddressUtils.fromString(destAddrString);

			if (clientName.isEmpty()) {
				return;
			}

			if (text.isEmpty()) {
				return;
			}

			Log.d(TAG, String.format("Sending data from address %s:%d", clientSocket.getInetAddress(), clientSocket.getPort()));

			StringWriter output = new StringWriter();
			JsonWriter wr = new JsonWriter(output);
			wr.beginObject();
			wr.name(SENDER_NAME).value(clientName);
			wr.name(CHATROOM).value(chatRoom);
			wr.name(MESSAGE_TEXT).value(text);
			wr.name(TIMESTAMP).value(timestamp.getTime());
			wr.name(LATITUDE).value(latitude);
			wr.name(LONGITUDE).value(longitude);
			wr.endObject();

			String content = output.toString();

			Log.d(TAG, "Message sent: "+content);

			byte[] sendData = content.getBytes();  // Default encoding is UTF-8

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destAddr, destPort);

			clientSocket.send(sendPacket);

			Log.d(TAG, "Sent packet!");


		} catch (UnknownHostException e) {
			throw new IllegalStateException("Unknown host exception: ", e);

		} catch (IOException e) {
			throw new IllegalStateException("IO exception: ", e);
		}

		messageText.setText("");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (clientSocket != null) {
			clientSocket.close();
		}
	}

}