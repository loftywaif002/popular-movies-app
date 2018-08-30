package com.app.cinema.cinema;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
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

import com.app.cinema.cinema.MovieDetails.MovieDetailActivity;
import com.app.cinema.cinema.MovieDetails.MovieDetailFragment;

import com.app.cinema.cinema.databaseSQLITE.MovieContract;
import com.app.cinema.cinema.utilities.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Dashboard extends AppCompatActivity implements MovieAdapter.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = Dashboard.class.getSimpleName();

    private boolean tabletView;

    String myApiKey = BuildConfig.API_KEY;
    private static final int FAVORITE_MOVIES_LOADER = 0;
    @BindView(R.id.recycled_movie_grid)
    RecyclerView movie_grid_recyclerView;

    @BindView(R.id.indeterminateBar)
    ProgressBar mProgressBar;

    String popularMoviesURL;
    String topRatedMoviesURL;

    ArrayList<Movie> mPopularList;
    ArrayList<Movie> mTopTopRatedList;


    private MovieAdapter mAdapter;
    private MovieAdapter mAdapterFavorite;
    private String mSortBy = FetchMovies.POPULAR;

    private static final String EXTRA_MOVIES = "EXTRA_MOVIES";
    private static final String EXTRA_SORT_BY = "EXTRA_SORT_BY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE); //Hide Progressbar by Default
        //Dealing with View Model

        //Define recyclerView Layout
        movie_grid_recyclerView.setLayoutManager(new GridLayoutManager(this, getResources()
                .getInteger(R.integer.number_of_grid_columns)));
        mAdapter = new MovieAdapter(new ArrayList<Movie>(), this);
        movie_grid_recyclerView.setAdapter(mAdapter);
        // Large-screen
        tabletView = findViewById(R.id.movie_detail_container) != null;

        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getString(EXTRA_SORT_BY);
            if (savedInstanceState.containsKey(EXTRA_MOVIES)) {
                List<Movie> movies = savedInstanceState.getParcelableArrayList(EXTRA_MOVIES);
                mAdapter.add(movies);
                findViewById(R.id.indeterminateBar).setVisibility(View.GONE);

                // For listening content updates for tow pane mode
                if (mSortBy.equals(FetchMovies.FAVORITES)) {
                    getSupportLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null,this);
                }
            }
            update_empty_state();
        } else {
            // Fetch Movies only if savedInstanceState == null
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
    }

    @Override
    public void onBackPressed() {
        if (mSortBy.equals(FetchMovies.FAVORITES)) {
            getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
        }
        mSortBy = FetchMovies.POPULAR;
        refreshList(mSortBy);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> movies = mAdapter.getMovies();
        if (movies != null && !movies.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_MOVIES, movies);
        }
        outState.putString(EXTRA_SORT_BY, mSortBy);

        if (!mSortBy.equals(FetchMovies.FAVORITES)) {
            getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
        }
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
                if (mSortBy.equals(FetchMovies.FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
                }
                mSortBy = FetchMovies.TOP_RATED;
                refreshList(mSortBy);
                item.setChecked(true);
                break;
            case R.id.sort_by_popular:
                if (mSortBy.equals(FetchMovies.FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
                }
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
            case FetchMovies.FAVORITES:
            getSupportLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this);
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

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        findViewById(R.id.indeterminateBar).setVisibility(View.VISIBLE);
        return new CursorLoader(this,
               MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        mAdapter.add(cursor);
        update_empty_state();
        findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {

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

    private void update_empty_state() {
        if (mAdapter.getItemCount() == 0) {
            if (mSortBy.equals(FetchMovies.FAVORITES)) {
                findViewById(R.id.empty_state).setVisibility(View.GONE);
                findViewById(R.id.empty_state_favorites_container).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.empty_state).setVisibility(View.VISIBLE);
                findViewById(R.id.empty_state_favorites_container).setVisibility(View.GONE);
            }
        } else {
            findViewById(R.id.empty_state).setVisibility(View.GONE);
            findViewById(R.id.empty_state_favorites_container).setVisibility(View.GONE);
        }
    }
}
