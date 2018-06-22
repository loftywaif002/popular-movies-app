package com.app.cinema.cinema;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    public static final String LOG_TAG = Movie.class.getSimpleName();


    @SerializedName("id")
    private int id;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("poster_path")
    private String posterPath;

    public Movie(){

    }

    public Movie(int id,
                 double voteAverage,
                 String originalTitle,
                 String backdropPath,
                 String overview,
                 String releaseDate,
                 String posterPath)
    {
        this.id = id;
        this.voteAverage = voteAverage;
        this.originalTitle = originalTitle;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    protected Movie(Parcel in){
        id = in.readInt();
        voteAverage = in.readDouble();
        originalTitle = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
          return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeDouble(voteAverage);
        parcel.writeString(originalTitle);
        parcel.writeString(backdropPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
    }

    //Getter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public Double getVoteAvg() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAvg){
        this.voteAverage = voteAvg;
    }

    @Nullable
    public String getTitle() {
        return originalTitle;
    }


    public void setOriginalTitle(String title){
        this.originalTitle = title;
    }

    public String getBackdrop() {
        return backdropPath;
    }

    public void setBackdropPath(String backdrpPath){
        this.backdropPath = backdrpPath;
    }

    @Nullable
    public String getOverview() {
        return overview;
    }

    public void setOverview(String ovrview){
        this.overview = ovrview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate){

        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return getPosterPath();
    }

    @Nullable
    public String getPosterPath(Context context) {
        if (posterPath != null && !posterPath.isEmpty()) {
            return "http://image.tmdb.org/t/p/w342" + posterPath;
        }
        return null; //Use Picasso to put placeholder for poster
    }


    public void setPosterPath(String posterPath){
        this.posterPath = posterPath;
    }


}
