package com.arctouch.codechallenge;

import android.content.Context;

import com.arctouch.codechallenge.di.InjectionMoviesListPresenter;
import com.arctouch.codechallenge.presentation.MoviesListContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.schedulers.Schedulers;

public class MoviesListPresenterTest {

    @Mock
    private Context context;

    @Mock
    private MoviesListContract.View view;

    private MoviesListContract.Presenter moviesListPresenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        moviesListPresenter = InjectionMoviesListPresenter.inject(context, view, Schedulers.trampoline(), Schedulers.trampoline());
    }

    @Test
    public void callGetMoviesListTest(){
        moviesListPresenter.getMovies(true);
    }

    @Test
    public void callGetGenresTest(){
        moviesListPresenter.getGenres(true);
    }

}
