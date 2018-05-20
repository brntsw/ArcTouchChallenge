package com.arctouch.codechallenge.movie_details.presentation;

import com.arctouch.codechallenge.BaseView;
import com.arctouch.codechallenge.model.Movie;

public interface MovieDetailsContract {

    interface View extends BaseView{
        void onSuccessMovieDetails(Movie movie);
    }

    interface Presenter{
        void getMovieDetails(long id);
    }

}
