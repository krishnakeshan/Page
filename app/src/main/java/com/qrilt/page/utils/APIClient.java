package com.qrilt.page.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class APIClient {
    // Properties
    private String host;
    private int port;

    private Socket socket;

    private ExecutorService executorService;

    // Constructors
    public APIClient() {
        executorService = Executors.newCachedThreadPool();
    }

    public static APIClient fromHostPort(String host, int port) {
        APIClient client = new APIClient();
        client.host = host;
        client.port = port;
        return client;
    }

    // Methods
    // method to send a message to the connected host
    public String sendMessageToRemote(final String message) {
        // start a new thread
        Future<String> response = executorService.submit(() -> {
            String resp = "";
            try {
                // connect to remote
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), 2000);

                // obtain IO streams
                BufferedReader sockIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter sockOut = new PrintWriter(socket.getOutputStream(), true);

                // send message
                sockOut.println(message);

                // read response
                resp = sockIn.readLine();

                Log.d("DebugK", "Got response " + resp);

                // close socket
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();

                JSONObject errResp = new JSONObject();
                errResp.put("success", false);
                return errResp.toString();
            }

            return resp;
        });

        // extract response from future
        try {
            return response.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return "error";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public interface ResponseHandler {
    }
}
