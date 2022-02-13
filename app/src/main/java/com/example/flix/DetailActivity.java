package com.example.flix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flix.databinding.ActivityDetailBinding;
import com.example.flix.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    public static final String YOUTUBE_API_KEY = "AIzaSyDVzhUCvDi8H9q2jGuPVPwWtiajRThAH3E"; // API for youtube services
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    public static final String TAG = "DetailActivity";

    //Store binding
    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());
        binding.ratingBar.setRating(movie.getRating());

        // Instantiate an AsyncHttpClient to execute the API request
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(String.format(VIDEOS_URL, movie.getMovieID()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "Video API Success"); // log for success, connected to TAG
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0){
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    Log.d(TAG, youtubeKey);
                    initializeYoutube(youtubeKey, movie.getRating());

                } catch (JSONException e) {
                    Log.d(TAG, "Failed to parse JSON", e); // log for error parsing, connected to TAG
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Video API Failure: " + response); // log for success, connected to TAG

            }
        });
    }

    private void initializeYoutube(final String youtubeKey, float movieRating) {
        binding.player.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onInitializationSuccess");
                if(movieRating < 5){
                    youTubePlayer.cueVideo(youtubeKey); // Auto play popular videos
                }
                else{
                    youTubePlayer.loadVideo(youtubeKey); // User clicks to play less popular video
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onInitializationFailure: " + youTubeInitializationResult);
            }
        });
    }
}