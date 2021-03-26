package edu.stevens.cs522.chatserver.databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import edu.stevens.cs522.chatserver.entities.Peer;

/*
 * TODO add annotations (NB insert should ignore conflicts, for upsert)
 *
 * We will continue to allow insertion to be done on main thread for noew.
 */
@Dao
public abstract class PeerDAO {

    // TODO
    @Query("SELECT * FROM Peer")
    public abstract LiveData<List<Peer>> fetchAllPeers();

    /**
     * Add a peer record if it does not already exist;
     * update information if it is already defined.
     * https://stackoverflow.com/a/50736568
     */

    // TODO get peer record based on name
    @Query("SELECT * FROM peer WHERE name LIKE :name LIMIT 1")
    protected abstract long getPeerId(String name);

    // TODO insert, ignore conflicts
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract long insert(Peer peer);

    // TODO update
    @Update
    protected abstract void update(Peer peer);

    @Transaction
    /**
     * This operation must be transactional, to avoid race condition
     * between conflict on insert and update.
     */
    public long upsert(Peer peer) {
        peer.id = getPeerId(peer.name);
        if (peer.id == 0) {
            peer.id = insert(peer);
        } else {
            update(peer);
        }
        return peer.id;
    }
}
