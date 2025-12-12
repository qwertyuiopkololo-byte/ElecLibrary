package com.electroniclibrary.ui.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.electroniclibrary.R;
import com.electroniclibrary.data.model.Book;
import com.electroniclibrary.data.model.User;
import com.electroniclibrary.ui.viewmodel.AuthViewModel;
import com.electroniclibrary.ui.viewmodel.BookViewModel;

public class BookDetailActivity extends AppCompatActivity {
    private ImageView ivCover;
    private TextView tvTitle, tvAuthor, tvGenre, tvDescription, tvRating;
    private Button btnRead;
    private ProgressBar progressBar;
    
    private BookViewModel bookViewModel;
    private AuthViewModel authViewModel;
    
    private String bookId;
    private Book currentBook;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        
        bookId = getIntent().getStringExtra("book_id");
        if (bookId == null) {
            finish();
            return;
        }
        
        initViews();
        setupViewModels();
        loadBook();
    }
    
    private void initViews() {
        ivCover = findViewById(R.id.ivCover);
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvGenre = findViewById(R.id.tvGenre);
        tvDescription = findViewById(R.id.tvDescription);
        tvRating = findViewById(R.id.tvRating);
        btnRead = findViewById(R.id.btnRead);
        progressBar = findViewById(R.id.progressBar);
        
        btnRead.setOnClickListener(v -> {
            if (currentBook == null) {
                Toast.makeText(this, "Книга недоступна", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, ReadBookActivity.class);
            intent.putExtra("book_id", currentBook.getId());
            if (currentBook.getFileUrl() != null && !currentBook.getFileUrl().isEmpty()) {
                intent.putExtra("file_url", currentBook.getFileUrl());
            } else if (currentBook.getContent() != null && !currentBook.getContent().isEmpty()) {
                intent.putExtra("content", currentBook.getContent());
            } else {
                Toast.makeText(this, "Книга недоступна для чтения", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(intent);
        });
    }
    
    private void setupViewModels() {
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        
        bookViewModel.getSelectedBook().observe(this, book -> {
            if (book != null) {
                currentBook = book;
                displayBook(book);
            }
        });
        
        bookViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Ошибка: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        
        bookViewModel.getLoading().observe(this, loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });
    }
    
    private void loadBook() {
        bookViewModel.loadBookById(bookId);
    }
    
    private void displayBook(Book book) {
        tvTitle.setText(book.getTitle());
        
        if (book.getAuthor() != null) {
            tvAuthor.setText("Автор: " + book.getAuthor().getFullName());
        }
        
        if (book.getGenre() != null) {
            tvGenre.setText("Жанр: " + book.getGenre().getName());
        }
        
        tvDescription.setText(book.getDescription());
        
        if (book.getRating() != null) {
            tvRating.setText("Рейтинг: " + String.format("%.1f", book.getRating()));
        }
        
        if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
            Glide.with(this)
                .load(book.getCoverUrl())
                .placeholder(R.drawable.ic_book_placeholder)
                .into(ivCover);
        }
        
        User currentUser = authViewModel.getCurrentUser().getValue();
        btnRead.setEnabled(currentUser != null && currentUser.isClient());
    }
}

