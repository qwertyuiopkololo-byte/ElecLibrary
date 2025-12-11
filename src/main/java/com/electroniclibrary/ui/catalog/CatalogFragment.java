package com.electroniclibrary.ui.catalog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electroniclibrary.R;
import com.electroniclibrary.data.model.Book;
import com.electroniclibrary.ui.adapter.BookAdapter;
import com.electroniclibrary.ui.viewmodel.BookViewModel;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment {
    private EditText etSearch;
    private RecyclerView recyclerViewBooks;
    private ProgressBar progressBar;
    private BookAdapter bookAdapter;
    private BookViewModel bookViewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupViewModel();
        setupRecyclerViews();
        setupSearch();
        loadData();
    }
    
    private void initViews(View view) {
        etSearch = view.findViewById(R.id.etSearch);
        recyclerViewBooks = view.findViewById(R.id.recyclerViewBooks);
        progressBar = view.findViewById(R.id.progressBar);
    }
    
    private void setupViewModel() {
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        
        bookViewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                bookAdapter.updateBooks(books);
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
    
    private void setupRecyclerViews() {
        // Books
        bookAdapter = new BookAdapter(new ArrayList<>(), book -> {
            if (getActivity() != null) {
                android.content.Intent intent = new android.content.Intent(getActivity(), 
                    com.electroniclibrary.ui.book.BookDetailActivity.class);
                intent.putExtra("book_id", book.getId());
                startActivity(intent);
            }
        });
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBooks.setAdapter(bookAdapter);
    }
    
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() >= 2) {
                    bookViewModel.searchBooks(query);
                } else if (query.isEmpty()) {
                    bookViewModel.loadPopularBooks(20);
                }
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void loadData() {
        bookViewModel.loadPopularBooks(20);
    }
}

