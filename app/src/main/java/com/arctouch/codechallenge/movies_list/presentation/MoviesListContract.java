package com.arctouch.codechallenge.movies_list.presentation;

import com.arctouch.codechallenge.BaseView;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;
import java.util.List;

public interface MoviesListContract {

    interface View extends BaseView {
        void onSuccessGenres(List<Genre> genres);
        void onSuccessMovies(ArrayList<Movie> movies);
        void showMoreMovies(List<Movie> movies);
    }

    interface Presenter{
        void getGenres(boolean hideProgress);
        void getMovies(int page, boolean showProgress);
        void getMoviesByName(int page, String name);
        void addGenresToMovies(List<Movie> movies);
    }

}
