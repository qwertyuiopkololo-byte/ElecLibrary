package com.electroniclibrary.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.electroniclibrary.R;
import com.electroniclibrary.data.model.Genre;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private List<Genre> genres;
    private OnGenreClickListener listener;
    
    public interface OnGenreClickListener {
        void onGenreClick(Genre genre);
    }
    
    public GenreAdapter(List<Genre> genres, OnGenreClickListener listener) {
        this.genres = genres;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.bind(genre);
    }
    
    @Override
    public int getItemCount() {
        return genres != null ? genres.size() : 0;
    }
    
    public void updateGenres(List<Genre> newGenres) {
        this.genres = newGenres;
        notifyDataSetChanged();
    }
    
    class GenreViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGenreName;
        
        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenreName = itemView.findViewById(R.id.tvGenreName);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onGenreClick(genres.get(position));
                }
            });
        }
        
        public void bind(Genre genre) {
            tvGenreName.setText(genre.getName());
        }
    }
}

