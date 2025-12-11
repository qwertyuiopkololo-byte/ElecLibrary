package com.electroniclibrary.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.electroniclibrary.R;

public class AdminFragment extends Fragment {
    private Button btnManageBooks, btnManageUsers;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        btnManageBooks = view.findViewById(R.id.btnManageBooks);
        btnManageUsers = view.findViewById(R.id.btnManageUsers);
        
        btnManageBooks.setOnClickListener(v -> {
            // TODO: Navigate to book management
        });
        
        btnManageUsers.setOnClickListener(v -> {
            // TODO: Navigate to user management
        });
    }
}

