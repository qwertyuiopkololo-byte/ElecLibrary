package com.electroniclibrary.ui.book;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.electroniclibrary.R;
import com.electroniclibrary.data.model.Book;
import com.electroniclibrary.ui.adapter.BookAdapter;
import com.electroniclibrary.ui.viewmodel.BookViewModel;

import java.util.ArrayList;
import java.util.List;

public class BooksByCategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvCategoryName;
    private BookAdapter bookAdapter;
    private BookViewModel bookViewModel;
    private String genreId;
    private String genreName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_by_category);
        
        genreId = getIntent().getStringExtra("genre_id");
        genreName = getIntent().getStringExtra("genre_name");
        
        if (genreId == null) {
            finish();
            return;
        }
        
        initViews();
        setupViewModel();
        setupRecyclerView();
        loadBooks();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvCategoryName = findViewById(R.id.tvCategoryName);
        
        if (genreName != null) {
            tvCategoryName.setText(genreName);
        } else {
            tvCategoryName.setText("Категория");
        }
    }
    
    private void setupViewModel() {
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        
        bookViewModel.getBooks().observe(this, books -> {
            if (books != null) {
                bookAdapter.updateBooks(books);
            }
        });
        
        bookViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Ошибка: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        
        bookViewModel.getLoading().observe(this, loading -> {
            progressBar.setVisibility(loading ? android.view.View.VISIBLE : android.view.View.GONE);
        });
    }
    
    private void setupRecyclerView() {
        bookAdapter = new BookAdapter(new ArrayList<>(), book -> {
            android.content.Intent intent = new android.content.Intent(this, BookDetailActivity.class);
            intent.putExtra("book_id", book.getId());
            startActivity(intent);
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookAdapter);
    }
    
    private void loadBooks() {
        bookViewModel.loadBooksByGenre(genreId);
    }
}


