package com.electroniclibrary.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electroniclibrary.R;
import com.electroniclibrary.data.model.Book;
import com.electroniclibrary.data.model.Genre;
import com.electroniclibrary.ui.adapter.BookAdapter;
import com.electroniclibrary.ui.adapter.GenreAdapter;
import com.electroniclibrary.ui.book.BooksByCategoryActivity;
import com.electroniclibrary.ui.viewmodel.BookViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private GenreAdapter genreAdapter;
    private BookViewModel bookViewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupViewModel();
        setupRecyclerView();
        loadBooks();
    }
    
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
    }
    
    private void setupViewModel() {
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        
        bookViewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null) {
                genreAdapter.updateGenres(genres);
            }
        });
        
        bookViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), "Ошибка: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        
        bookViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });
    }
    
    private void setupRecyclerView() {
        genreAdapter = new GenreAdapter(new ArrayList<>(), genre -> {
            // Navigate to books by category
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), BooksByCategoryActivity.class);
                intent.putExtra("genre_id", genre.getId());
                intent.putExtra("genre_name", genre.getName());
                startActivity(intent);
            }
        });
        
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(genreAdapter);
    }
    
    private void loadBooks() {
        bookViewModel.loadGenres();
    }
}

