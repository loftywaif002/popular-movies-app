package com.app.cinema.cinema.MovieDetails;

import android.os.AsyncTask;
import android.util.Log;

import com.app.cinema.cinema.BuildConfig;
import com.app.cinema.cinema.MovieComponents.Reviews;
import com.app.cinema.cinema.MovieComponents.Trailers;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReviewsTask extends AsyncTask<Long, Void, List<Reviews>> {

    @SuppressWarnings("unused")
    public static String LOG_TAG = ReviewsTask.class.getSimpleName();
    private final Listener mListener;

    /**
     * Interface definition for a callback to be invoked when reviews are loaded.
     */
    interface Listener {
        void on_reviews_loaded(List<Reviews> reviews);
    }
    public ReviewsTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected List<Reviews> doInBackground(Long... params) {
        List<Reviews> reviews = new ArrayList<>();
        // If there's no movie id, there's nothing to look up.
        if (params.length == 0) {
            return null;
        }
        long movieId = params[0];

        try {

            URL url;
            url = new URL("http://api.themoviedb.org/3/movie/"+movieId +"/reviews?api_key="+BuildConfig.API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type
            parseJson(results,reviews);
            inputStream.close();
            return reviews;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void parseJson(String data, List<Reviews> list){
        Reviews reviews = new Reviews();

        try {
            JSONObject mainObject = new JSONObject(data);
            Log.v(LOG_TAG,mainObject.toString());
            JSONArray resArray = mainObject.getJSONArray("results"); //Getting the results object
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                reviews.setmId(jsonObject.getString("id"));
                reviews.setmAuthor(jsonObject.getString("author"));
                reviews.setmUrl(jsonObject.getString("url"));
                reviews.setmContent(jsonObject.getString("content"));
                //Log.e(LOG_TAG, jsonObject.toString());
                list.add(reviews);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error occurred during JSON Parsing");
        }

    }

    @Override
    protected void onPostExecute(List<Reviews> reviews) {
        if (reviews != null) {
            mListener.on_reviews_loaded(reviews);
        } else {
            mListener.on_reviews_loaded(new ArrayList<Reviews>());
        }
    }
}