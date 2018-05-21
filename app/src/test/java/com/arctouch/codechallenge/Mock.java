package com.arctouch.codechallenge;

import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mock {

    public static List<Genre> getMockGenres(){

        return Arrays.asList(
                new Genre(1, "Action"),
                new Genre(2, "Adventure"),
                new Genre(3, "Animation"),
                new Genre(4, "Crime"),
                new Genre(5, "Drama"),
                new Genre(6, "Fantasy"),
                new Genre(7, "Historical"),
                new Genre(8, "Horror"),
                new Genre(9, "Mystery"),
                new Genre(10, "Romance"),
                new Genre(11, "Sci-fi")
        );

    }

    private static ArrayList<Genre> getGenresMovie1(){

        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Action"));
        genres.add(new Genre(2, "Adventure"));
        genres.add(new Genre(10, "Romance"));

        return genres;

    }

    private static ArrayList<Genre> getGenresMovie2(){

        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Action"));
        genres.add(new Genre(2, "Adventure"));
        genres.add(new Genre(4, "Crime"));

        return genres;

    }

    public static List<Movie> getMockMovies(){

        return Arrays.asList(
                new Movie(370567, "Gnomeu e Julieta: O Mistério do Jardim", getGenresMovie1(), false, "2018-05-31", 5.3),
                new Movie(370567, "Desejo de Matar", getGenresMovie2(), false, "2018-05-31", 5)
        );

    }

}
