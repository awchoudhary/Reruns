package com.awaeschoudhary.reruns;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by awaeschoudhary on 10/9/17.
 * Scrape IMDB for episodes and series information and save it in the database.
 */

public class ExtractAndSaveSeriesTask extends AsyncTask<String, Void, Void> {

    private Context context;
    private ProgressDialog progressDialog;


    public ExtractAndSaveSeriesTask(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        //show progress dialog
        progressDialog = ProgressDialog.show(context, "", "Loading", true);
    }

    protected Void doInBackground(String... params) {
        String id = params[0];

        // TODO: 10/12/17 Check if series is already loaded here

        try {
            extractAndSaveSeries(id);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            progressDialog.dismiss();
        }

        return null;
    }

    protected void onPostExecute(Void v) {
        //hide dialog
        progressDialog.dismiss();

        Intent intent = new Intent(context, context.getClass());
        context.startActivity(intent);
    }

    private void extractAndSaveSeries(String imdbID) throws Exception {
        Series series = new Series();
        series.setImdbID(imdbID);

        //season to be extracted
        int seasonCount = 100;

        //number of season that have at least one released episode
        int validSeasonCount = 0;

        while(seasonCount > 0){
            Document document = Jsoup.connect("http://www.imdb.com/title/" + imdbID + "/episodes?season=" + seasonCount).get();

            //if we provide a value greater than the latest season of a series, IMDB will load the page
            //for the latest season, which is a good way to get the total seasons.
            if(seasonCount == 100){
                String selectedSeason = document.getElementById("bySeason")
                        .getElementsByAttributeValue("selected", "selected").get(0).attr("value");

                seasonCount = Integer.parseInt(selectedSeason);
            }

            if(series.getTitle() == null || series.getTitle().equals("")){
                //first h3 tag in page should contain an href which contains the series title
                String seriesTitle = document.getElementsByTag("h3").get(0).getElementsByTag("a").get(0).text();
                series.setTitle(seriesTitle);
            }

            if(extractAndSaveEpisodesForSeason(document, imdbID, seasonCount)){
                validSeasonCount++;
            }

            seasonCount--;
        }

        series.setNumberOfSeasons(validSeasonCount);

        //save series to db
        DbHandler.getInstance(context).createSeries(series);
    }

    //extract and save released episodes for a season and return true if the season has at least
    //one released episode
    private boolean extractAndSaveEpisodesForSeason(Document document, String seriesImdbID, int seasonNumber){
        boolean isSeasonValid = false;

        //Used to determine if episode is not out yet, since unreleased episodes have this description
        final String unreleasedEpisodeDescription = "Know what this is about? Be the first one to add a plot.";

        //episode info is contained within divs with the class "list_item"
        Elements episodeDivs = document.getElementsByClass("list_item");

        for(Element div : episodeDivs){
            //Stop looking for more episodes there is one that is not out yet
            String episodeDescription = div.getElementsByClass("item_description").get(0).text();

            if(episodeDescription.trim().equalsIgnoreCase(unreleasedEpisodeDescription)){
                break;
            }

            //set to true since there is one episodes in the season that is released.
            isSeasonValid = true;

            Episode episode = new Episode();

            episode.setSeriesImdbID(seriesImdbID);
            episode.setSeasonNumber(seasonNumber);
            episode.setWeightage(100);

            //first meta tag in div will have episode number as value
            episode.setNumberInSeason(Integer.parseInt(div.getElementsByTag("meta").get(0).attr("content")));

            //first href will have episode title in title attribute
            episode.setTitle(div.getElementsByTag("a").get(0).attr("title"));

            //div with class item_description will have episode description
            episode.setDescription(episodeDescription);

            //save series to db
            DbHandler.getInstance(context).createEpisode(episode);
        }

        return isSeasonValid;
    }
}
