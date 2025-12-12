package com.electroniclibrary.data.repository;

import android.content.Context;
import android.util.Log;

import com.electroniclibrary.data.model.Book;
import com.electroniclibrary.data.model.Favorite;
import com.electroniclibrary.data.supabase.SupabaseClientHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.electroniclibrary.data.supabase.SupabaseHelpers;
import io.github.jan.supabase.postgrest.Postgrest;
import java.util.HashMap;
import java.util.Map;

public class FavoriteRepository {
    private static final String TAG = "FavoriteRepository";
    private static FavoriteRepository instance;
    private Postgrest postgrest;
    private ExecutorService executorService;
    
    private FavoriteRepository(Context context) {
        postgrest = SupabaseClientHelper.getInstance().getPostgrest();
        executorService = Executors.newFixedThreadPool(2);
    }
    
    public static synchronized FavoriteRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FavoriteRepository(context);
        }
        return instance;
    }
    
    public CompletableFuture<List<Book>> getFavorites(String userId) {
        CompletableFuture<List<Book>> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                List<Favorite> favorites = SupabaseHelpers.selectWhereFavorites(postgrest, "favorites", "user_id", userId);
                List<Book> books = favorites.stream()
                    .map(Favorite::getBook)
                    .collect(java.util.stream.Collectors.toList());
                future.complete(books);
            } catch (Exception e) {
                Log.e(TAG, "Error getting favorites", e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
    
    public CompletableFuture<Boolean> addToFavorites(String userId, String bookId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Adding to favorites - User ID: " + userId + ", Book ID: " + bookId);
                
                // Проверяем, что userId и bookId не пустые
                if (userId == null || userId.isEmpty()) {
                    throw new Exception("User ID is null or empty");
                }
                if (bookId == null || bookId.isEmpty()) {
                    throw new Exception("Book ID is null or empty");
                }
                
                // Проверяем существование пользователя в БД
                try {
                    List<com.electroniclibrary.data.model.User> users = SupabaseHelpers.selectWhereUsers(
                        postgrest, "users", "id", userId);
                    if (users == null || users.isEmpty()) {
                        Log.e(TAG, "User with ID " + userId + " not found in database");
                        throw new Exception("User not found in database. Please log in again.");
                    }
                    Log.d(TAG, "User exists in database");
                } catch (Exception e) {
                    Log.e(TAG, "Error checking user existence", e);
                    // Продолжаем попытку добавления, возможно проверка не критична
                }
                
                Favorite favorite = new Favorite();
                favorite.setUserId(userId);
                favorite.setBookId(bookId);
                
                SupabaseHelpers.insertFavorite(postgrest, "favorites", favorite);
                Log.d(TAG, "Successfully added to favorites");
                future.complete(true);
            } catch (Exception e) {
                Log.e(TAG, "Error adding to favorites - User ID: " + userId + ", Book ID: " + bookId, e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
    
    public CompletableFuture<Boolean> removeFromFavorites(String userId, String bookId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                Map<String, String> conditions = new HashMap<>();
                conditions.put("user_id", userId);
                conditions.put("book_id", bookId);
                SupabaseHelpers.deleteWhereMultiple(postgrest, "favorites", conditions);
                future.complete(true);
            } catch (Exception e) {
                Log.e(TAG, "Error removing from favorites", e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
    
    public CompletableFuture<Boolean> isFavorite(String userId, String bookId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                Map<String, String> conditions = new HashMap<>();
                conditions.put("user_id", userId);
                conditions.put("book_id", bookId);
                List<Favorite> favorites = SupabaseHelpers.selectWhereMultipleFavorites(postgrest, "favorites", conditions);
                future.complete(favorites != null && !favorites.isEmpty());
            } catch (Exception e) {
                Log.e(TAG, "Error checking favorite", e);
                future.complete(false);
            }
        });
        
        return future;
    }
}

