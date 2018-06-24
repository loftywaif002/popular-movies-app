package com.app.cinema.cinema;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.cinema.cinema.MovieDetails.MovieDetailActivity;
import com.app.cinema.cinema.MovieDetails.MovieDetailFragment;
import com.app.cinema.cinema.database.MovieDatabase;
import com.app.cinema.cinema.database.MovieEntry;
import com.app.cinema.cinema.utilities.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Dashboard extends AppCompatActivity implements MovieAdapter.OnItemClickListener{

    private static final String TAG = Dashboard.class.getSimpleName();

    private boolean tabletView;

    String myApiKey = BuildConfig.API_KEY;

    @BindView(R.id.recycled_movie_grid)
    RecyclerView movie_grid_recyclerView;

    @BindView(R.id.indeterminateBar)
    ProgressBar mProgressBar;

    String popularMoviesURL;
    String topRatedMoviesURL;

    ArrayList<Movie> mPopularList;
    ArrayList<Movie> mTopTopRatedList;

    private MovieAdapter mAdapter;

    private String mSortBy = FetchMovies.POPULAR;

    private MovieDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE); //Hide Progressbar by Default
        //Creating new Database for movies

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
        mAdapter = new MovieAdapter(new ArrayList<Movie>(), this);
        movie_grid_recyclerView.setAdapter(mAdapter);

        // Large-screen
        tabletView = findViewById(R.id.movie_detail_container) != null;

        mDb = MovieDatabase.getsInstance(getApplicationContext());
        //Create new MovieEntry object
        //createMovie();
    }

    public void createMovie(){
        Date date = new Date();
        MovieEntry movieEntry = new MovieEntry(1,1,"originalTitle","backdropPath","overview","releaseDate","posterPath",date);
        mDb.movieDao().insertMovie(movieEntry);

        //Reading Data
        List<Movie> movies = mDb.movieDao().loadFavoriteMovies();
        String test_name = movies.get(0).getOriginalTitle();
        Log.d(TAG,test_name);
        Toast.makeText(Dashboard.this,test_name,Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);

        switch (mSortBy) {
            case FetchMovies.POPULAR:
                menu.findItem(R.id.sort_by_popular).setChecked(true);
                break;
            case FetchMovies.TOP_RATED:
                menu.findItem(R.id.sort_by_top_rated).setChecked(true);
                break;
            case FetchMovies.FAVORITES:
                menu.findItem(R.id.sort_by_favorites).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_top_rated:
                mSortBy = FetchMovies.TOP_RATED;
                refreshList(mSortBy);
                item.setChecked(true);
                break;
            case R.id.sort_by_popular:
                mSortBy = FetchMovies.POPULAR;
                refreshList(mSortBy);
                item.setChecked(true);
                break;
            case R.id.sort_by_favorites:
                mSortBy = FetchMovies.FAVORITES;
                item.setChecked(true);
                refreshList(mSortBy);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshList(String sort_by) {
        switch (sort_by){
            case FetchMovies.POPULAR:
            mAdapter = new MovieAdapter(new ArrayList<Movie>(),this);
            mAdapter.add(mPopularList);
            movie_grid_recyclerView.setAdapter(mAdapter);
            break;
            case FetchMovies.TOP_RATED:
            mAdapter = new MovieAdapter(new ArrayList<Movie>(),this);
            mAdapter.add(mTopTopRatedList);
            movie_grid_recyclerView.setAdapter(mAdapter);
            break;
        }


    }

    public void send_details(Movie movie, int position) {
        if (tabletView) {

        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailFragment.ARG_MOVIE, movie);
            startActivity(intent);
        }
    }


    //AsyncTask
    public class FetchMovies extends AsyncTask<Void,Void,Void> {

        public final static String POPULAR = "popular";
        public final static String TOP_RATED = "top_rated";
        public final static String FAVORITES = "favorites";

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
            mAdapter = new MovieAdapter(new ArrayList<Movie>(),Dashboard.this);
            mAdapter.add(mPopularList);
            movie_grid_recyclerView.setAdapter(mAdapter);
        }
    }
}
