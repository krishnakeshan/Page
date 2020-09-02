package com.qrilt.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qrilt.page.model.RemoteHost;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    // Properties
    Toolbar toolbar;
    EditText ipPart1EditText, ipPart2EditText, ipPart3EditText, ipPart4EditText, portEditText;
    Button connectButton;

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind views
        toolbar = findViewById(R.id.activity_main_toolbar);
        ipPart1EditText = findViewById(R.id.activity_main_ip_part_1_edit_text);
        ipPart2EditText = findViewById(R.id.activity_main_ip_part_2_edit_text);
        ipPart3EditText = findViewById(R.id.activity_main_ip_part_3_edit_text);
        ipPart4EditText = findViewById(R.id.activity_main_ip_part_4_edit_text);
        portEditText = findViewById(R.id.activity_main_port_edit_text);
        connectButton = findViewById(R.id.activity_main_connect_button);

        // setup toolbar
        setSupportActionBar(toolbar);

        // setup connect button
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get client
                String host = ipPart1EditText.getText().toString() + "." + ipPart2EditText.getText().toString() + "." + ipPart3EditText.getText().toString() + "." + ipPart4EditText.getText().toString();
                int port = Integer.parseInt(portEditText.getText().toString());

                // go to remote home activity
                Intent intent = new Intent(MainActivity.this, RemoteHomeActivity.class);
                intent.putExtra("REMOTE_HOST", host);
                intent.putExtra("REMOTE_PORT", port);
                startActivity(intent);
            }
        });
    }
}