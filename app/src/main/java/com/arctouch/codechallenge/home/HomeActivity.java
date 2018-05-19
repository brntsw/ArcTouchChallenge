package com.arctouch.codechallenge.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.presentation.MoviesListContract;
import com.arctouch.codechallenge.presentation.MoviesListPresenter;
import com.arctouch.codechallenge.util.NetworkUtils;
import com.arctouch.codechallenge.util.SnackUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements MoviesListContract.View {

    @BindView(R.id.coordinator_main)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerMovies;

    private MoviesListPresenter moviesListPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        ButterKnife.bind(this);

        moviesListPresenter = new MoviesListPresenter(this, this, Schedulers.io(), AndroidSchedulers.mainThread());

        moviesListPresenter.getGenres(false);

        swipeRefresh.setOnRefreshListener(() -> {
            if(NetworkUtils.isNetworkAvailable(this)) {
                moviesListPresenter.getMovies(true);
            }
            else{
                SnackUtils.showSnackbarWithAction(this, coordinatorLayout, "OK", "No network conection, try again later");
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
        moviesListPresenter.addGenresToMovies(movies);

        recyclerMovies.setAdapter(new HomeAdapter(movies));
    }
}
