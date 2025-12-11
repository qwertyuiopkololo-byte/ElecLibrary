package com.electroniclibrary.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.electroniclibrary.R;
import com.electroniclibrary.data.model.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books;
    private OnBookClickListener listener;
    
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }
    
    public BookAdapter(List<Book> books, OnBookClickListener listener) {
        this.books = books;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }
    
    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }
    
    public void updateBooks(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }
    
    class BookViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCover;
        private TextView tvTitle, tvAuthor, tvGenre, tvRating;
        
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvGenre = itemView.findViewById(R.id.tvGenre);
            tvRating = itemView.findViewById(R.id.tvRating);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBookClick(books.get(position));
                }
            });
        }
        
        public void bind(Book book) {
            tvTitle.setText(book.getTitle());
            
            if (book.getAuthor() != null) {
                tvAuthor.setText(book.getAuthor().getFullName());
            } else {
                tvAuthor.setText("Неизвестный автор");
            }
            
            if (book.getGenre() != null) {
                tvGenre.setText(book.getGenre().getName());
            } else {
                tvGenre.setText("Без жанра");
            }
            
            if (book.getRating() != null) {
                tvRating.setText(String.format("%.1f", book.getRating()));
            } else {
                tvRating.setText("—");
            }
            
            if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                    .load(book.getCoverUrl())
                    .placeholder(R.drawable.ic_book_placeholder)
                    .into(ivCover);
            }
        }
    }
}

