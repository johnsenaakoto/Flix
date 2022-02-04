package com.example.flix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flix.adapters.MovieAdapter;
import com.example.flix.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    // Create string to hold http for API request
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    // Creating a tag for logging result of client get request
    public static final String TAG = "MainActivity";
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define recycler view
        RecyclerView rvMovies = findViewById(R.id.rvMovies);

        // Define base movies for recycler
        movies = new ArrayList<>();

        // Create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        // Set a Layout Manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));


        // Instantiate an AsyncHttpClient to execute the API request
        AsyncHttpClient client = new AsyncHttpClient();

        // Make a get request on the client object. A get request takes in the API request http, params, and a response handler
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "Success"); // log for success, connected to TAG
                JSONObject jsonObject = json.jsonObject; // we store the response jsonObject in the variable jsonObject
                try {
                    JSONArray results = jsonObject.getJSONArray("results"); // we store the results array into new variable results. This results array is extracted from the jsonObject by using the getJSONArray method
                    Log.i(TAG, "Results: " + results.toString()); //logs onSuccess and shows what is in results
                    movies.addAll(Movie.fromJsonArray(results)); // runs Movie.fromJsonArray on results and returns a list of Movie objects in variable movies
                    movieAdapter.notifyDataSetChanged(); // notify movieAdapter of the new movie list that have been received
                    Log.i(TAG, "Movies size is: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e); // handles the exception if results is not in the jsonObject
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");

            }
        });
    }
}