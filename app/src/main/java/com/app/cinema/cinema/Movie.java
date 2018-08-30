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
    private Long id;
    @SerializedName("vote_average")
    private String voteAverage;
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

    public Movie(long id,
                 String voteAverage,
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
        id = in.readLong();
        voteAverage = in.readString();
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
        parcel.writeLong(id);
        parcel.writeString(voteAverage);
        parcel.writeString(originalTitle);
        parcel.writeString(backdropPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
    }

    //Getter Methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getBackdropPath() {
        if (backdropPath != null && !backdropPath.isEmpty()) {
            if(!backdropPath.toLowerCase().contains("http://")){
                return "http://image.tmdb.org/t/p/original" + backdropPath;
            }else{
                return backdropPath;
            }

        }
        return null; //Use Picasso to put placeholder for poster
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Nullable
    public String getPosterPath() {
        if (posterPath != null && !posterPath.isEmpty()) {

            if(!posterPath.toLowerCase().contains("http://")){
                return "http://image.tmdb.org/t/p/w342" + posterPath;
            }else{
                return posterPath;
            }

        }
        return null; //Use Picasso to put placeholder for poster
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

}
