package com.qrilt.page;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.qrilt.page.viewmodels.MainActivityViewModel;

public class HostSettingsFragment extends Fragment {
    // Views
    EditText nameEditText, ipEditText, portEditText;
    AppCompatButton saveButton;

    // Properties
    MainActivityViewModel viewModel;

    public HostSettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_host_settings, container, false);

        bindViews(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get viewmodel
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        // setup views
        nameEditText.setText(viewModel.getSelectedRemoteHost().getValue().getName());
        ipEditText.setText(viewModel.getSelectedRemoteHost().getValue().getHost());
        portEditText.setText(String.valueOf(viewModel.getSelectedRemoteHost().getValue().getPort()));
    }

    private void bindViews(View view) {
        nameEditText = view.findViewById(R.id.fragment_host_settings_name_edittext);
        ipEditText = view.findViewById(R.id.fragment_host_settings_ip_edittext);
        portEditText = view.findViewById(R.id.fragment_host_settings_port_edittext);
        saveButton = view.findViewById(R.id.fragment_host_settings_save_button);

        // setup buttons
        saveButton.setOnClickListener(clickedView -> {
            viewModel.getSelectedRemoteHost().getValue().name = nameEditText.getText().toString();
            viewModel.getSelectedRemoteHost().getValue().host = ipEditText.getText().toString();
            viewModel.getSelectedRemoteHost().getValue().port = Integer.valueOf(portEditText.getText().toString());
            viewModel.updateRemoteHost(viewModel.getSelectedRemoteHost().getValue());
        });
    }
}