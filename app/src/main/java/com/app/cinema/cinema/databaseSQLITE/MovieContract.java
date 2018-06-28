package com.app.cinema.cinema.databaseSQLITE;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.app.cinema.cinema";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_TITLE = "original_title";
        public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] MOVIE_COLUMNS = {
                COLUMN_MOVIE_ID,
                COLUMN_MOVIE_VOTE_AVERAGE,
                COLUMN_MOVIE_TITLE,
                COLUMN_MOVIE_BACKDROP_PATH,
                COLUMN_MOVIE_OVERVIEW,
                COLUMN_MOVIE_RELEASE_DATE,
                COLUMN_MOVIE_POSTER_PATH
        };

        public static final int COL_MOVIE_ID = 0;
        public static final int COL_MOVIE_VOTE_AVERAGE = 1;
        public static final int COL_MOVIE_TITLE = 2;
        public static final int COL_MOVIE_BACKDROP_PATH = 3;
        public static final int COL_MOVIE_OVERVIEW = 4;
        public static final int COL_MOVIE_RELEASE_DATE = 5;
        public static final int COL_MOVIE_POSTER_PATH = 6;

    }
}