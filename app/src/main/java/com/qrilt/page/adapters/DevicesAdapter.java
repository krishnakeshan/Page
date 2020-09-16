package com.qrilt.page.adapters;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.qrilt.page.R;
import com.qrilt.page.model.RemoteHost;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {
    public interface DeviceSelectionListener {
        void onDeviceSelected(RemoteHost remoteHost);
    }

    DeviceSelectionListener listener;
    List<RemoteHost> remoteHosts;

    public DevicesAdapter(List<RemoteHost> remoteHosts, DeviceSelectionListener listener) {
        this.remoteHosts = remoteHosts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_device, parent, false);

        // set listeners
        DeviceViewHolder viewHolder = new DeviceViewHolder(view);
        view.setOnClickListener(clickedView -> {
            if (listener != null) {
                listener.onDeviceSelected(viewHolder.remoteHost);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.remoteHost = remoteHosts.get(position);
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return remoteHosts != null ? remoteHosts.size() : 0;
    }

    public void setRemoteHosts(List<RemoteHost> remoteHosts) {
        this.remoteHosts = remoteHosts;
        notifyDataSetChanged();
    }

    // View Holder
    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        // Properties / Views
        RemoteHost remoteHost;
        TextView ipTextView;

        DeviceViewHolder(View view) {
            super(view);

            ipTextView = view.findViewById(R.id.view_holder_device_ip_text_view);
        }

        public void bind() {
            ipTextView.setText(remoteHost.getHost());
        }
    }
}
