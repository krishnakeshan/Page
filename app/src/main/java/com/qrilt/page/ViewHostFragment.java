package com.qrilt.page;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qrilt.page.adapters.DirectoryContentsAdapter;
import com.qrilt.page.model.RemoteFile;
import com.qrilt.page.model.RemoteHost;
import com.qrilt.page.utils.Animator;
import com.qrilt.page.viewmodels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewHostFragment extends Fragment implements DirectoryContentsAdapter.ListInteractionListener {
    // Views
    private View mainContainer, noHostsContainer, offlineHostContainer;
    private ImageView filesBackButton;
    private TextView cwdTextView;
    private EditText searchEditText;
    private RecyclerView filesRecyclerView;
    private AppCompatButton addDeviceButton;
    private TextView offlineHostTextView;
    private AppCompatButton offlineHostSettingsButton;
    private ImageButton toolbarActionButton;

    // Properties
    private MainActivityViewModel viewModel;
    private DirectoryContentsAdapter adapter;
    private NavController navController;
    private List<RemoteFile> searchResults;

    public ViewHostFragment() {
        searchResults = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_host, container, false);

        // setup views
        bindViews(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // find NavController
        navController = Navigation.findNavController(view);

        // get ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        // listen for RemoteHosts list changes
        viewModel.getRemoteHosts().observe(requireActivity(), remoteHosts -> {
            // check if no hosts added
            if (remoteHosts == null || remoteHosts.isEmpty()) {
                // navigate to add device screen
                Animator.crossFade(mainContainer, noHostsContainer);
            }

            // hosts exist, setup views
            else {
                // show host
                Animator.crossFade(noHostsContainer, mainContainer);

                // initialize all remoteHost instances
                for (RemoteHost remoteHost : remoteHosts) {
                    remoteHost.initDBInstance();
                }

                // if no device selected, select first
                if (viewModel.getSelectedRemoteHost().getValue() == null) {
                    viewModel.setSelectedRemoteHost(remoteHosts.get(0));
                }

                // setup toolbar action
                toolbarActionButton = ((MainActivity) requireActivity()).toolbarActionButton;
                toolbarActionButton.setVisibility(View.VISIBLE);
                toolbarActionButton.setImageResource(R.drawable.ic_settings_outline);
                toolbarActionButton.setOnClickListener(clickedView -> {
                    navController.navigate(R.id.action_viewHostFragment_to_hostSettingsFragment);
                    toolbarActionButton.setVisibility(View.GONE);
                });
            }
        });

        // listen for selected RemoteHost changes
        viewModel.getSelectedRemoteHost().observe(requireActivity(), remoteHost -> {
            // set listener for changes in host status
            remoteHost.setDeviceStateChangedListener(isOnline -> {
                // device is offline and offline view not visible
                if (!isOnline && offlineHostContainer.getVisibility() != View.VISIBLE) {
                    view.post(() -> {
                        Animator.crossFade(mainContainer, offlineHostContainer);
                    });
                }

                // device is online and online view is not visible
                else if (isOnline && mainContainer.getVisibility() != View.VISIBLE) {
                    view.post(() -> {
                        Animator.crossFade(offlineHostContainer, mainContainer);
                    });
                }
            });

            // set title
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(remoteHost.getName());

            // set offline text
            offlineHostTextView.setText(remoteHost.name + " is Offline");

            // set current folder
            cwdTextView.setText(remoteHost.getFileExplorer().getCurrentDirectory().getName());

            // get current directory contents
            adapter.setContents(remoteHost.getFileExplorer().getCurrentDirectory().getContents());
        });
    }

    private void bindViews(View view) {
        mainContainer = view.findViewById(R.id.fragment_view_host_main_container);
        filesBackButton = view.findViewById(R.id.fragment_view_host_files_back_button);
        cwdTextView = view.findViewById(R.id.fragment_view_host_cwd_textview);
        searchEditText = view.findViewById(R.id.fragment_view_host_search_edittext);
        filesRecyclerView = view.findViewById(R.id.fragment_view_host_files_recyclerview);

        noHostsContainer = view.findViewById(R.id.fragment_view_host_no_hosts_container);
        addDeviceButton = view.findViewById(R.id.fragment_view_host_add_button);

        offlineHostContainer = view.findViewById(R.id.fragment_view_host_offline_host_container);
        offlineHostTextView = view.findViewById(R.id.fragment_view_host_offline_host_textview);
        offlineHostSettingsButton = view.findViewById(R.id.fragment_view_host_settings_button);


        // setup buttons
        filesBackButton.setOnClickListener(clickedView -> {
            viewModel.getSelectedRemoteHost().getValue().getFileExplorer().goUp();
            updateCWDViews();
        });

        addDeviceButton.setOnClickListener(clickedView -> {
            navController.navigate(R.id.addDeviceFragment);
        });

        offlineHostSettingsButton.setOnClickListener(clickedView -> {
            navController.navigate(R.id.hostSettingsFragment);
        });

        // setup files recyclerview
        filesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DirectoryContentsAdapter();
        adapter.addListInteractionListener(this);
        filesRecyclerView.setAdapter(adapter);

        // setup search
//        searchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String searchString = charSequence.toString();
//
//                // search cleared, display everything
//                if (searchString.isEmpty()) {
//                    adapter.setContents(viewModel.getSelectedRemoteHost().getValue().getFileExplorer().getCurrentDirectory().getContents());
//                }
//
//                // user searched for something
//                else {
//                    searchResults.clear();
//                    for (RemoteFile remoteFile : viewModel.getSelectedRemoteHost().getValue().getFileExplorer().getCurrentDirectory().getContents()) {
//                        if (remoteFile.getName().contains(searchString)) {
//                            searchResults.add(remoteFile);
//                        }
//                    }
//                    adapter.setContents(searchResults);
//                }
//
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
    }

    private void updateCWDViews() {
        // update cwd textview
        cwdTextView.setText(viewModel.getSelectedRemoteHost().getValue().getFileExplorer().getCurrentDirectory().getName());

        // update list
        adapter.setContents(viewModel.getSelectedRemoteHost().getValue().getFileExplorer().getCurrentDirectory().getContents());
    }

    public void onItemSelected(DirectoryContentsAdapter.ContentItemViewHolder viewHolder) {
        RemoteFile selectedFile = viewHolder.getRemoteFile();

        // if folder, navigate to it
        if (selectedFile.isDir()) {
            viewModel.getSelectedRemoteHost().getValue().getFileExplorer().changeCurrentDirectory(selectedFile);
            updateCWDViews();
        }
    }
}