package com.arctouch.codechallenge.di;

import android.content.Context;

import com.arctouch.codechallenge.presentation.MoviesListContract;
import com.arctouch.codechallenge.presentation.MoviesListPresenter;

import io.reactivex.Scheduler;

public class InjectionMoviesListPresenter {

    public static MoviesListContract.Presenter inject(Context context, MoviesListContract.View view, Scheduler scheduler, Scheduler observer){
        return new MoviesListPresenter(context, view, scheduler, observer);
    }

}
