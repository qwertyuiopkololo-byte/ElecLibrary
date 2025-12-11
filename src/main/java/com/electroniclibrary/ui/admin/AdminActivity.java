package com.electroniclibrary.ui.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.electroniclibrary.R;
import com.electroniclibrary.data.model.User;
import com.electroniclibrary.ui.viewmodel.AuthViewModel;

public class AdminActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        
        // Check if user is admin
        User currentUser = authViewModel.getCurrentUser().getValue();
        if (currentUser == null || !currentUser.isAdmin()) {
            Toast.makeText(this, "Доступ запрещен", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Панель администратора");
        
        // Load admin fragment
        if (savedInstanceState == null) {
            loadFragment(new AdminFragment());
        }
    }
    
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit();
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

