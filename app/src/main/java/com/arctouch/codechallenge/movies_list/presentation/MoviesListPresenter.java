package com.arctouch.codechallenge.movies_list.presentation;

import android.content.Context;
import android.util.Log;

import com.arctouch.codechallenge.BasePresenter;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.di.InjectionTmdbApi;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.GenreResponse;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class MoviesListPresenter extends BasePresenter<MoviesListContract.View> implements MoviesListContract.Presenter {

    private Context context;
    private Scheduler scheduler;
    private Scheduler observer;

    public MoviesListPresenter(Context context, MoviesListContract.View view, Scheduler scheduler, Scheduler observer) {
        super(view);
        this.context = context;
        this.scheduler = scheduler;
        this.observer = observer;
    }

    @Override
    public void getGenres(boolean hideProgress) {
        view.showProgress();

        InjectionTmdbApi.inject()
                .genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(scheduler)
                .observeOn(observer)
                .subscribe(new Observer<Response<GenreResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Response<GenreResponse> genreResponseResponse) {
                        if(hideProgress)
                            view.hideProgress();

                        if(genreResponseResponse.isSuccessful()){
                            if(genreResponseResponse.body() != null)
                                view.onSuccessGenres(genreResponseResponse.body().genres);
                        }
                        else{
                            if(genreResponseResponse.errorBody() != null){
                                try {
                                    Log.d("Error", Objects.requireNonNull(genreResponseResponse.errorBody()).string());
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

    @Override
    public void getMovies(boolean showProgress) {

        if(showProgress)
            view.showProgress();

        InjectionTmdbApi.inject()
                .upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1L, TmdbApi.DEFAULT_REGION)
                .subscribeOn(scheduler)
                .observeOn(observer)
                .subscribe(new Observer<Response<UpcomingMoviesResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Response<UpcomingMoviesResponse> upcomingMoviesResponseResponse) {
                        view.hideProgress();

                        if(upcomingMoviesResponseResponse.isSuccessful()){
                            if(upcomingMoviesResponseResponse.body() != null)
                                view.onSuccessMovies(upcomingMoviesResponseResponse.body().results);
                        }
                        else{
                            if(upcomingMoviesResponseResponse.errorBody() != null){
                                try {
                                    Log.d("Error", Objects.requireNonNull(upcomingMoviesResponseResponse.errorBody()).string());
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

    public void addGenresToMovies(List<Movie> movies){
        for (Movie movie : movies) {
            movie.genres = new ArrayList<>();
            for (Genre genre : Cache.getGenres()) {
                if (movie.genreIds.contains(genre.id)) {
                    movie.genres.add(genre);
                }
            }
        }
    }
}
