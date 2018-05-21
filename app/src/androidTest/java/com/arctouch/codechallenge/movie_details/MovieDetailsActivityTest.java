package com.arctouch.codechallenge.movie_details;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.arctouch.codechallenge.Mock;
import com.arctouch.codechallenge.OkHttp3IdlingResource;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.movies_list.MoviesListActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@RunWith(AndroidJUnit4.class)
public class MovieDetailsActivityTest {

    @Rule
    public ActivityTestRule<MovieDetailsActivity> activityTestRule =
            new ActivityTestRule<>(MovieDetailsActivity.class, false, false);

    @Before
    public void setup(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = TmdbApi.Builder.getOkHttpClient(interceptor);
        IdlingResource idlingResource = OkHttp3IdlingResource.create("OkHttpClient", okHttpClient);

        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void screenContentTest(){

        Intent i = new Intent();
        i.putExtra(Movie.BUNDLE, Mock.getMockMovies().get(0));
        activityTestRule.launchActivity(i);

        onView(withId(R.id.tv_movie_title)).check(matches(withText("Gnomeu e Julieta: O Mistério do Jardim")));
        onView(withId(R.id.tv_vote_average)).check(matches(withText("5.3")));
        onView(withId(R.id.img_adult)).check(matches(isDisplayed()));

    }

}
