package com.awaeschoudhary.reruns;

/**
 * Created by awaeschoudhary on 10/9/17.
 */

public class Series {
    private int ID;
    private String imdbID;
    private String title;
    private int numberOfSeasons;

    public int getID() {
        return ID;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getTitle() {
        return title;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }
}
