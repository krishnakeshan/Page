package com.qrilt.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.navigation.NavigationView;
import com.qrilt.page.adapters.DevicesAdapter;
import com.qrilt.page.model.RemoteHost;
import com.qrilt.page.utils.Animator;
import com.qrilt.page.viewmodels.MainActivityViewModel;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DevicesAdapter.DeviceSelectionListener {
    // Views
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageButton toolbarActionButton;
    NavHostFragment navHostFragment;
    RecyclerView devicesRecyclerView;

    // Properties
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    MainActivityViewModel viewModel;

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize stuff
        Animator.init(this);

        // get viewModel
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // bind views
        drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        navigationView = findViewById(R.id.activity_main_navigation_view);
        toolbar = findViewById(R.id.activity_main_toolbar);
        toolbarActionButton = findViewById(R.id.activity_main_toolbar_action_button);
        devicesRecyclerView = findViewById(R.id.activity_main_devices_recyclerview);

        // setup drawer layout
        DevicesAdapter devicesAdapter = new DevicesAdapter(viewModel.getRemoteHosts().getValue(), this);
        devicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        devicesRecyclerView.setAdapter(devicesAdapter);
        viewModel.getRemoteHosts().observe(this, devicesAdapter::setRemoteHosts);

        // setup navigation
        setSupportActionBar(toolbar);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawerLayout).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    // DeviceSelectionListener methods
    public void onDeviceSelected(RemoteHost remoteHost) {
        // set selected device
        viewModel.setSelectedRemoteHost(remoteHost);

        // close drawer
        drawerLayout.closeDrawers();
    }
}