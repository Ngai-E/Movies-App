package com.example.moviesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.moviesapp.databinding.ActivityMainBinding;
import com.example.moviesapp.model.Movie;
import com.example.moviesapp.utitilities.MovieNetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.HandleClickListener {
    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private  MoviesAdapter moviesAdapter;
    private char filter = 'P'; //P or R
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding  = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_Movies);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter(this);
        recyclerView.setAdapter(moviesAdapter);

        loadData();
    }

    @Override
    public void onClick(Movie movie) {
//        Toast.makeText(this, "movie " + movie.getmMovietitle() + " was clicked", Toast.LENGTH_SHORT).show();
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
                this.filter = 'P';
                break;
            case R.id.top_rated:
                this.filter = 'R';
                break;
        }
        this.loadData();
        return super.onOptionsItemSelected(item);
    }

    private  void loadData() {
        showMovies();
        String sort_by  =  String.valueOf(this.filter);
        new FetchMoviesData().execute(sort_by);
    }

    private void showMovies(){
        binding.tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        binding.recyclerviewMovies.setVisibility(View.VISIBLE);
    }
    private void showError(){
        binding.tvErrorMessageDisplay.setVisibility(View.VISIBLE);
        binding.recyclerviewMovies.setVisibility(View.INVISIBLE);
    }

    public  class FetchMoviesData extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected  void onPreExecute() {
            super.onPreExecute();
            binding.pbLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(String... params) {

            /* If there's no sort_by code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String sort_by = params[0];
            URL movieRequestURL = MovieNetworkUtils.buildUrl(sort_by);

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

        protected void onPostExecute(JSONObject movieJson) {
            binding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieJson != null) {
                showMovies();
                try {
                    System.out.println(movieJson.getJSONArray("results"));
                    List<Movie> movies = Movie.createMovies(movieJson.getJSONArray("results"));
                    moviesAdapter.setMoviesData(movies);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(this.getClass().getSimpleName(), "onPostExecute: " + e.getMessage());
                }

            } else {
                showError();
            }
        }
    }
}
