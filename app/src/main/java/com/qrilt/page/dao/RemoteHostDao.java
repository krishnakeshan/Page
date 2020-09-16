package com.qrilt.page.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.qrilt.page.model.RemoteHost;

import java.util.List;

@Dao
public interface RemoteHostDao {
    @Query("SELECT * FROM remote_hosts")
    LiveData<List<RemoteHost>> getAll();

    // Insert
    @Insert
    void insert(RemoteHost remoteHost);

    @Insert
    void insertAll(RemoteHost... remoteHosts);

    // Update
    @Update
    void update(RemoteHost remoteHost);

    // Delete
    @Delete
    void delete(RemoteHost remoteHost);

    @Delete
    void deleteAll(RemoteHost... remoteHosts);
}
