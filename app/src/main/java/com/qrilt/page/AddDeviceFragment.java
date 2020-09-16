package com.qrilt.page;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qrilt.page.model.RemoteHost;
import com.qrilt.page.viewmodels.MainActivityViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddDeviceFragment extends Fragment {
    // Views
    private View ipEntryContainer, portEntryContainer, nameEntryContainer, checkContainer;
    private AppCompatEditText ipEditText, portEditText, nameEditText;
    private AppCompatButton ipNextButton, portNextButton, portBackButton, nameNextButton, nameBackButton, checkCancelButton;

    // Properties
    private int shortAnimationDuration;
    private NavController navController;
    private MainActivityViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get short animation duration
        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_device, container, false);

        // setup views
        setupViews(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // find viewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        // find navController
        navController = Navigation.findNavController(view);
    }

    private void setupViews(View view) {
        ipEntryContainer = view.findViewById(R.id.fragment_add_device_ip_container);
        portEntryContainer = view.findViewById(R.id.fragment_add_device_port_container);
        nameEntryContainer = view.findViewById(R.id.fragment_add_device_name_container);
        checkContainer = view.findViewById(R.id.fragment_add_device_checking_container);

        ipEditText = view.findViewById(R.id.fragment_add_device_ip_edit_text);
        portEditText = view.findViewById(R.id.fragment_add_device_port_edit_text);
        nameEditText = view.findViewById(R.id.fragment_add_device_name_edit_text);

        ipNextButton = view.findViewById(R.id.fragment_add_device_ip_next_button);
        portBackButton = view.findViewById(R.id.fragment_add_device_port_back_button);
        portNextButton = view.findViewById(R.id.fragment_add_device_port_next_button);
        nameBackButton = view.findViewById(R.id.fragment_add_device_name_back_button);
        nameNextButton = view.findViewById(R.id.fragment_add_device_name_next_button);
        checkCancelButton = view.findViewById(R.id.fragment_add_device_cancel_check_button);

        ipNextButton.setOnClickListener(clickedView -> {
            // transition to port container
            crossFade(ipEntryContainer, portEntryContainer);
        });

        portBackButton.setOnClickListener(clickedView -> {
            // transition to port container
            crossFade(portEntryContainer, ipEntryContainer);
        });

        portNextButton.setOnClickListener(clickedView -> {
            // transition to check container
            crossFade(portEntryContainer, nameEntryContainer);
        });

        nameBackButton.setOnClickListener(clickedView -> {
            // transition to port container
            crossFade(nameEntryContainer, portEntryContainer);
        });

        nameNextButton.setOnClickListener(clickedView -> {
            // add remoteHost to database and return
            viewModel.addRemoteHost(nameEditText.getText().toString(), ipEditText.getText().toString(), Integer.parseInt(portEditText.getText().toString()));
            navController.popBackStack();
        });

        checkCancelButton.setOnClickListener(clickedView -> {
            // transition to port container
            crossFade(checkContainer, portEntryContainer);
        });
    }

    private void crossFade(final View from, final View to) {
        to.setAlpha(0f);
        to.setVisibility(View.VISIBLE);
        to.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);

        from.animate().alpha(0f).setDuration(shortAnimationDuration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                from.setVisibility(View.GONE);
            }
        });
    }
}