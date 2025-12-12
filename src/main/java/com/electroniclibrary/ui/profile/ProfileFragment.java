package com.electroniclibrary.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.electroniclibrary.R;
import com.electroniclibrary.data.model.User;
import com.electroniclibrary.ui.auth.LoginActivity;
import com.electroniclibrary.ui.viewmodel.AuthViewModel;

public class ProfileFragment extends Fragment {
    private TextView tvUsername, tvName;
    private Button btnLogout;
    private AuthViewModel authViewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupViewModel();
        setupListeners();
    }
    
    private void initViews(View view) {
        tvUsername = view.findViewById(R.id.tvUsername);
        tvName = view.findViewById(R.id.tvName);
        btnLogout = view.findViewById(R.id.btnLogout);
    }
    
    private void setupViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        
        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                tvUsername.setText(user.getUsername());
                tvName.setText(user.getFirstName() + " " + user.getLastName());
            } else {
                navigateToLogin();
            }
        });
    }
    
    private void setupListeners() {
        btnLogout.setOnClickListener(v -> {
            authViewModel.logout();
            Toast.makeText(getContext(), "Вы вышли из системы", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        });
    }
    
    private void navigateToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}

