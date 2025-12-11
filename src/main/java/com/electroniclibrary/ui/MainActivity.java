package com.electroniclibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.electroniclibrary.R;
import com.electroniclibrary.data.model.User;
import com.electroniclibrary.ui.auth.LoginActivity;
import com.electroniclibrary.ui.book.BookDetailActivity;
import com.electroniclibrary.ui.catalog.CatalogFragment;
import com.electroniclibrary.ui.home.HomeFragment;
import com.electroniclibrary.ui.profile.ProfileFragment;
import com.electroniclibrary.ui.viewmodel.AuthViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private AuthViewModel authViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        
        // Check if user is logged in
        if (!authViewModel.isLoggedIn()) {
            navigateToLogin();
            return;
        }
        
        initViews();
        setupBottomNavigation();
        
        // Load home fragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }
    
    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
    
    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment fragment = null;
            
            if (itemId == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.nav_catalog) {
                fragment = new CatalogFragment();
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment();
            }
            
            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }
    
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        User currentUser = authViewModel.getCurrentUser().getValue();
        if (currentUser != null && currentUser.isAdmin()) {
            getMenuInflater().inflate(R.menu.admin_menu, menu);
        }
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_admin) {
            Intent intent = new Intent(this, com.electroniclibrary.ui.admin.AdminActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

