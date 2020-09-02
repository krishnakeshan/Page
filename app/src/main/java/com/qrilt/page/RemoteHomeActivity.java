package com.qrilt.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qrilt.page.adapters.DirectoryContentsAdapter;
import com.qrilt.page.model.RemoteFile;
import com.qrilt.page.model.RemoteHost;

public class RemoteHomeActivity extends AppCompatActivity implements DirectoryContentsAdapter.ListInteractionListener {
    // Views
    TextView cwdTextView;
    RecyclerView cwdContentsRecyclerView;
    Button backButton;

    // Properties
    private RemoteHost remoteHost;
    private RecyclerView.LayoutManager layoutManager;
    private DirectoryContentsAdapter adapter;

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_home);

        // create remote host
        remoteHost = RemoteHost.forHostPort(getIntent().getStringExtra("REMOTE_HOST"), getIntent().getIntExtra("REMOTE_PORT", 0));

        // bind views
        cwdTextView = findViewById(R.id.activity_remote_home_cwd_text_view);
        cwdContentsRecyclerView = findViewById(R.id.activity_remote_home_cwd_contents_recycler_view);
        backButton = findViewById(R.id.activity_remote_home_back_button);

        // setup recyclerview
        adapter = new DirectoryContentsAdapter();
        adapter.addListInteractionListener(this);
        layoutManager = new LinearLayoutManager(this);
        cwdContentsRecyclerView.hasFixedSize();
        cwdContentsRecyclerView.setAdapter(adapter);
        cwdContentsRecyclerView.setLayoutManager(layoutManager);

        // get current directory name
        cwdTextView.setText(remoteHost.getFileExplorer().getCurrentDirectory().getName());
        adapter.setContents(remoteHost.getFileExplorer().getCurrentDirectory().getContents());

        // setup actions
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remoteHost.getFileExplorer().goUp();

                // update UI
                cwdTextView.setText(remoteHost.getFileExplorer().getCurrentDirectory().getName());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void changeCurrentDirectory(RemoteFile destination) {
        // call method on remoteHost
        remoteHost.getFileExplorer().changeCurrentDirectory(destination);

        // update ui
        cwdTextView.setText(remoteHost.getFileExplorer().getCurrentDirectory().getName());
        adapter.notifyDataSetChanged();
    }

    private void viewFileDetails(RemoteFile file) {
        // prepare intent
        Intent intent = new Intent(this, FileDetailsActivity.class);
        intent.putExtra("REMOTE_HOST", remoteHost.getHost());
        intent.putExtra("REMOTE_PORT", remoteHost.getPort());
        intent.putExtra("REMOTE_FILE", file);
        startActivity(intent);
    }

    // List Interaction Methods
    public void onItemSelected(DirectoryContentsAdapter.ContentItemViewHolder viewHolder) {
        RemoteFile selectedFile = viewHolder.getRemoteFile();

        // change directory if directory selected
        if (selectedFile.isDir())
            changeCurrentDirectory(viewHolder.getRemoteFile());

            // view file if file selected
        else viewFileDetails(viewHolder.getRemoteFile());
    }
}