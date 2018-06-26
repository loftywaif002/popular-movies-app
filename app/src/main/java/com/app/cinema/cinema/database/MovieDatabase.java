package com.app.cinema.cinema.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

@Database(entities = {MovieEntry.class}, version = 5, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class MovieDatabase extends RoomDatabase{

    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABSE_NAME="movielist";
    private static MovieDatabase sInstance;

    public static MovieDatabase getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG,"Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class,MovieDatabase.DATABSE_NAME)
                        .addMigrations(MIGRATION_4_5)
                        .build();
            }
        }
        Log.d(LOG_TAG,"Getting the Database Instance");
        return sInstance;
    }

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
           // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    public abstract MovieDao movieDao();
}
