package com.example.flix.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

// Encapsulates what makes up a movie in the app
@Parcel // To make it parceable
public class Movie {

    int movieID;
    String posterPath;
    String backdropPath;
    String title;
    String overview;
    String rating;

    // empty constructor needed by Parcelor Library
    public Movie(){}

    // create the class constructor. It takes in a jsonObject and fills in the attributes of the class
    public Movie(JSONObject jsonObject) throws JSONException {
        movieID = jsonObject.getInt("id");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        rating = jsonObject.getString("vote_average");
    }

    // create a method (fromJsonArray) that iterates through the input JSONArray movieJsonArray
    // extracts the relevant information to form a movie object. All movie objects are put in a list.
    // the output list is returned
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>(); // Create output list

        // for block to iterate over movieJsonArray to extract movies
        for (int i = 0; i < movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i))); // getJSONObject(i) gets movie by movie from the moviesJsonArray
        }

        // return movies list.
        // Contains all the movie objects that were constructed by extracting objects from movieJsonArray (input)
        return movies;
    }

    // getters to extract posterPath, title, and overview from Movie objects

    public int getMovieID() { return movieID; }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public float getRating() { return Float.parseFloat(rating); }
}
