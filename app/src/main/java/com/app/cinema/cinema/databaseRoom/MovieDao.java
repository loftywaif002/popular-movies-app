package com.app.cinema.cinema.databaseRoom;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.Update;

import com.app.cinema.cinema.Movie;

import java.util.List;
//Not Using ROOM
@Dao
public interface MovieDao {
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> loadFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieEntry movieEntry);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieEntry movieEntry);

    @Delete
    void deleteMovie(MovieEntry movieEntry);

    @Query("SELECT * FROM movie WHERE id = :id")
    MovieEntry loadMovieById(long id);

    @Query("DELETE FROM movie WHERE id = :id")
    void deleteMovieById(long id);

    @Query("DELETE FROM movie")
    void delete();
}
