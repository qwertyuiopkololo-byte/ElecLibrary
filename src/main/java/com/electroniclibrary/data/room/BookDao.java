package com.electroniclibrary.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.electroniclibrary.data.room.BookEntity;

import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM books")
    List<BookEntity> getAllBooks();
    
    @Query("SELECT * FROM books WHERE id = :bookId")
    BookEntity getBookById(String bookId);
    
    @Query("SELECT * FROM books WHERE genreId = :genreId")
    List<BookEntity> getBooksByGenre(String genreId);
    
    @Query("SELECT * FROM books WHERE authorId = :authorId")
    List<BookEntity> getBooksByAuthor(String authorId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BookEntity> books);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookEntity book);
    
    @Query("DELETE FROM books")
    void deleteAll();
}

