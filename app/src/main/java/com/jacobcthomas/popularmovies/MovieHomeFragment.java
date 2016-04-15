package com.jacobcthomas.popularmovies;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.zip.Inflater;

//GridView Version
public class MovieHomeFragment extends Fragment {

    private final String POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=d284ef3240230de19eef97db7d0fbb27";
    private final String RATED_MOVIES_URL = "https://api.themoviedb.org/3/discover/movie?sort_by=rating.desc&api_key=d284ef3240230de19eef97db7d0fbb27";
    String movieUrl = null;


    public MovieHomeFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_home, container, false);
        getMovieData((GridView) view.findViewById(R.id.movie_grid));


        return view;
    }

public void getMovieData(final GridView movieGrid){
    //get JSON object populated with data from MovieDB
    if (movieUrl==null)
        movieUrl = POPULAR_MOVIES_URL;

    JsonObjectRequest movieList = new JsonObjectRequest(movieUrl,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(final JSONObject popularMovies) {
                    //display movie grid
                    try {
                        movieGrid.setAdapter(new MoviePosterAdapter(getContext(), popularMovies.getJSONArray("results")));
                        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                MovieDetailFragment detailsFragment = new MovieDetailFragment();
                                Bundle args = new Bundle();
                                try {
                                    args.putString("movieDetails",
                                            popularMovies.getJSONArray("results").get(position).toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                detailsFragment.setArguments(args);

                                android.support.v4.app.FragmentTransaction transaction =
                                        getFragmentManager().beginTransaction();

                                transaction.replace(R.id.fragment_container, detailsFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
    MySingleton.getInstance(getContext()).addToRequestQueue(movieList);

    //define action when a movie poster is clicked

}
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_popular) {
            movieUrl = POPULAR_MOVIES_URL;
            getMovieData((GridView) getActivity().findViewById(R.id.movie_grid));
            return true;
        } else if (id == R.id.sort_rating) {
            movieUrl = RATED_MOVIES_URL;
            getMovieData((GridView) getActivity().findViewById(R.id.movie_grid));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
