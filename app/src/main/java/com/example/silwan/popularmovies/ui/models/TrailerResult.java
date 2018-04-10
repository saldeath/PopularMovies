package com.example.silwan.popularmovies.ui.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Silwan on 27/03/2018.
 */

public class TrailerResult {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<TrailerModel> results = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TrailerModel> getResults() {
        return results;
    }

    public void setResults(List<TrailerModel> results) {
        this.results = results;
    }
}
