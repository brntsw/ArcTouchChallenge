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
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.movie_details.presentation.MovieDetailsContract;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.arctouch.codechallenge.util.ProgressDialogUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsContract.View {

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
    private int movieId;
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

        movieImageUrlBuilder = new MovieImageUrlBuilder();

        movieDetailsPresenter = InjectionMovieDetailsPresenter.inject(this, this, Schedulers.io(), AndroidSchedulers.mainThread());

        args = getIntent().getExtras();

        if(args != null){
            movieId = args.getInt(Movie.BUNDLE_ID);

            movieDetailsPresenter.getMovieDetails(Long.parseLong(String.valueOf(movieId)));
        }
    }

    @Override
    public void onSuccessMovieDetails(Movie movie) {
        if(movie != null) {
            GlideExtensionsKt.loadUrl(imgContext, movieImageUrlBuilder.buildBackdropUrl(movie.backdropPath));
            GlideExtensionsKt.loadUrl(imgPoster, movieImageUrlBuilder.buildPosterUrl(movie.posterPath), true);

            tvMovieTitle.setText(movie.title);

            if(movie.adult){
                imgAdult.setVisibility(View.VISIBLE);
            }

            tvVoteAverage.setText(String.valueOf(movie.voteAverage));
            tvGenres.setText(TextUtils.join(", ", movie.genres));
            tvReleaseDate.setText(getString(R.string.release_date, DateExtensionsKt.convertToFormattedDate(movie.releaseDate)));
            tvOverview.setText(movie.overview);
        }
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
}
