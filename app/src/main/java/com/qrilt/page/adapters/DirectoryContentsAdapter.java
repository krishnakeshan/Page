package com.qrilt.page.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qrilt.page.R;
import com.qrilt.page.model.RemoteFile;

import java.util.ArrayList;
import java.util.List;

public class DirectoryContentsAdapter extends RecyclerView.Adapter<DirectoryContentsAdapter.ContentItemViewHolder> {
    // interfaces
    public interface ListInteractionListener {
        public void onItemSelected(ContentItemViewHolder viewHolder);
    }

    // Properties
    private List<RemoteFile> contents;
    private List<ListInteractionListener> listeners;

    // Constructors
    public DirectoryContentsAdapter() {
        listeners = new ArrayList<>();
    }

    public DirectoryContentsAdapter(List<RemoteFile> contents) {
        listeners = new ArrayList<>();
        this.contents = contents;
    }

    // Methods
    @NonNull
    @Override
    public ContentItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_directory_content_item, parent, false);

        // attach click listener
        final ContentItemViewHolder contentItemViewHolder = new ContentItemViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ListInteractionListener listener : listeners) {
                    listener.onItemSelected(contentItemViewHolder);
                }
            }
        });
        return contentItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContentItemViewHolder holder, int position) {
        RemoteFile file = contents.get(position);
        holder.setRemoteFile(file);
        holder.nameTextView.setText(file.getName());

        if (file.isDir()) {
            holder.iconImageView.setImageResource(R.drawable.ic_folder_open);
            holder.downloadImageView.setVisibility(View.GONE);
        } else {
            holder.iconImageView.setImageResource(R.drawable.ic_document_outline);
            holder.downloadImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return contents != null ? contents.size() : 0;
    }

    public void setContents(List<RemoteFile> contents) {
        this.contents = contents;
        notifyDataSetChanged();
    }

    public void addListInteractionListener(ListInteractionListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    // View Holder
    public static class ContentItemViewHolder extends RecyclerView.ViewHolder {
        // properties
        private RemoteFile remoteFile;
        private ImageView iconImageView, downloadImageView;
        public TextView nameTextView;

        // constructors
        public ContentItemViewHolder(@NonNull View itemView) {
            super(itemView);

            iconImageView = itemView.findViewById(R.id.view_holder_directory_content_item_icon_imageview);
            nameTextView = itemView.findViewById(R.id.view_holder_directory_content_item_name_textview);
            downloadImageView = itemView.findViewById(R.id.view_holder_directory_content_item_download_imageview);
        }

        public void setRemoteFile(RemoteFile remoteFile) {
            this.remoteFile = remoteFile;
        }

        public RemoteFile getRemoteFile() {
            return remoteFile;
        }
    }
}
