package com.arctouch.codechallenge.movies_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.listener.EndlessRecyclerViewScrollListener;
import com.arctouch.codechallenge.listener.IMovieClickListener;
import com.arctouch.codechallenge.listener.ISearchViewTextSubmitListener;
import com.arctouch.codechallenge.listener.ISnackBarActionListener;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.movie_details.MovieDetailsActivity;
import com.arctouch.codechallenge.movies_list.presentation.MoviesListContract;
import com.arctouch.codechallenge.movies_list.presentation.MoviesListPresenter;
import com.arctouch.codechallenge.util.MaterialSearchViewHelper;
import com.arctouch.codechallenge.util.NetworkUtils;
import com.arctouch.codechallenge.util.SnackUtils;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MoviesListActivity extends AppCompatActivity implements MoviesListContract.View, IMovieClickListener, ISearchViewTextSubmitListener, ISnackBarActionListener {

    @BindView(R.id.coordinator_main)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerMovies;

    private MoviesListPresenter moviesListPresenter;
    private MoviesListAdapter adapter;
    private ArrayList<Movie> movieList;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private String searchedName;
    private int currentPage;
    private Menu menu;

    private static final String BUNDLE_SEARCHED_NAME = "search_by_name";
    private static final String BUNDLE_CURRENT_PAGE = "current_page";

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

        searchedName = "";
        currentPage = 1;

        if(savedInstanceState != null){
            if(!"".equals(savedInstanceState.getString(BUNDLE_SEARCHED_NAME))) {
                searchedName = savedInstanceState.getString(BUNDLE_SEARCHED_NAME);
            }

            if(savedInstanceState.getInt(BUNDLE_CURRENT_PAGE) > 0){
                currentPage = savedInstanceState.getInt(BUNDLE_CURRENT_PAGE);
            }
        }

        searchView.setVoiceSearch(true);
        searchView.setHint(getString(R.string.search_movie_name));
        searchView.setOnQueryTextListener(MaterialSearchViewHelper.getQueryTextListener(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerMovies.setLayoutManager(linearLayoutManager);

        moviesListPresenter = new MoviesListPresenter(this, this, Schedulers.io(), AndroidSchedulers.mainThread());

        if(savedInstanceState != null && savedInstanceState.getParcelableArrayList(Movie.BUNDLE_LIST) != null){
            movieList = savedInstanceState.getParcelableArrayList(Movie.BUNDLE_LIST);
            adapter = new MoviesListAdapter(movieList);
            adapter.setMovieClickListener(this);
            recyclerMovies.setAdapter(adapter);
        }
        else{
            if(NetworkUtils.isNetworkAvailable(this)) {
                moviesListPresenter.getGenres(false);
            }
            else{
                SnackUtils.showSnackBarWithActionListener(this, coordinatorLayout, "OK", getString(R.string.no_network),this);
            }
        }

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(currentPage < page) {
                    currentPage = page;
                }

                currentPage += 1;

                loadData();
            }
        };
        recyclerMovies.addOnScrollListener(endlessRecyclerViewScrollListener);

        swipeRefresh.setOnRefreshListener(() -> {
            currentPage = 1;

            loadData();
        });
    }

    private void loadData(){
        if(NetworkUtils.isNetworkAvailable(this)) {
            if (!"".equals(searchedName)) {
                moviesListPresenter.getMoviesByName(currentPage, searchedName);
            } else {
                moviesListPresenter.getMovies(currentPage, true);
            }
        }
        else{
            SnackUtils.showSnackBarWithActionListener(this, coordinatorLayout, "OK", getString(R.string.no_network),this);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        moviesListPresenter.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies_list, menu);

        this.menu = menu;

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        if(!"".equals(searchedName)){
            item.setVisible(false);
            menu.findItem(R.id.action_clear).setVisible(true);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                menu.findItem(R.id.action_search).setVisible(true);
                item.setVisible(false);
                searchedName = "";
                currentPage = 1;
                moviesListPresenter.getMovies(1,true);
                return true;
        }

        return false;
    }

    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelableArrayList(Movie.BUNDLE_LIST, movieList);
        savedInstanceState.putString(BUNDLE_SEARCHED_NAME, searchedName);
        savedInstanceState.putInt(BUNDLE_CURRENT_PAGE, currentPage);

        super.onSaveInstanceState(savedInstanceState);
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

        moviesListPresenter.getMovies(1,false);
    }

    @Override
    public void onSuccessMovies(ArrayList<Movie> movies) {
        adapter = new MoviesListAdapter(movies);
        adapter.setMovieClickListener(this);

        moviesListPresenter.addGenresToMovies(movies);

        movieList = movies;

        recyclerMovies.setAdapter(adapter);

        endlessRecyclerViewScrollListener.resetState();
    }

    @Override
    public void showMoreMovies(List<Movie> movies) {
        if(movies.size() > 0) {
            int sizeBeforeNewItems = movieList.size();
            movieList.addAll(movies);

            adapter.notifyItemRangeInserted(sizeBeforeNewItems, movieList.size());
        }
        else{
            Snackbar.make(coordinatorLayout, getString(R.string.no_more_movies), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(Movie.BUNDLE_ID, movie.id);
        startActivity(intent);
    }

    public void onSearchTextSubmit(String text){
        if(!"".equals(text)) {
            moviesListPresenter.stop();

            searchedName = text;
            currentPage = 1;

            moviesListPresenter.getMoviesByName(1, text);

            if(menu != null) {
                menu.findItem(R.id.action_search).setVisible(false);
                menu.findItem(R.id.action_clear).setVisible(true);
            }
        }
    }

    public void onSnackBarClicked(){
        loadData();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
            searchedName = "";

        } else {
            super.onBackPressed();
        }
    }
}
