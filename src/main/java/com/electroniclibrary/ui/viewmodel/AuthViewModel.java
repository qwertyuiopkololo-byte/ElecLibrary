package com.electroniclibrary.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.electroniclibrary.data.model.User;
import com.electroniclibrary.data.repository.AuthRepository;

import java.util.concurrent.CompletableFuture;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<User> currentUserLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Boolean> loadingLiveData;
    
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = AuthRepository.getInstance(application);
        currentUserLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
        
        // Load current user if exists
        User currentUser = authRepository.getCurrentUser();
        if (currentUser != null) {
            currentUserLiveData.setValue(currentUser);
        }
    }
    
    public LiveData<User> getCurrentUser() {
        return currentUserLiveData;
    }
    
    public LiveData<String> getError() {
        return errorLiveData;
    }
    
    public LiveData<Boolean> getLoading() {
        return loadingLiveData;
    }
    
    public void register(String username, String password, String firstName, String lastName) {
        loadingLiveData.setValue(true);
        CompletableFuture<User> future = authRepository.register(username, password, firstName, lastName);
        future.thenAccept(user -> {
            currentUserLiveData.postValue(user);
            loadingLiveData.postValue(false);
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            loadingLiveData.postValue(false);
            return null;
        });
    }
    
    public void login(String username, String password) {
        loadingLiveData.setValue(true);
        CompletableFuture<User> future = authRepository.login(username, password);
        future.thenAccept(user -> {
            currentUserLiveData.postValue(user);
            loadingLiveData.postValue(false);
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            loadingLiveData.postValue(false);
            return null;
        });
    }
    
    public void logout() {
        authRepository.logout();
        currentUserLiveData.setValue(null);
    }
    
    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }
}

