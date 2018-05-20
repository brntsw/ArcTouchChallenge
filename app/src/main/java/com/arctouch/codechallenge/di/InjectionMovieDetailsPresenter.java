package com.arctouch.codechallenge.di;

import android.content.Context;

import com.arctouch.codechallenge.movie_details.presentation.MovieDetailsContract;
import com.arctouch.codechallenge.movie_details.presentation.MovieDetailsPresenter;

import io.reactivex.Scheduler;

public class InjectionMovieDetailsPresenter {

    public static MovieDetailsContract.Presenter inject(Context context, MovieDetailsContract.View view, Scheduler scheduler, Scheduler observer){
        return new MovieDetailsPresenter(context, view, scheduler, observer);
    }

}
