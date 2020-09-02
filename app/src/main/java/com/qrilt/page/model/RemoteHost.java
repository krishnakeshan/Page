package com.qrilt.page.model;

import com.qrilt.page.APIClient;

import java.util.ArrayList;
import java.util.List;

public class RemoteHost {
    // Properties
    private static List<RemoteHost> remoteHosts;

    private String host;
    private int port;
    private APIClient apiClient;
    private RemoteFileExplorer fileExplorer;

    // Constructors
    public static RemoteHost forHostPort(String host, int port) {
        // no instances created
        if (remoteHosts == null) {
            remoteHosts = new ArrayList<>();
            RemoteHost newHost = new RemoteHost(host, port);
            remoteHosts.add(newHost);
            return newHost;
        }

        // search and return
        for (RemoteHost remoteHost : remoteHosts) {
            if (remoteHost.host.equals(host) && remoteHost.port == port)
                return remoteHost;
        }

        // create new
        RemoteHost newHost = new RemoteHost(host, port);
        remoteHosts.add(newHost);
        return newHost;
    }

    private RemoteHost() {
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
    public boolean equals(RemoteHost another) {
        if (!another.host.equals(host))
            return false;
        return another.port == port;
    }

    // Getters
    public RemoteFileExplorer getFileExplorer() {
        return fileExplorer;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
