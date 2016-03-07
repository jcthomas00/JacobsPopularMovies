package com.jacobcthomas.popularmovies;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailFragment extends Fragment {
    private final String BASE_URL = "http://image.tmdb.org/t/p/w";
    private final int POSTER_SIZE = 300;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public String formatMovieInfo(JSONObject jsonObject) throws JSONException {
        String movieInfo =
                "TITLE: " + jsonObject.getString("title") + "\n" +
                "RELEASE DATE: " + jsonObject.getString("release_date") + "\n" +
                "RATING: " + jsonObject.getString("vote_average") + "\n" +
                "PLOT: " + jsonObject.getString("overview") + "\n";
        return movieInfo;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail2, container, false);
        String arguements = getArguments().getString("movieDetails");
        FrameLayout movieDetailsLayout = (FrameLayout) view.findViewById(R.id.movie_details_fragment);

        try {
            JSONObject movieDetails = new JSONObject(arguements);

            ImageView imageView = new ImageView(getContext());
            StringBuilder url = new StringBuilder(BASE_URL + POSTER_SIZE);
            try {
                url.append(movieDetails.getString("poster_path"));
            } catch (JSONException e) {
                imageView.setImageResource(R.drawable.image_not_found);
            }
            ImageLoader imageLoader = MySingleton.getInstance(getContext()).getImageLoader();
            imageLoader.get(url.toString(), ImageLoader.getImageListener(imageView,
                    R.drawable.image_not_found, R.drawable.image_not_found));

            TextView movieText = new TextView(getContext());
            movieText.setText(formatMovieInfo(movieDetails));
            movieText.setPadding(50, 0, 50, 0);
            movieText.setTextColor(Color.GRAY);
            movieText.setBackgroundColor(Color.DKGRAY);
            movieText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            movieText.setGravity(Gravity.BOTTOM);
            imageView.setPadding(0, 100, 0, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            movieDetailsLayout.addView(imageView);
            movieDetailsLayout.addView(movieText);

        } catch (JSONException e) {
            e.printStackTrace();
        }        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
