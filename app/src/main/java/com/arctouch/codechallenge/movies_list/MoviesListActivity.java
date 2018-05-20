package com.arctouch.codechallenge.movies_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.listener.IMovieClickListener;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.movie_details.MovieDetailsActivity;
import com.arctouch.codechallenge.movies_list.presentation.MoviesListContract;
import com.arctouch.codechallenge.movies_list.presentation.MoviesListPresenter;
import com.arctouch.codechallenge.util.NetworkUtils;
import com.arctouch.codechallenge.util.SnackUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MoviesListActivity extends AppCompatActivity implements MoviesListContract.View, IMovieClickListener {

    @BindView(R.id.coordinator_main)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerMovies;

    private MoviesListPresenter moviesListPresenter;
    private MoviesListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.app_name);
        }

        moviesListPresenter = new MoviesListPresenter(this, this, Schedulers.io(), AndroidSchedulers.mainThread());

        moviesListPresenter.getGenres(false);

        swipeRefresh.setOnRefreshListener(() -> {
            if(NetworkUtils.isNetworkAvailable(this)) {
                moviesListPresenter.getMovies(true);
            }
            else{
                SnackUtils.showSnackbarWithAction(this, coordinatorLayout, "OK", getString(R.string.no_network));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        moviesListPresenter.stop();
    }

    @Override
    public void showProgress() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onError(String msg) {
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessGenres(List<Genre> genres) {
        Cache.setGenres(genres);

        moviesListPresenter.getMovies(false);
    }

    @Override
    public void onSuccessMovies(List<Movie> movies) {
        adapter = new MoviesListAdapter(movies);
        adapter.setMovieClickListener(this);

        moviesListPresenter.addGenresToMovies(movies);

        recyclerMovies.setAdapter(adapter);
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(Movie.BUNDLE_ID, movie.id);
        startActivity(intent);
    }
}
