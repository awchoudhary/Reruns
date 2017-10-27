package com.awaeschoudhary.reruns;

import java.io.Serializable;

/**
 * Created by awaeschoudhary on 10/9/17.
 */

public class Episode implements Serializable{
    private int ID;
    private String seriesImdbID;
    private int seasonNumber;
    private int numberInSeason;
    private String title;
    private String description;
    private int weightage;

    public int getID() {
        return ID;
    }

    public String getSeriesImdbID() {
        return seriesImdbID;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getNumberInSeason() {
        return numberInSeason;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getWeightage() {
        return weightage;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setSeriesImdbID(String seriesImdbID) {
        this.seriesImdbID = seriesImdbID;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public void setNumberInSeason(int numberInSeason) {
        this.numberInSeason = numberInSeason;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWeightage(int weightage) {
        this.weightage = weightage;
    }
}
