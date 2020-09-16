package com.qrilt.page.utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.qrilt.page.dao.RemoteHostDao;
import com.qrilt.page.model.RemoteHost;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {RemoteHost.class}, version = 1)
public abstract class MainDatabase extends RoomDatabase {
    public abstract RemoteHostDao remoteHostDao();

    private static volatile MainDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MainDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (MainDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MainDatabase.class, "main-database").build();
                }
            }
        }
        return INSTANCE;
    }
}
