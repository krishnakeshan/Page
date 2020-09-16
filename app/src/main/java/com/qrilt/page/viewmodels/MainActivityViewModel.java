package com.qrilt.page.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qrilt.page.model.RemoteHost;
import com.qrilt.page.utils.MainRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    // Properties
    private MainRepository mainRepository;

    // data
    private MutableLiveData<RemoteHost> selectedRemoteHost;
    private LiveData<List<RemoteHost>> remoteHosts;

    // Constructors
    public MainActivityViewModel(Application application) {
        super(application);
        mainRepository = new MainRepository(application);
        selectedRemoteHost = new MutableLiveData<>();
        remoteHosts = mainRepository.getAllRemoteHosts();

        if (remoteHosts.getValue() != null && !remoteHosts.getValue().isEmpty()) {
            selectedRemoteHost.setValue(remoteHosts.getValue().get(0));
        }
    }

    // Methods
    // get currently selected RemoteHost
    public LiveData<RemoteHost> getSelectedRemoteHost() {
        if (selectedRemoteHost == null) {
            selectedRemoteHost = new MutableLiveData<>();
        }
        return selectedRemoteHost;
    }

    // set a RemoteHost to selected
    public void setSelectedRemoteHost(RemoteHost remoteHost) {
        selectedRemoteHost.setValue(remoteHost);
    }

    // get all remote hosts
    public LiveData<List<RemoteHost>> getRemoteHosts() {
        return remoteHosts;
    }

    // add a remote host
    public void addRemoteHost(String name, String host, int port) {
        RemoteHost remoteHost = RemoteHost.forHostPort(host, port);
        remoteHost.setName(name);
        mainRepository.insertRemoteHost(remoteHost);
    }

    // update a remote host
    public void updateRemoteHost(RemoteHost remoteHost) {
        mainRepository.updateRemoteHost(remoteHost);
    }
}
