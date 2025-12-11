package com.electroniclibrary.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.electroniclibrary.data.model.Book;
import com.electroniclibrary.data.model.Genre;
import com.electroniclibrary.data.repository.BookRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BookViewModel extends AndroidViewModel {
    private BookRepository bookRepository;
    private MutableLiveData<List<Book>> booksLiveData;
    private MutableLiveData<List<Genre>> genresLiveData;
    private MutableLiveData<Book> selectedBookLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Boolean> loadingLiveData;
    
    public BookViewModel(@NonNull Application application) {
        super(application);
        bookRepository = BookRepository.getInstance(application);
        booksLiveData = new MutableLiveData<>();
        genresLiveData = new MutableLiveData<>();
        selectedBookLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
    }
    
    public LiveData<List<Book>> getBooks() {
        return booksLiveData;
    }
    
    public LiveData<List<Genre>> getGenres() {
        return genresLiveData;
    }
    
    public LiveData<Book> getSelectedBook() {
        return selectedBookLiveData;
    }
    
    public LiveData<String> getError() {
        return errorLiveData;
    }
    
    public LiveData<Boolean> getLoading() {
        return loadingLiveData;
    }
    
    public void loadAllBooks() {
        loadingLiveData.setValue(true);
        CompletableFuture<List<Book>> future = bookRepository.getAllBooks();
        future.thenAccept(books -> {
            booksLiveData.postValue(books);
            loadingLiveData.postValue(false);
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            loadingLiveData.postValue(false);
            return null;
        });
    }
    
    public void searchBooks(String query) {
        loadingLiveData.setValue(true);
        CompletableFuture<List<Book>> future = bookRepository.searchBooks(query);
        future.thenAccept(books -> {
            booksLiveData.postValue(books);
            loadingLiveData.postValue(false);
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            loadingLiveData.postValue(false);
            return null;
        });
    }
    
    public void loadBooksByGenre(String genreId) {
        loadingLiveData.setValue(true);
        CompletableFuture<List<Book>> future = bookRepository.getBooksByGenre(genreId);
        future.thenAccept(books -> {
            booksLiveData.postValue(books);
            loadingLiveData.postValue(false);
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            loadingLiveData.postValue(false);
            return null;
        });
    }
    
    public void loadBooksByAuthor(String authorId) {
        loadingLiveData.setValue(true);
        CompletableFuture<List<Book>> future = bookRepository.getBooksByAuthor(authorId);
        future.thenAccept(books -> {
            booksLiveData.postValue(books);
            loadingLiveData.postValue(false);
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            loadingLiveData.postValue(false);
            return null;
        });
    }
    
    public void loadBookById(String bookId) {
        loadingLiveData.setValue(true);
        CompletableFuture<Book> future = bookRepository.getBookById(bookId);
        future.thenAccept(book -> {
            selectedBookLiveData.postValue(book);
            loadingLiveData.postValue(false);
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            loadingLiveData.postValue(false);
            return null;
        });
    }
    
    public void loadGenres() {
        loadingLiveData.setValue(true);
        CompletableFuture<List<Genre>> future = bookRepository.getAllGenres();
        future.thenAccept(genres -> {
            genresLiveData.postValue(genres);
            loadingLiveData.postValue(false);
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            loadingLiveData.postValue(false);
            return null;
        });
    }
    
    public void loadPopularBooks(int limit) {
        loadingLiveData.setValue(true);
        CompletableFuture<List<Book>> future = bookRepository.getPopularBooks(limit);
        future.thenAccept(books -> {
            booksLiveData.postValue(books);
            loadingLiveData.postValue(false);
        }).exceptionally(throwable -> {
            errorLiveData.postValue(throwable.getMessage());
            loadingLiveData.postValue(false);
            return null;
        });
    }
}

