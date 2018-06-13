package com.app.cinema.cinema;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

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

    @Nullable
    public Double getVoteAvg() {
        return voteAverage;
    }

    @Nullable
    public String getTitle() {
        return originalTitle;
    }

    public String getBackdrop() {
        return backdropPath;
    }

    @Nullable
    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return getPosterPath();
    }


}
