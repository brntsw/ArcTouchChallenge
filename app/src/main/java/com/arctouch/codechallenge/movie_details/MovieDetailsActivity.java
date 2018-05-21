package com.arctouch.codechallenge.movie_details;

import android.app.ProgressDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.di.InjectionMovieDetailsPresenter;
import com.arctouch.codechallenge.extensions.DateExtensionsKt;
import com.arctouch.codechallenge.extensions.GlideExtensionsKt;
import com.arctouch.codechallenge.listener.ISnackBarActionListener;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.movie_details.presentation.MovieDetailsContract;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.arctouch.codechallenge.util.NetworkUtils;
import com.arctouch.codechallenge.util.ProgressDialogUtils;
import com.arctouch.codechallenge.util.SnackUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsContract.View, ISnackBarActionListener {

    @BindView(R.id.coordinator_details)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_context)
    ImageView imgContext;

    @BindView(R.id.img_poster)
    ImageView imgPoster;

    @BindView(R.id.img_adult)
    ImageView imgAdult;

    @BindView(R.id.tv_vote_average)
    TextView tvVoteAverage;

    @BindView(R.id.tv_genres)
    TextView tvGenres;

    @BindView(R.id.tv_movie_title)
    TextView tvMovieTitle;

    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;

    @BindView(R.id.tv_overview)
    TextView tvOverview;

    private ProgressDialog progress;
    private MovieDetailsContract.Presenter movieDetailsPresenter;
    private Bundle args;
    private Movie movie;
    private MovieImageUrlBuilder movieImageUrlBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.app_name);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        args = getIntent().getExtras();

        movieImageUrlBuilder = new MovieImageUrlBuilder();

        movieDetailsPresenter = InjectionMovieDetailsPresenter.inject(this, this, Schedulers.io(), AndroidSchedulers.mainThread());

        if(args != null && args.getParcelable(Movie.BUNDLE) == null) {
            if (savedInstanceState != null && savedInstanceState.getParcelable(Movie.BUNDLE) != null) {
                movie = savedInstanceState.getParcelable(Movie.BUNDLE);

                loadData();
            } else {
                loadServerData();
            }
        }
        else if(args != null){
            movie = args.getParcelable(Movie.BUNDLE);

            loadData();
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelable(Movie.BUNDLE, movie);

        super.onSaveInstanceState(savedInstanceState);
    }

    private void loadServerData(){
        if (args != null) {
            if(NetworkUtils.isNetworkAvailable(this)) {
                movieDetailsPresenter.getMovieDetails(Long.parseLong(String.valueOf(args.getInt(Movie.BUNDLE_ID))));
            }
            else{
                SnackUtils.showSnackBarWithActionListener(this, coordinatorLayout, "OK", getString(R.string.no_network),this);
            }
        }
    }

    private void loadData(){
        if(movie != null) {
            GlideExtensionsKt.loadUrl(imgContext, movieImageUrlBuilder.buildBackdropUrl(movie.backdropPath));
            GlideExtensionsKt.loadUrl(imgPoster, movieImageUrlBuilder.buildPosterUrl(movie.posterPath), true);

            tvMovieTitle.setText(movie.title);

            if(movie.adult){
                imgAdult.setVisibility(View.VISIBLE);
            }

            tvVoteAverage.setText(String.valueOf(movie.voteAverage));
            tvGenres.setText(getString(R.string.genres_placeholder, TextUtils.join(", ", movie.genres)));
            tvReleaseDate.setText(getString(R.string.release_date, DateExtensionsKt.convertToFormattedDate(movie.releaseDate)));
            tvOverview.setText(movie.overview);
        }
    }

    @Override
    public void onSuccessMovieDetails(Movie movie) {
        this.movie = movie;

        loadData();
    }

    @Override
    public void showProgress() {
        hideProgress();
        progress = ProgressDialogUtils.showLoadingDialog(this, "");
    }

    @Override
    public void hideProgress() {
        if (progress != null && progress.isShowing()) {
            progress.cancel();
        }
    }

    @Override
    public void onError(String msg) {
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSnackBarClicked() {
        loadServerData();
    }
}
