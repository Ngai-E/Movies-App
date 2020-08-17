package com.example.moviesapp;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviesapp.databinding.ActivityMoviesDetailsBinding;
import com.example.moviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

public class MoviesDetails extends AppCompatActivity {
    Movie movie;
    private ActivityMoviesDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoviesDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (getIntent().hasExtra("movie")){
            this.movie = (Movie) getIntent().getSerializableExtra("movie");
            if (this.movie != null) {
//                Toast.makeText(this, this.movie.getmMovietitle() + " this is id", Toast.LENGTH_SHORT).show();
                try {
                    populateView(movie);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void populateView(Movie movie ) throws ParseException {
        binding.description.setText(movie.getmOverview().trim());
        binding.title.setText(movie.getmMovietitle());
        binding.vote.setText(movie.getmVoteAverage() + "/10");
        binding.release.setText(movie.getmReleaseDate());
        Picasso.with(this)
                .load(Movie.getImageFullPath(movie.getmMovieImageURL()))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.poster);

    }

}
