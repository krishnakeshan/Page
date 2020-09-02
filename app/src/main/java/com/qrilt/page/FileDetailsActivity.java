package com.qrilt.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.qrilt.page.model.RemoteFile;
import com.qrilt.page.model.RemoteHost;

public class FileDetailsActivity extends AppCompatActivity {
    // Views
    TextView nameTextView, sizeTextView;
    Button downloadButton;

    // Properties
    private RemoteHost remoteHost;
    private RemoteFile remoteFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);

        // bind views
        nameTextView = findViewById(R.id.activity_file_details_name_text_view);
        sizeTextView = findViewById(R.id.activity_file_details_size_text_view);
        downloadButton = findViewById(R.id.activity_file_details_download_button);

        // get remote host
        remoteHost = RemoteHost.forHostPort(getIntent().getStringExtra("REMOTE_HOST"), getIntent().getIntExtra("REMOTE_PORT", 0));
        remoteFile = (RemoteFile) getIntent().getParcelableExtra("REMOTE_FILE");

        Log.d("DebugK", remoteFile.getName());
    }
}