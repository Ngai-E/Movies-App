package com.example.moviesapp.model;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie implements Serializable {
    public static final String  BASE_URL = "https://api.themoviedb.org/3";
    public static final String  TOKEN = "e6141af8eb1ab2f2e74e482264fce476";
    public static final String  SORT_PARAM_NAME = "sort_by";
    public static final String  TOKEN_PARAM_NAME = "api_key";
    public static final String  ENDPOINT_BY_POPULAR = "movie/popular";
    public static final String  ENDPOINT_BY_MOST_RATED = "movie/top_rated";
    public static final String  IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    public static final String  IMAGE_SIZE = "/w500";
    public static final String  PAGE_PARAM_NAME = "page";

    //example request = 'https://api.themoviedb.org/3/movie/top_rated?api_key=e6141af8eb1ab2f2e74e482264fce476&page=1'

    private String  mMovietitle;
    private String  mMovieDescription;
    private String  mMovieRating;
    private Long  mMovieID;
    private  String mOverview;

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public float getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(float mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    private  float mVoteAverage;

    public String getmReleaseDate() throws ParseException {
        String strDate;
        try {
            strDate =  new SimpleDateFormat("yyyy-MM-dd").parse(this.mReleaseDate).toString();
        } catch (ParseException e){
            strDate = "n/a";
        }
        String [] arrParts = strDate.split(" ");
        arrParts[3] = arrParts[4] = "";
        arrParts[0] += ",";
        return TextUtils.join(" ", arrParts);
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    private  String mReleaseDate;
    private String  mMovieImageURL;

    public Movie(String mMovietitle, Long mMovieID, String mMovieImageURL, String mReleaseDate) {
        this.mMovietitle = mMovietitle;
        this.mMovieID = mMovieID;
        this.mMovieImageURL = mMovieImageURL;
        this.mReleaseDate = mReleaseDate;
    }

    public String getmMovietitle() {
        return mMovietitle;
    }

    public void setmMovietitle(String mMovietitle) {
        this.mMovietitle = mMovietitle;
    }

    public String getmMovieDescription() {
        return mMovieDescription;
    }

    public void setmMovieDescription(String mMovieDescription) {
        this.mMovieDescription = mMovieDescription;
    }

    public String getmMovieRating() {
        return mMovieRating;
    }

    public void setmMovieRating(String mMovieRating) {
        this.mMovieRating = mMovieRating;
    }

    public Long getmMovieID() {
        return mMovieID;
    }

    public void setmMovieID(Long mMovieID) {
        this.mMovieID = mMovieID;
    }

    public String getmMovieImageURL() {
        return mMovieImageURL;
    }

    public void setmMovieImageURL(String mMovieImageURL) {
        this.mMovieImageURL = mMovieImageURL;
    }

    public static List<Movie> createMovies(JSONArray jsonMovies) {
        List<Movie> list = new ArrayList<>();
        Movie movie;
        JSONObject jsonMovieObject;
        try {
            String title;
            Long ID;
            String imageUrl;
            String releaseDate;
            for (int i = 0; i < jsonMovies.length(); i++) {
                jsonMovieObject = jsonMovies.getJSONObject(i);
                title = jsonMovieObject.getString("title");
                ID = jsonMovieObject.getLong("id");
                imageUrl = jsonMovieObject.getString("poster_path");
                releaseDate = jsonMovieObject.getString("release_date");
                movie = new Movie(title, ID, imageUrl, releaseDate);
                movie.setmVoteAverage((float) jsonMovieObject.getDouble("vote_average"));
                movie.setmOverview(jsonMovieObject.getString("overview"));
                list.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(Movie.class.getSimpleName(), "createMovies: JSON exception while creating movie" + e);
        }
        return  list;
    }
    public  static String getImageFullPath(String imagePath) {
        return IMAGE_BASE_URL + IMAGE_SIZE + imagePath;
    }
}
