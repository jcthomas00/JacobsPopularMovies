package com.jacobcthomas.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class MoviePosterAdapter extends BaseAdapter {
    private Context mContext;
    private JSONArray movieArray;
    private final String BASE_URL = "http://image.tmdb.org/t/p/w";
    private final int POSTER_SIZE = 300;
    private final int HEIGHT = (int) (POSTER_SIZE*1.5);

    public MoviePosterAdapter(Context context, JSONArray movieArray) {
        mContext = context;
        this.movieArray = movieArray;
    }

    @Override
    public int getCount() {
        if (movieArray != null)
            return movieArray.length();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView==null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(POSTER_SIZE, HEIGHT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else
        imageView = (ImageView) convertView;

        if (movieArray == null)
            imageView.setImageResource(R.drawable.image_not_found);
        else {
            StringBuilder url = new StringBuilder(BASE_URL + POSTER_SIZE);
            try {
                url.append(movieArray.getJSONObject(position).getString("poster_path"));
            } catch (JSONException e) {
                imageView.setImageResource(R.drawable.image_not_found);
            }

            ImageLoader imageLoader = MySingleton.getInstance(mContext).getImageLoader();
            imageLoader.get(url.toString(), ImageLoader.getImageListener(imageView,
                    R.drawable.image_not_found, R.drawable.image_not_found));
        }
        return imageView;
    }

}
