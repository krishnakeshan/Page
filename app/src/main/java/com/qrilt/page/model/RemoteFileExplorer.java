package com.qrilt.page.model;

import com.qrilt.page.utils.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

public class RemoteFileExplorer {
    // Properties
    private APIClient apiClient;
    private RemoteFile currentDirectory;

    // Constructors
    public RemoteFileExplorer() {
    }

    public RemoteFileExplorer(APIClient apiClient) {
        this.apiClient = apiClient;
        init();
    }

    // Methods
    void init() {
        try {
            // get current directory name
            JSONObject request = new JSONObject();
            request.put("message", "getCurrentDirectory");
            JSONObject response = new JSONObject(apiClient.sendMessageToRemote(request.toString()));
            currentDirectory = new RemoteFile();
            currentDirectory.isDir = true;
            currentDirectory.name = response.getJSONObject("cwd").getString("name");

            // get contents
            syncCurrentDirectoryContents();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean changeCurrentDirectory(RemoteFile destination) {
        try {
            // create request
            JSONObject request = new JSONObject();
            request.put("message", "changeCurrentDirectory");
            request.put("destinationDirectory", destination.getName());
            JSONObject response = new JSONObject((apiClient.sendMessageToRemote(request.toString())));

            // process response
            currentDirectory.name = response.getJSONObject("cwd").getString("name");
            currentDirectory.contents.clear();

            // sync current directory
            syncCurrentDirectoryContents();
            return response.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean goUp() {
        try {
            // create request
            JSONObject request = new JSONObject();
            request.put("message", "goUp");
            JSONObject response = new JSONObject(apiClient.sendMessageToRemote(request.toString()));

            // process response
            currentDirectory.name = response.getJSONObject("cwd").getString("name");
            currentDirectory.contents.clear();

            // sync directory
            syncCurrentDirectoryContents();
            return response.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    // sync methods
    private boolean syncCurrentDirectoryContents() {
        try {
            JSONObject request = new JSONObject();
            request.put("message", "getCurrentDirectoryContents");
            JSONObject response = new JSONObject(apiClient.sendMessageToRemote(request.toString()));
            currentDirectory.setContents(response.getJSONArray("contents"));
            return response.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Getters / Setters
    public RemoteFile getCurrentDirectory() {
        return currentDirectory;
    }
}
