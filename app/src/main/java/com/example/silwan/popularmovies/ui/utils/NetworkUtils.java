package com.example.silwan.popularmovies.ui.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Silwan on 13-3-2018.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Uri buildYoutubeThumbnailUrl(String value){
        Uri buildUri = Uri.parse(Constants.YOUTUBE_THUMBNAIL_BASE_PATH).buildUpon()
                .appendPath(value)
                .appendPath("mqdefault.jpg")
                .build();

        return buildUri;
    }

    public static Uri buildYoutubeUrl(String key){
        Uri youtubeUri = Uri.parse(Constants.YOUTUBE_URL).buildUpon()
                .appendQueryParameter(Constants.VIEW_PARAM, key)
                .build();

        return youtubeUri;
    }
}
