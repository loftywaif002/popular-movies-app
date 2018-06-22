package com.app.cinema.cinema;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.cinema.cinema.utilities.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Dashboard extends AppCompatActivity {

    private static final String TAG = Dashboard.class.getSimpleName();

    String myApiKey = "525e39d1c3568cd23cdaf0a3674918fa";

    @BindView(R.id.recycled_movie_grid)
    RecyclerView movie_grid_recyclerView;

    @BindView(R.id.indeterminateBar)
    ProgressBar mProgressBar;

    String popularMoviesURL;
    String topRatedMoviesURL;

    ArrayList<Movie> mPopularList;
    ArrayList<Movie> mTopTopRatedList;

    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE); //Hide Progressbar by Default

        if(NetworkUtils.networkStatus(Dashboard.this)){
            new FetchMovies().execute();
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard.this);
            dialog.setTitle(getString(R.string.title_network_alert));
            dialog.setMessage(getString(R.string.message_network_alert));
            dialog.setCancelable(false);
            dialog.show();
        }
        //Define recyclerView Layout
        movie_grid_recyclerView.setLayoutManager(new GridLayoutManager(this, getResources()
                .getInteger(R.integer.number_of_grid_columns)));
        mAdapter = new MovieAdapter(new ArrayList<Movie>());
        movie_grid_recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(NetworkUtils.networkStatus(Dashboard.this)){
            new FetchMovies().execute();
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard.this);
            dialog.setTitle(getString(R.string.title_network_alert));
            dialog.setMessage(getString(R.string.message_network_alert));
            dialog.setCancelable(false);
            dialog.show();
        }

    }

    //AsyncTask
    public class FetchMovies extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {


            popularMoviesURL = "https://api.themoviedb.org/3/movie/popular?api_key="+myApiKey+"&language=en-US";
            topRatedMoviesURL = "https://api.themoviedb.org/3/movie/top_rated?api_key="+myApiKey+"&language=en-US";

            mPopularList = new ArrayList<>();
            mTopTopRatedList = new ArrayList<>();

            try {
                if(NetworkUtils.networkStatus(Dashboard.this)){
                    mPopularList = NetworkUtils.fetchData(popularMoviesURL); //Get popular movies
                    mTopTopRatedList = NetworkUtils.fetchData(topRatedMoviesURL); //Get top rated movies
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard.this);
                    dialog.setTitle(getString(R.string.title_network_alert));
                    dialog.setMessage(getString(R.string.message_network_alert));
                    dialog.setCancelable(false);
                    dialog.show();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            mProgressBar.setVisibility(View.INVISIBLE);
            //Load popular movies by default
            mAdapter = new MovieAdapter(new ArrayList<Movie>());
            mAdapter.add(mPopularList);
            movie_grid_recyclerView.setAdapter(mAdapter);
        }
    }
}
