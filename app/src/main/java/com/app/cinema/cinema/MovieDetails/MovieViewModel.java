package com.app.cinema.cinema.MovieDetails;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.app.cinema.cinema.Movie;
import com.app.cinema.cinema.database.MovieDatabase;

import java.util.List;

public class MovieViewModel extends ViewModel{
    public static MovieDatabase mDb;
    private MutableLiveData<List<Movie>> movies;
    public LiveData<List<Movie>> getAllMovies() {
        if (movies == null) {
            movies = new MutableLiveData<List<Movie>>();
            loadMovies();
        }
        return movies;
    }

    private void loadMovies() {
        //Do an asynchronous operation to fetch users.
        movies = (MutableLiveData<List<Movie>>) mDb.movieDao().loadFavoriteMovies();
    }
}
