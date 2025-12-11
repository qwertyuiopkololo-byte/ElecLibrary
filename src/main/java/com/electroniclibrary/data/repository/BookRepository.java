package com.electroniclibrary.data.repository;

import android.content.Context;
import android.util.Log;

import com.electroniclibrary.data.model.Book;
import com.electroniclibrary.data.model.Author;
import com.electroniclibrary.data.model.Genre;
import com.electroniclibrary.data.room.BookDao;
import com.electroniclibrary.data.room.AppDatabase;
import com.electroniclibrary.data.supabase.SupabaseClientHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.electroniclibrary.data.supabase.SupabaseHelpers;
import io.github.jan.supabase.postgrest.Postgrest;

public class BookRepository {
    private static final String TAG = "BookRepository";
    private static BookRepository instance;
    private Postgrest postgrest;
    private BookDao bookDao;
    private ExecutorService executorService;
    
    private BookRepository(Context context) {
        postgrest = SupabaseClientHelper.getInstance().getPostgrest();
        bookDao = AppDatabase.getInstance(context).bookDao();
        executorService = Executors.newFixedThreadPool(4);
    }
    
    public static synchronized BookRepository getInstance(Context context) {
        if (instance == null) {
            instance = new BookRepository(context);
        }
        return instance;
    }
    
    public CompletableFuture<List<Book>> getAllBooks() {
        CompletableFuture<List<Book>> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                // Fetch from Supabase
                try {
                    List<Book> books = SupabaseHelpers.selectAllBooks(postgrest, "books");
                    future.complete(books);
                } catch (Exception e) {
                    Log.e(TAG, "Error fetching books from Supabase", e);
                    future.completeExceptionally(e);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in getAllBooks", e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
    
    public CompletableFuture<List<Book>> searchBooks(String query) {
        CompletableFuture<List<Book>> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                List<Book> books = SupabaseHelpers.selectWhereLikeBooks(postgrest, "books", "title", "%" + query + "%");
                future.complete(books);
            } catch (Exception e) {
                Log.e(TAG, "Error searching books", e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
    
    public CompletableFuture<List<Book>> getBooksByGenre(String genreId) {
        CompletableFuture<List<Book>> future = new CompletableFuture<>();
        
        // Use a final array to hold the mutable genreId value
        final String[] genreIdRef = {genreId};
        
        executorService.execute(() -> {
            try {
                if (genreIdRef[0] == null || genreIdRef[0].isEmpty()) {
                    Log.w(TAG, "Genre ID is null or empty");
                    future.complete(new ArrayList<>());
                    return;
                }
                
                // Если это статическая категория (начинается с "static_"), 
                // нужно найти реальный ID жанра по имени
                if (genreIdRef[0].startsWith("static_")) {
                    Log.d(TAG, "Static category selected, trying to find real genre ID");
                    // Получаем индекс из static_ID
                    try {
                        int index = Integer.parseInt(genreIdRef[0].replace("static_", ""));
                        String[] categoryNames = {
                            "Фантастика", "Детектив", "Роман", "История",
                            "Классика", "Поэзия", "Биография"
                        };
                        
                        if (index >= 0 && index < categoryNames.length) {
                            String categoryName = categoryNames[index];
                            // Получаем реальный ID жанра из БД по имени
                            try {
                                List<com.electroniclibrary.data.model.Genre> genres = 
                                    SupabaseHelpers.selectAllGenres(postgrest, "genres");
                                if (genres != null && !genres.isEmpty()) {
                                    for (com.electroniclibrary.data.model.Genre genre : genres) {
                                        if (genre.getName().equals(categoryName)) {
                                            genreIdRef[0] = genre.getId();
                                            Log.d(TAG, "Found real genre ID: " + genreIdRef[0] + " for category: " + categoryName);
                                            break;
                                        }
                                    }
                                } else {
                                    Log.w(TAG, "No genres found in database, cannot filter books");
                                    future.complete(new ArrayList<>());
                                    return;
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error getting genres for static category", e);
                                future.complete(new ArrayList<>());
                                return;
                            }
                        } else {
                            Log.w(TAG, "Invalid static category index: " + index);
                            future.complete(new ArrayList<>());
                            return;
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Invalid static category ID format: " + genreIdRef[0], e);
                        future.complete(new ArrayList<>());
                        return;
                    }
                }
                
                // Фильтруем книги по genre_id
                Log.d(TAG, "Filtering books by genre_id: " + genreIdRef[0]);
                List<Book> books = SupabaseHelpers.selectWhereBooks(postgrest, "books", "genre_id", genreIdRef[0]);
                Log.d(TAG, "Found " + (books != null ? books.size() : 0) + " books for genre " + genreIdRef[0]);
                
                // Если не найдено книг, проверяем, может быть проблема с фильтрацией
                if (books == null || books.isEmpty()) {
                    Log.w(TAG, "No books found for genre_id: " + genreIdRef[0] + ". Checking if books have genre_id set...");
                }
                
                future.complete(books != null ? books : new ArrayList<>());
            } catch (Exception e) {
                Log.e(TAG, "Error getting books by genre", e);
                // В случае ошибки возвращаем пустой список вместо исключения
                future.complete(new ArrayList<>());
            }
        });
        
        return future;
    }
    
    public CompletableFuture<List<Book>> getBooksByAuthor(String authorId) {
        CompletableFuture<List<Book>> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                List<Book> books = SupabaseHelpers.selectWhereBooks(postgrest, "books", "author_id", authorId);
                future.complete(books);
            } catch (Exception e) {
                Log.e(TAG, "Error getting books by author", e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
    
    public CompletableFuture<Book> getBookById(String bookId) {
        CompletableFuture<Book> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                Book book = SupabaseHelpers.selectSingleBook(postgrest, "books", "id", bookId);
                future.complete(book);
            } catch (Exception e) {
                Log.e(TAG, "Error getting book by id", e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
    
    public CompletableFuture<List<Genre>> getAllGenres() {
        CompletableFuture<List<Genre>> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                List<Genre> genres = SupabaseHelpers.selectAllGenres(postgrest, "genres");
                if (genres != null && !genres.isEmpty()) {
                    future.complete(genres);
                } else {
                    // Если таблица пуста или не существует, возвращаем статический список категорий
                    Log.w(TAG, "Genres table is empty or doesn't exist, using static categories");
                    future.complete(getStaticGenres());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting genres, using static categories", e);
                // В случае ошибки возвращаем статический список категорий
                future.complete(getStaticGenres());
            }
        });
        
        return future;
    }
    
    private List<Genre> getStaticGenres() {
        List<Genre> genres = new ArrayList<>();
        
        // Создаем статический список категорий (только используемые)
        String[] categoryNames = {
            "Фантастика", "Детектив", "Роман", "История",
            "Классика", "Поэзия", "Биография"
        };
        
        String[] categoryDescriptions = {
            "Научная фантастика и фэнтези",
            "Детективные романы и триллеры",
            "Романтические произведения",
            "Исторические произведения",
            "Классическая литература",
            "Поэтические произведения",
            "Биографические произведения"
        };
        
        for (int i = 0; i < categoryNames.length; i++) {
            Genre genre = new Genre();
            genre.setId("static_" + i); // Временный ID для статических категорий
            genre.setName(categoryNames[i]);
            genre.setDescription(categoryDescriptions[i]);
            genres.add(genre);
        }
        
        return genres;
    }
    
    public CompletableFuture<List<Author>> getAllAuthors() {
        CompletableFuture<List<Author>> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                List<Author> authors = SupabaseHelpers.selectAllAuthors(postgrest, "authors");
                future.complete(authors);
            } catch (Exception e) {
                Log.e(TAG, "Error getting authors", e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
    
    public CompletableFuture<List<Book>> getPopularBooks(int limit) {
        CompletableFuture<List<Book>> future = new CompletableFuture<>();
        
        executorService.execute(() -> {
            try {
                List<Book> books = SupabaseHelpers.selectPopularBooks(postgrest, "books", limit);
                future.complete(books);
            } catch (Exception e) {
                Log.e(TAG, "Error getting popular books", e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
}

