package com.qrilt.page.utils;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.qrilt.page.dao.RemoteHostDao;
import com.qrilt.page.model.RemoteHost;

import java.util.List;

public class MainRepository {
    // DAOs
    private MainDatabase mainDatabase;
    private RemoteHostDao remoteHostDao;

    // data
    private LiveData<List<RemoteHost>> allRemoteHosts;

    // constructors
    public MainRepository(Application application) {
        mainDatabase = MainDatabase.getInstance(application);
        remoteHostDao = mainDatabase.remoteHostDao();
        allRemoteHosts = remoteHostDao.getAll();
    }

    // accessors
    public LiveData<List<RemoteHost>> getAllRemoteHosts() {
        return allRemoteHosts;
    }

    public void insertRemoteHost(RemoteHost remoteHost) {
        MainDatabase.databaseWriteExecutor.execute(() -> {
            remoteHostDao.insert(remoteHost);
        });
    }

    public void updateRemoteHost(RemoteHost remoteHost) {
        MainDatabase.databaseWriteExecutor.execute(() -> {
            remoteHostDao.update(remoteHost);
        });
    }
}
