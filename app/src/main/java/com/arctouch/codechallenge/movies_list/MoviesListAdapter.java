package com.arctouch.codechallenge.movies_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.extensions.DateExtensionsKt;
import com.arctouch.codechallenge.extensions.GlideExtensionsKt;
import com.arctouch.codechallenge.listener.IMovieClickListener;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {

    private List<Movie> movies;
    private IMovieClickListener movieClickListener;

    MoviesListAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    void setMovieClickListener(IMovieClickListener movieClickListener) {
        this.movieClickListener = movieClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

        @BindView(R.id.card_view)
        CardView cardView;

        @BindView(R.id.titleTextView)
        TextView titleTextView;

        @BindView(R.id.genresTextView)
        TextView genresTextView;

        @BindView(R.id.releaseDateTextView)
        TextView releaseDateTextView;

        @BindView(R.id.posterImageView)
        ImageView posterImageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Movie movie) {
            titleTextView.setText(movie.title);

            if(movie.genres != null)
                genresTextView.setText(TextUtils.join(", ", movie.genres));

            if(movie.releaseDate != null && !"".equals(movie.releaseDate))
                releaseDateTextView.setText(DateExtensionsKt.convertToFormattedDate(movie.releaseDate));

            String posterPath = movie.posterPath;
            if (!TextUtils.isEmpty(posterPath)) {
                GlideExtensionsKt.loadUrl(posterImageView, movieImageUrlBuilder.buildPosterUrl(posterPath), true);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public int getItemViewType(int position){
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        if(movie != null) {
            holder.bind(movie);

            holder.cardView.setOnClickListener(view -> movieClickListener.onMovieClicked(movie));
        }
    }
}
