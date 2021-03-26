package edu.stevens.cs522.chatserver.databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import edu.stevens.cs522.chatserver.entities.Message;

// TODO add annotations for Repository pattern
@Dao
public interface MessageDAO {

    // TODO
    @Query("SELECT * FROM Message")
    public LiveData<List<Message>> fetchAllMessages();

    // TODO
    @Query("SELECT * FROM Message WHERE Message.fk = :peerId")
    public LiveData<List<Message>> fetchMessagesFromPeer(long peerId);

    // TODO
    @Insert
    public void persist(Message message);

}
