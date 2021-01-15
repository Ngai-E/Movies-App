package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.moviesapp.adapters.MoviesAdapter;
import com.example.moviesapp.databinding.ActivityMainBinding;
import com.example.moviesapp.model.Movie;
import com.example.moviesapp.utitilities.MovieNetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.HandleClickListener, LoaderManager.LoaderCallbacks<JSONObject> {
    private ActivityMainBinding binding;
    private  MoviesAdapter moviesAdapter;
    private  static  final char TOP_RATED = 'R';
    private  static  final char MOST_POPULAR = 'p';
    private char filter = MOST_POPULAR; //P or R
    private static final String MOVIE_SORT_ORDER = "sort_by";
    private  static final int MOVIE_LOADER = 22;
    public  static  final String  RESULTS = "results";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding  = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerviewMovies.setLayoutManager(gridLayoutManager);
        binding.recyclerviewMovies.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter(this);
        binding.recyclerviewMovies.setAdapter(moviesAdapter);
        getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);
        loadData();
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MoviesDetails.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();
        switch (menuItemSelected) {
            case R.id.most_popular:
                this.filter = MOST_POPULAR;
                break;
            case R.id.top_rated:
                this.filter = TOP_RATED;
                break;
        }
        this.loadData();
        return super.onOptionsItemSelected(item);
    }

    private  void loadData() {
        showMovies();
        String sort_by_value  =  String.valueOf(this.filter);

        Bundle bundle = new Bundle();
        bundle.putString(MOVIE_SORT_ORDER, sort_by_value);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader loader = loaderManager.getLoader(MOVIE_LOADER);

        if (loader == null)
            loaderManager.initLoader(MOVIE_LOADER, bundle, this);
        else
            loaderManager.restartLoader(MOVIE_LOADER, bundle, this);
    }

    private void showMovies(){
        binding.tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        binding.recyclerviewMovies.setVisibility(View.VISIBLE);
    }
    private void showError(){
        binding.tvErrorMessageDisplay.setVisibility(View.VISIBLE);
        binding.recyclerviewMovies.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<JSONObject>(this) {
            JSONObject results = new JSONObject();
            @Override
            public void deliverResult(@Nullable JSONObject data) {
                try {
                    results.put(String.valueOf(filter), data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showError();
                }
                super.deliverResult(data);
            }

            @Nullable
            @Override
            public JSONObject loadInBackground() {
                /* If there's no sort_by code, there's nothing to look up. */
                if (bundle.getString(MOVIE_SORT_ORDER) == null || TextUtils.isEmpty(bundle.getString(MOVIE_SORT_ORDER))) {
                    return null;
                }
                String movieSortOrder = bundle.getString(MOVIE_SORT_ORDER);
                URL movieRequestURL = MovieNetworkUtils.buildUrl(movieSortOrder);

                try {
                    String jsonMovieResponse = MovieNetworkUtils
                            .getResponseFromHttpUrl(movieRequestURL);
                    JSONObject movieJson = new JSONObject(jsonMovieResponse);
                    return movieJson;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
                if (bundle == null)
                    return;
                try {
                    if (results.has(String.valueOf(filter)) &&  results.getJSONObject(String.valueOf(filter)) != null){
                        deliverResult(results.getJSONObject(String.valueOf(filter)));
                    } else {
                        binding.pbLoadingIndicator.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showError();
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject movieJson) {
        binding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movieJson != null) {
            showMovies();
            try {
                System.out.println(movieJson.getJSONArray(RESULTS));
                List<Movie> movies = Movie.createMovies(movieJson.getJSONArray(RESULTS));
                moviesAdapter.setMoviesData(movies);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(this.getClass().getSimpleName(), "onPostExecute: " + e.getMessage());
            }

        } else {
            showError();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }

}
