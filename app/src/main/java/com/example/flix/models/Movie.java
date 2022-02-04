package com.example.flix.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Encapsulates what makes up a movie in the app
public class Movie {

    String posterPath;
    String backdropPath;
    String title;
    String overview;

    // create the class constructor. It takes in a jsonObject and fills in the attributes of the class
    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
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
}
