package com.arctouch.codechallenge.movies_list;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.arctouch.codechallenge.Mock;
import com.arctouch.codechallenge.OkHttp3IdlingResource;
import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.RecyclerViewMatcher;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.movies_list.MoviesListActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.click;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@RunWith(AndroidJUnit4.class)
public class MoviesListActivityTest {

    @Rule
    public ActivityTestRule<MoviesListActivity> activityTestRule =
            new ActivityTestRule<>(MoviesListActivity.class, false, false);

    @Before
    public void setup(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = TmdbApi.Builder.getOkHttpClient(interceptor);
        IdlingResource idlingResource = OkHttp3IdlingResource.create("OkHttpClient", okHttpClient);

        Espresso.registerIdlingResources(idlingResource);
    }

    private static RecyclerViewMatcher withRecyclerView(){
        return new RecyclerViewMatcher(R.id.recyclerView);
    }

    @Test
    public void testSecondItemTitle(){

        Intent i = new Intent();
        i.putExtra(Movie.BUNDLE_LIST, Mock.getMockMovies());
        activityTestRule.launchActivity(i);

        onView(withRecyclerView().atPosition(1)).check(matches(hasDescendant(withText("Desejo de Matar"))));

    }

    @Test
    public void testThirdItemClick(){

        Intent i = new Intent();
        activityTestRule.launchActivity(i);

        onView(withRecyclerView().atPosition(2)).perform(click());

    }
}
