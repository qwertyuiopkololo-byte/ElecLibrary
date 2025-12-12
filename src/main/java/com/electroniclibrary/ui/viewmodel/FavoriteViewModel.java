package com.electroniclibrary.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.electroniclibrary.data.model.Book;
import com.electroniclibrary.data.repository.FavoriteRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FavoriteViewModel extends AndroidViewModel {
    private FavoriteRepository favoriteRepository;
    private MutableLiveData<List<Book>> favoritesLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Boolean> loadingLiveData;
    
    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        favoriteRepository = FavoriteRepository.getInstance(application);
        favoritesLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
    }
    
    public LiveData<List<Book>> getFavorites() {
        return favoritesLiveData;
    }
    
    public LiveData<String> getError() {
        return errorLiveData;
    }
    
    public LiveData<Boolean> getLoading() {
        return loadingLiveData;
    }
    
    public void loadFavorites(String userId) {
        loadingLiveData.setValue(true);
        CompletableFuture<List<Book>> future = favoriteRepository.getFavorites(userId);
        future.thenAccept(books -> {
            favoritesLiveData.postValue(books);
            loadingLiveData.postValue(false);
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            loadingLiveData.postValue(false);
            return null;
        });
    }
    
    public void addToFavorites(String userId, String bookId) {
        CompletableFuture<Boolean> future = favoriteRepository.addToFavorites(userId, bookId);
        future.thenAccept(success -> {
            if (success) {
                loadFavorites(userId);
            }
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            return null;
        });
    }
    
    public void removeFromFavorites(String userId, String bookId) {
        CompletableFuture<Boolean> future = favoriteRepository.removeFromFavorites(userId, bookId);
        future.thenAccept(success -> {
            if (success) {
                loadFavorites(userId);
            }
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            return null;
        });
    }
    
    public CompletableFuture<Boolean> isFavorite(String userId, String bookId) {
        return favoriteRepository.isFavorite(userId, bookId);
    }
}

