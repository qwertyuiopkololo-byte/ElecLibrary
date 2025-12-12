package com.electroniclibrary.ui.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.electroniclibrary.R;
import com.electroniclibrary.data.model.Book;
import com.electroniclibrary.data.model.User;
import com.electroniclibrary.data.repository.FavoriteRepository;
import com.electroniclibrary.ui.viewmodel.AuthViewModel;
import com.electroniclibrary.ui.viewmodel.BookViewModel;
import com.electroniclibrary.ui.viewmodel.FavoriteViewModel;

public class BookDetailActivity extends AppCompatActivity {
    private ImageView ivCover;
    private TextView tvTitle, tvAuthor, tvGenre, tvDescription, tvRating, tvYourRating;
    private Button btnRead, btnFavorite, btnRate;
    private RatingBar ratingBar;
    private ProgressBar progressBar;
    
    private BookViewModel bookViewModel;
    private FavoriteViewModel favoriteViewModel;
    private AuthViewModel authViewModel;
    private FavoriteRepository favoriteRepository;
    
    private String bookId;
    private Book currentBook;
    private boolean isFavorite = false;
    
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
        tvYourRating = findViewById(R.id.tvYourRating);
        ratingBar = findViewById(R.id.ratingBar);
        btnRead = findViewById(R.id.btnRead);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnRate = findViewById(R.id.btnRate);
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
        
        btnFavorite.setOnClickListener(v -> toggleFavorite());
        btnRate.setOnClickListener(v -> submitRating());
    }
    
    private void setupViewModels() {
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        favoriteRepository = FavoriteRepository.getInstance(this);
        
        bookViewModel.getSelectedBook().observe(this, book -> {
            if (book != null) {
                currentBook = book;
                displayBook(book);
                checkFavorite();
                loadUserRating();
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

        bookViewModel.getUserRating().observe(this, rating -> {
            if (rating != null && rating > 0) {
                ratingBar.setRating(rating);
                tvYourRating.setText(getString(R.string.your_rating_value, rating));
            } else {
                ratingBar.setRating(0);
                tvYourRating.setText(getString(R.string.your_rating_placeholder));
            }
        });
        
        // Наблюдаем за ошибками при работе с избранным
        favoriteViewModel.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, "Ошибка: " + error, Toast.LENGTH_SHORT).show();
            }
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
            String ratingText = "Рейтинг: " + String.format("%.1f", book.getRating());
            if (book.getRatingCount() != null && book.getRatingCount() > 0) {
                ratingText += " (" + book.getRatingCount() + ")";
            }
            tvRating.setText(ratingText);
        } else {
            tvRating.setText(getString(R.string.rating_not_available));
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
    
    private void checkFavorite() {
        User currentUser = authViewModel.getCurrentUser().getValue();
        if (currentUser == null || !currentUser.isClient()) {
            btnFavorite.setVisibility(View.GONE);
            return;
        }
        
        favoriteViewModel.isFavorite(currentUser.getId(), bookId)
            .thenAccept(favorite -> {
                isFavorite = favorite;
                updateFavoriteButton();
            });
    }
    
    private void toggleFavorite() {
        User currentUser = authViewModel.getCurrentUser().getValue();
        if (currentUser == null) {
            Toast.makeText(this, "Необходимо войти в систему", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String userId = currentUser.getId();
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Ошибка: ID пользователя не найден. Пожалуйста, войдите заново.", Toast.LENGTH_LONG).show();
            return;
        }
        
        if (bookId == null || bookId.isEmpty()) {
            Toast.makeText(this, "Ошибка: ID книги не найден", Toast.LENGTH_SHORT).show();
            return;
        }
        
        android.util.Log.d("BookDetailActivity", "Toggle favorite - User ID: " + userId + ", Book ID: " + bookId);
        
        btnFavorite.setEnabled(false); // Блокируем кнопку во время операции
        
        if (isFavorite) {
            // Удаляем из избранного
            favoriteRepository.removeFromFavorites(userId, bookId)
                .thenAccept(success -> {
                    runOnUiThread(() -> {
                        if (success) {
                            isFavorite = false;
                            updateFavoriteButton();
                            Toast.makeText(this, "Удалено из избранного", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Ошибка при удалении из избранного", Toast.LENGTH_SHORT).show();
                        }
                        btnFavorite.setEnabled(true);
                    });
                })
                .exceptionally(throwable -> {
                    runOnUiThread(() -> {
                        String errorMsg = throwable.getMessage();
                        android.util.Log.e("BookDetailActivity", "Error removing favorite", throwable);
                        Toast.makeText(this, "Ошибка: " + (errorMsg != null ? errorMsg : "Неизвестная ошибка"), Toast.LENGTH_SHORT).show();
                        btnFavorite.setEnabled(true);
                    });
                    return null;
                });
        } else {
            // Добавляем в избранное
            favoriteRepository.addToFavorites(userId, bookId)
                .thenAccept(success -> {
                    runOnUiThread(() -> {
                        if (success) {
                            isFavorite = true;
                            updateFavoriteButton();
                            Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Ошибка при добавлении в избранное", Toast.LENGTH_SHORT).show();
                        }
                        btnFavorite.setEnabled(true);
                    });
                })
                .exceptionally(throwable -> {
                    runOnUiThread(() -> {
                        String errorMsg = throwable.getMessage();
                        android.util.Log.e("BookDetailActivity", "Error adding favorite", throwable);
                        
                        // Более понятное сообщение об ошибке
                        String userFriendlyMsg = "Ошибка при добавлении в избранное";
                        if (errorMsg != null) {
                            if (errorMsg.contains("foreign key constraint")) {
                                userFriendlyMsg = "Ошибка: пользователь не найден в базе данных. Пожалуйста, войдите заново.";
                            } else {
                                userFriendlyMsg = "Ошибка: " + errorMsg;
                            }
                        }
                        
                        Toast.makeText(this, userFriendlyMsg, Toast.LENGTH_LONG).show();
                        btnFavorite.setEnabled(true);
                    });
                    return null;
                });
        }
    }
    
    private void updateFavoriteButton() {
        btnFavorite.setText(isFavorite ? 
            getString(R.string.remove_from_favorites) : 
            getString(R.string.add_to_favorites));
    }

    private void loadUserRating() {
        User currentUser = authViewModel.getCurrentUser().getValue();
        if (currentUser == null || !currentUser.isClient()) {
            tvYourRating.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            btnRate.setVisibility(View.GONE);
            return;
        }

        tvYourRating.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.VISIBLE);
        btnRate.setVisibility(View.VISIBLE);

        bookViewModel.loadUserRating(bookId, currentUser.getId());
    }

    private void submitRating() {
        User currentUser = authViewModel.getCurrentUser().getValue();
        if (currentUser == null || !currentUser.isClient()) {
            Toast.makeText(this, getString(R.string.login_to_rate), Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentBook == null) {
            Toast.makeText(this, "Книга недоступна", Toast.LENGTH_SHORT).show();
            return;
        }

        int value = Math.round(ratingBar.getRating());
        if (value < 1 || value > 5) {
            Toast.makeText(this, "Оценка должна быть от 1 до 5", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRate.setEnabled(false);
        bookViewModel.rateBook(currentBook.getId(), currentUser.getId(), value)
            .thenAccept(book -> runOnUiThread(() -> {
                if (book != null) {
                    currentBook = book;
                    displayBook(book);
                    Toast.makeText(this, getString(R.string.rating_saved), Toast.LENGTH_SHORT).show();
                }
                btnRate.setEnabled(true);
            }))
            .exceptionally(throwable -> {
                runOnUiThread(() -> {
                    String errorMsg = throwable != null && throwable.getMessage() != null
                        ? throwable.getMessage()
                        : "Ошибка сохранения оценки";
                    Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                    btnRate.setEnabled(true);
                });
                return null;
            });
    }
}

