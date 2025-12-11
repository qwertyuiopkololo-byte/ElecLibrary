package com.electroniclibrary.data.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.electroniclibrary.data.room.converters.DateConverter;

@Entity(tableName = "books")
@TypeConverters({DateConverter.class})
public class BookEntity {
    @PrimaryKey
    @NonNull
    public String id;
    
    public String title;
    public String authorId;
    public String genreId;
    public String description;
    public String coverUrl;
    public String fileUrl;
    public Double rating;
    public Integer ratingCount;
    
    public BookEntity() {
    }
}

