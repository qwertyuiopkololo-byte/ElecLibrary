package com.electroniclibrary.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.electroniclibrary.data.model.User;
import com.electroniclibrary.data.supabase.SupabaseClientHelper;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.electroniclibrary.data.supabase.SupabaseHelpers;
import io.github.jan.supabase.postgrest.Postgrest;

public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_USER = "current_user";
    private static final String KEY_SESSION = "session_token";
    
    private static AuthRepository instance;
    private Postgrest postgrest;
    private SharedPreferences prefs;
    private ExecutorService executorService;
    
    private AuthRepository(Context context) {
        postgrest = SupabaseClientHelper.getInstance().getPostgrest();
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        executorService = Executors.newFixedThreadPool(2);
    }
    
    public static synchronized AuthRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AuthRepository(context);
        }
        return instance;
    }
    
    public CompletableFuture<User> register(String username, String password, String firstName, String lastName) {
        CompletableFuture<User> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                // Check if username already exists
                List<User> existing = SupabaseHelpers.selectWhereUsers(postgrest, "users", "username", username);
                if (existing != null && !existing.isEmpty()) {
                    throw new Exception("Username already taken");
                }

                User user = new User();
                user.setUsername(username);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setRole("client");
                user.setEmail(username); // email not used; store username as email placeholder
                user.setPassword(password);

                User createdUser = SupabaseHelpers.insertUser(postgrest, "users", user);
                
                // Проверяем, что пользователь получил ID
                if (createdUser.getId() == null || createdUser.getId().isEmpty()) {
                    Log.e(TAG, "User created but ID is null or empty");
                    throw new Exception("Failed to get user ID after registration");
                }
                
                Log.d(TAG, "User registered successfully with ID: " + createdUser.getId());

                saveUser(createdUser);
                future.complete(createdUser);
            } catch (Exception e) {
                Log.e(TAG, "Error registering user", e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
    
    public CompletableFuture<User> login(String username, String password) {
        CompletableFuture<User> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                // Get user from database by username
                List<User> users = SupabaseHelpers.selectWhereUsers(postgrest, "users", "username", username);

                if (users != null && !users.isEmpty()) {
                    User user = users.get(0);
                    if (user.getPassword() != null && user.getPassword().equals(password)) {
                        // Проверяем, что пользователь имеет ID
                        if (user.getId() == null || user.getId().isEmpty()) {
                            Log.e(TAG, "User logged in but ID is null or empty");
                            future.completeExceptionally(new Exception("User ID not found. Please contact support."));
                            return;
                        }
                        
                        Log.d(TAG, "User logged in successfully with ID: " + user.getId());
                        saveUser(user);
                        future.complete(user);
                    } else {
                        future.completeExceptionally(new Exception("Invalid password"));
                    }
                } else {
                    future.completeExceptionally(new Exception("User not found in database"));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error logging in", e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
    
    public void logout() {
        clearUser();
    }
    
    public User getCurrentUser() {
        String userJson = prefs.getString(KEY_USER, null);
        if (userJson != null) {
            return new Gson().fromJson(userJson, User.class);
        }
        return null;
    }
    
    private void saveUser(User user) {
        String userJson = new Gson().toJson(user);
        prefs.edit().putString(KEY_USER, userJson).apply();
    }
    
    private void clearUser() {
        prefs.edit().remove(KEY_USER).remove(KEY_SESSION).apply();
    }
    
    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }
}

