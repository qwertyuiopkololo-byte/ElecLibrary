package com.electroniclibrary.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electroniclibrary.R;
import com.electroniclibrary.data.model.Book;
import com.electroniclibrary.data.model.User;
import com.electroniclibrary.ui.adapter.BookAdapter;
import com.electroniclibrary.ui.auth.LoginActivity;
import com.electroniclibrary.ui.book.BookDetailActivity;
import com.electroniclibrary.ui.viewmodel.AuthViewModel;
import com.electroniclibrary.ui.viewmodel.FavoriteViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private TextView tvUsername, tvName, tvEmptyFavorites;
    private Button btnLogout;
    private RecyclerView rvFavorites;
    private ProgressBar progressBarFavorites;
    private AuthViewModel authViewModel;
    private FavoriteViewModel favoriteViewModel;
    private BookAdapter favoritesAdapter;
    
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
        rvFavorites = view.findViewById(R.id.rvFavorites);
        tvEmptyFavorites = view.findViewById(R.id.tvEmptyFavorites);
        progressBarFavorites = view.findViewById(R.id.progressBarFavorites);
        
        // Настройка RecyclerView для избранных книг
        favoritesAdapter = new BookAdapter(new ArrayList<>(), book -> {
            Intent intent = new Intent(getContext(), BookDetailActivity.class);
            intent.putExtra("book_id", book.getId());
            startActivity(intent);
        });
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorites.setAdapter(favoritesAdapter);
    }
    
    private void setupViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        
        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                tvUsername.setText(user.getUsername());
                tvName.setText(user.getFirstName() + " " + user.getLastName());
                // Загружаем избранные книги пользователя
                loadFavorites(user.getId());
            } else {
                navigateToLogin();
            }
        });
        
        // Наблюдаем за изменениями избранных книг
        favoriteViewModel.getFavorites().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                favoritesAdapter.updateBooks(books);
                tvEmptyFavorites.setVisibility(books.isEmpty() ? View.VISIBLE : View.GONE);
                rvFavorites.setVisibility(books.isEmpty() ? View.GONE : View.VISIBLE);
            }
        });
        
        // Наблюдаем за ошибками
        favoriteViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), "Ошибка загрузки избранного: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        
        // Наблюдаем за состоянием загрузки
        favoriteViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            progressBarFavorites.setVisibility(loading ? View.VISIBLE : View.GONE);
        });
    }
    
    private void loadFavorites(String userId) {
        if (userId != null && !userId.isEmpty()) {
            favoriteViewModel.loadFavorites(userId);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        User currentUser = authViewModel.getCurrentUser().getValue();
        if (currentUser != null) {
            loadFavorites(currentUser.getId());
        }
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

