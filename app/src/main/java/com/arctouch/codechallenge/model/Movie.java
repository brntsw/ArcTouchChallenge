package com.arctouch.codechallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie implements Parcelable {
    public static final String BUNDLE_ID = "movie_id";
    public static final String BUNDLE = "movie";
    public static final String BUNDLE_LIST = "movies_list";

    public int id;
    public String title;
    public String overview;
    public ArrayList<Genre> genres;
    @JsonProperty("genre_ids")
    public List<Integer> genreIds;
    @JsonProperty("poster_path")
    public String posterPath;
    @JsonProperty("backdrop_path")
    public String backdropPath;
    @JsonProperty("adult")
    public boolean adult;
    @JsonProperty("release_date")
    public String releaseDate;
    @JsonProperty("vote_average")
    public double voteAverage;

    public Movie(){}

    public Movie(int id, String title, ArrayList<Genre> genres, boolean adult, String releaseDate, double voteAverage){
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.adult = adult;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        genres = in.createTypedArrayList(Genre.CREATOR);
        posterPath = in.readString();
        backdropPath = in.readString();
        adult = in.readByte() != 0;
        releaseDate = in.readString();
        voteAverage = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeTypedList(genres);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
    }

    @Override
    public int describeContents() {
        return 0;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;

        if (id != movie.id) return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        if (overview != null ? !overview.equals(movie.overview) : movie.overview != null)
            return false;
        if (genres != null ? !genres.equals(movie.genres) : movie.genres != null) return false;
        if (genreIds != null ? !genreIds.equals(movie.genreIds) : movie.genreIds != null)
            return false;
        if (posterPath != null ? !posterPath.equals(movie.posterPath) : movie.posterPath != null)
            return false;
        if (backdropPath != null ? !backdropPath.equals(movie.backdropPath) : movie.backdropPath != null)
            return false;
        return releaseDate != null ? releaseDate.equals(movie.releaseDate) : movie.releaseDate == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (overview != null ? overview.hashCode() : 0);
        result = 31 * result + (genres != null ? genres.hashCode() : 0);
        result = 31 * result + (genreIds != null ? genreIds.hashCode() : 0);
        result = 31 * result + (posterPath != null ? posterPath.hashCode() : 0);
        result = 31 * result + (backdropPath != null ? backdropPath.hashCode() : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", genres=" + genres +
                ", genreIds=" + genreIds +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
