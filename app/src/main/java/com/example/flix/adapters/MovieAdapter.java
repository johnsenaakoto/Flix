package com.example.flix.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flix.DetailActivity;
import com.example.flix.MainActivity;
import com.example.flix.R;
import com.example.flix.databinding.ActivityMainBinding;
import com.example.flix.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

// Movie adaptor holds the movie in the apart. It connects the data to the xml file
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context; // we need a context
    List<Movie> movies; // we need our movie list

    // we generate constructors for context and movies
    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // onCreateViewHolder inflates the item_movie.xml and return it to the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater takes a context and a layout.xml (item_movie in this case) and inflates it into a view (movieView)
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        // return the movieView through a ViewHolder
        return new ViewHolder(movieView);
    }

    // onBindViewHolder takes the position of the item (data) and populates it through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get movie object from movies list at the input position
        Movie movie = movies.get(position);

        // holder binds attributes from the movie.
        holder.bind(movie);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create ViewHolder, which essentially holds a row from our layout.xml.
    // a row has an ImageView for the poster (ivPoster), a TextView for the title (tvTitle),
    // and a TextView for the overview (tvOverview)

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Define views
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        ImageView previewButton;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find view variables by the ID from the layout.xml
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            previewButton = itemView.findViewById(R.id.previewButton);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl; // create variable so that we can change between landscape and portrait mode

            // checks context to see if phone is in landscape or portrait
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
            } else {
                imageUrl = movie.getPosterPath();
            }

            // add rounded corners to images
            int radius = 30; // corner radius, higher value = more rounded
            int margin = 10; // crop margin, set to 0 for corners with no crop
            Glide.with(context).load(imageUrl).fitCenter().transform(new RoundedCornersTransformation(radius, margin)).into(ivPoster); // Binding an image is more complex, we use Glide

            // delete previewButton for less popular movies
            if(movie.getRating() > 4.99){
                previewButton.setVisibility(View.VISIBLE);
            }


            // Create an onClickListener on the tvTitle so that stuff can be done when it's clicked
                // 1. Register click listener on the whole row
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 2. Navigate to a new activity on tap
                    // Creating an intent to navigate to activity_detail.xml
                    Intent i = new Intent(context, DetailActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, ivPoster, "activityTransition");
                    i.putExtra("movie", Parcels.wrap(movie)); // send entire movie class using Parcels to DetailActivity
                    context.startActivity(i, options.toBundle());
                }
            });
        }
    }
}
