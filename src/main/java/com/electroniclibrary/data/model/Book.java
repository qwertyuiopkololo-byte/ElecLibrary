package com.electroniclibrary.data.model;

import androidx.room.Ignore;
import com.google.gson.annotations.SerializedName;

public class Book {
    @SerializedName("id")
    private String id;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("author_id")
    private String authorId;
    
    @SerializedName("genre_id")
    private String genreId;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("content")
    private String content;
    
    @SerializedName("cover_url")
    private String coverUrl;
    
    @SerializedName("file_url")
    private String fileUrl;
    
    @SerializedName("rating")
    private Double rating;
    
    @SerializedName("rating_count")
    private Integer ratingCount;
    
    @SerializedName("author")
    @Ignore
    private Author author;
    
    @SerializedName("genre")
    @Ignore
    private Genre genre;
    
    @SerializedName("is_favorite")
    @Ignore
    private Boolean isFavorite;
    
    public Book() {
    }
    
    public Book(String id, String title, String authorId, String genreId, String description) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
        this.description = description;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthorId() {
        return authorId;
    }
    
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
    
    public String getGenreId() {
        return genreId;
    }
    
    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getCoverUrl() {
        return coverUrl;
    }
    
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
    
    public String getFileUrl() {
        return fileUrl;
    }
    
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    
    public Double getRating() {
        return rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
    }
    
    public Integer getRatingCount() {
        return ratingCount;
    }
    
    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }
    
    public Author getAuthor() {
        return author;
    }
    
    public void setAuthor(Author author) {
        this.author = author;
    }
    
    public Genre getGenre() {
        return genre;
    }
    
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    
    public Boolean getIsFavorite() {
        return isFavorite != null && isFavorite;
    }
    
    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}

