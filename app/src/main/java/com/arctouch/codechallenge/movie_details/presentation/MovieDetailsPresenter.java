package com.arctouch.codechallenge.movie_details.presentation;

import android.content.Context;
import android.util.Log;

import com.arctouch.codechallenge.BasePresenter;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.di.InjectionTmdbApi;
import com.arctouch.codechallenge.model.Movie;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class MovieDetailsPresenter extends BasePresenter<MovieDetailsContract.View> implements MovieDetailsContract.Presenter {

    private Context context;
    private Scheduler scheduler;
    private Scheduler observer;

    public MovieDetailsPresenter(Context context, MovieDetailsContract.View view, Scheduler scheduler, Scheduler observer) {
        super(view);
        this.context = context;
        this.scheduler = scheduler;
        this.observer = observer;
    }

    @Override
    public void getMovieDetails(long id) {
        view.showProgress();

        InjectionTmdbApi.inject()
                .movie(id, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(scheduler)
                .observeOn(observer)
                .subscribe(new Observer<Response<Movie>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Response<Movie> movieResponse) {
                        view.hideProgress();

                        if(movieResponse.isSuccessful()){
                            if(movieResponse.body() != null)
                                view.onSuccessMovieDetails(movieResponse.body());
                        }
                        else{
                            if(movieResponse.errorBody() != null){
                                try {
                                    Log.d("Error", Objects.requireNonNull(movieResponse.errorBody()).string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                view.onError(context.getString(R.string.general_error));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProgress();

                        if(e != null)
                            Log.e("onError", e.getMessage());

                        if (e instanceof SocketTimeoutException) {
                            view.onError(context.getString(R.string.timeout_error));
                        } else {
                            view.onError(context.getString(R.string.general_error));
                        }
                    }

                    @Override
                    public void onComplete() { }
                });
    }
}
