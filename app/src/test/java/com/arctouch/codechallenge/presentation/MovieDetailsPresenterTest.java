package com.arctouch.codechallenge.presentation;

import android.content.Context;

import com.arctouch.codechallenge.di.InjectionMovieDetailsPresenter;
import com.arctouch.codechallenge.movie_details.presentation.MovieDetailsContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.schedulers.Schedulers;

public class MovieDetailsPresenterTest {

    @Mock
    private Context context;

    @Mock
    private MovieDetailsContract.View view;

    private MovieDetailsContract.Presenter presenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        presenter = InjectionMovieDetailsPresenter.inject(context, view, Schedulers.trampoline(), Schedulers.trampoline());
    }

    @Test
    public void callGetMovieDetailsTest(){
        presenter.getMovieDetails(370567);
    }

}
