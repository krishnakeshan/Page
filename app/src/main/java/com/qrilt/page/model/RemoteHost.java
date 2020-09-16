package com.qrilt.page.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.qrilt.page.utils.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "remote_hosts")
public class RemoteHost {
    // Properties
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String host;
    public int port;

    @Ignore
    private boolean isOnline;
    @Ignore
    private Thread pingThread;
    @Ignore
    private DeviceStateChangedListener deviceStateChangedListener;
    @Ignore
    private APIClient apiClient;
    @Ignore
    private RemoteFileExplorer fileExplorer;

    // Constructors
    public RemoteHost() {
    }

    public static RemoteHost forHostPort(String host, int port) {
        return new RemoteHost(host, port);
    }

    private RemoteHost(String host, int port) {
        this.host = host;
        this.port = port;

        // create api client
        apiClient = APIClient.fromHostPort(host, port);

        // create file explorer
        fileExplorer = new RemoteFileExplorer(apiClient);
    }

    // Methods
    public void initDBInstance() {
        // setup apiclient
        if (apiClient == null)
            apiClient = APIClient.fromHostPort(host, port);

        // setup file explorer
        if (fileExplorer == null)
            fileExplorer = new RemoteFileExplorer(apiClient);

        // setup ping thread to query host continuously
        if (pingThread == null) {
            pingThread = new Thread() {
                public void run() {
                    try {
                        while (true) {
                            JSONObject request = new JSONObject();
                            request.put("message", "ping");
                            JSONObject response = new JSONObject(apiClient.sendMessageToRemote(request.toString()));
                            isOnline = response.getBoolean("success");

                            // notify any listeners
                            if (deviceStateChangedListener != null) {
                                deviceStateChangedListener.onDeviceStateChanged(isOnline);
                            }

                            // try again after 1 second
                            Thread.sleep(2000);
                        }
                    } catch (JSONException e) {
                        // handle invalid response
                        isOnline = false;
                    } catch (InterruptedException e) {
                        // handle sleep interruption
                        isOnline = false;
                    }
                }
            };
            pingThread.start();
        }
    }

    // Getters and Setters
    public RemoteFileExplorer getFileExplorer() {
        return fileExplorer;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeviceStateChangedListener(DeviceStateChangedListener listener) {
        deviceStateChangedListener = listener;
    }

    // Interfaces
    public interface DeviceStateChangedListener {
        void onDeviceStateChanged(boolean isOnline);
    }
}
