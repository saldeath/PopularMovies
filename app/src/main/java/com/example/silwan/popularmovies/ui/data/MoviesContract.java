package com.example.silwan.popularmovies.ui.data;

import android.net.Uri;

public class MoviesContract {
    public static final String AUTHORITY = "com.example.silwan.popularmovies";
    public static final Uri BASE_CONTENT_URI = android.net.Uri.parse("content://"+AUTHORITY);
    public static final String PATH_MOVIES = "movies";


    public static final class MoviesEntry {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACK_DROP_PATH = "backdrop_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
    }
}
