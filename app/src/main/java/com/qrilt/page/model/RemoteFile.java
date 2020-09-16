package com.qrilt.page.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RemoteFile implements Parcelable {
    // Properties
    String name;
    boolean isDir;
    int size;
    List<RemoteFile> contents = new ArrayList<>();
    public static final Parcelable.Creator<RemoteFile> CREATOR = new Parcelable.Creator<RemoteFile>() {
        @Override
        public RemoteFile createFromParcel(Parcel parcel) {
            return new RemoteFile(parcel);
        }

        @Override
        public RemoteFile[] newArray(int size) {
            return new RemoteFile[size];
        }
    };

    // Constructors
    public RemoteFile() {
    }

    public RemoteFile(JSONObject fileRep) {
        try {
            name = fileRep.getString("name");
            isDir = fileRep.getBoolean("is_dir");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Parcel constructor
    private RemoteFile(Parcel in) {
        name = in.readString();
        isDir = in.readInt() == 1;
        size = in.readInt();
        in.readTypedList(contents, CREATOR);
    }

    // Getters
    public String getName() {
        return name;
    }

    public boolean isDir() {
        return isDir;
    }

    public List<RemoteFile> getContents() {
        Log.d("DebugK", "Called Get Contents");
        return contents;
    }

    // Setters
    public void setContents(List<RemoteFile> contents) {
        this.contents = contents;
    }

    public void setContents(JSONArray contents) {
        try {
            // create RemoteFile objects
            for (int i = 0; i < contents.length(); i++) {
                JSONObject fileRep = contents.getJSONObject(i);
                RemoteFile remoteFile = new RemoteFile(fileRep);
                this.contents.add(remoteFile);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeInt(isDir ? 1 : 0);
        parcel.writeInt(size);
        parcel.writeTypedList(contents);
    }
}
