package com.electroniclibrary.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.electroniclibrary.ui.viewmodel.AuthViewModel;
import com.electroniclibrary.ui.viewmodel.FavoriteViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private BookAdapter bookAdapter;
    private FavoriteViewModel favoriteViewModel;
    private AuthViewModel authViewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupViewModels();
        setupRecyclerView();
        loadFavorites();
    }
    
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);
    }
    
    private void setupViewModels() {
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        
        favoriteViewModel.getFavorites().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                bookAdapter.updateBooks(books);
                tvEmpty.setVisibility(books.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
        
        favoriteViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), "Ошибка: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        
        favoriteViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });
    }
    
    private void setupRecyclerView() {
        bookAdapter = new BookAdapter(new ArrayList<>(), book -> {
            if (getActivity() != null) {
                android.content.Intent intent = new android.content.Intent(getActivity(), 
                    com.electroniclibrary.ui.book.BookDetailActivity.class);
                intent.putExtra("book_id", book.getId());
                startActivity(intent);
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bookAdapter);
    }
    
    private void loadFavorites() {
        User currentUser = authViewModel.getCurrentUser().getValue();
        if (currentUser != null) {
            favoriteViewModel.loadFavorites(currentUser.getId());
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }
}

