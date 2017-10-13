package com.awaeschoudhary.reruns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by awaeschoudhary on 10/8/17.
 */

public class DbHandler extends SQLiteOpenHelper{
    private static DbHandler mInstance = null;

    //Database information
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "RerunsDB";

    //Series table info
    private static final String TABLE_SERIES = "Series";
    private static final String KEY_SERIES_ID = "ID";
    private static final String KEY_SERIES_IMDB_ID = "ImdbID";
    private static final String KEY_SERIES_TITLE = "Title";
    private static final String KEY_SERIES_NUMBER_OF_SEASONS = "NumberOfSeasons";

    //episodes table info
    private static final String TABLE_EPISODES = "Episodes";
    private static final String KEY_EPISODE_ID = "ID";
    private static final String KEY_EPISODE_SERIES_IMDB_ID = "SeriesID";
    private static final String KEY_EPISODE_SEASON_NUMBER = "SeasonNumber";
    private static final String KEY_EPISODE_NUMBER_IN_SEASON = "NumberInSeason";
    private static final String KEY_EPISODE_TITLE = "Title";
    private static final String KEY_EPISODE_DESCRIPTION = "Description";
    private static final String KEY_EPISODE_WEIGHTAGE = "Weightage";

    public static synchronized DbHandler getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (mInstance == null) {
            mInstance = new DbHandler(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     */
    private DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_SERIES = "CREATE TABLE " + TABLE_SERIES + "("
                + KEY_SERIES_ID + " INTEGER PRIMARY KEY," + KEY_SERIES_TITLE + " TEXT,"
                + KEY_SERIES_NUMBER_OF_SEASONS + " INTEGER)";

        String CREATE_TABLE_EPISODES = "CREATE TABLE " + TABLE_EPISODES + "("
                + KEY_EPISODE_ID + " INTEGER PRIMARY KEY," + KEY_EPISODE_SERIES_IMDB_ID + " TEXT,"
                + KEY_EPISODE_SEASON_NUMBER + " INTEGER," + KEY_EPISODE_NUMBER_IN_SEASON + " INTEGER,"
                + KEY_EPISODE_TITLE + " TEXT," + KEY_EPISODE_DESCRIPTION + " TEXT," + KEY_EPISODE_WEIGHTAGE + " INTEGER)";

        db.execSQL(CREATE_TABLE_SERIES);
        db.execSQL(CREATE_TABLE_EPISODES);
    }

    //called when app is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EPISODES);

        // Create tables again
        onCreate(db);
    }

    //Insert new record to series table
    public void createSeries(Series s){
        SQLiteDatabase db = this.getWritableDatabase();

        //place all values
        ContentValues values = new ContentValues();
        values.put(KEY_SERIES_IMDB_ID, s.getImdbID());
        values.put(KEY_SERIES_TITLE, s.getTitle());
        values.put(KEY_SERIES_NUMBER_OF_SEASONS, s.getNumberOfSeasons());

        // Insert Row
        //2nd argument is String that specifies nullable column name
        db.insert(TABLE_SERIES, null, values);

        db.close(); // Closing database connection
    }

    // get all series
    public ArrayList<Series> getAllSeries() {
        ArrayList<Series> series = new ArrayList<Series>();

        String selectQuery = "SELECT  * FROM " + TABLE_SERIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                Series s = new Series();

                s.setID(Integer.parseInt(cursor.getString(0)));
                s.setImdbID(cursor.getString(1));
                s.setTitle(cursor.getString(2));
                s.setNumberOfSeasons(Integer.parseInt(cursor.getString(3)));

                series.add(s);

            } while (cursor.moveToNext());
        }

        //close the cursor
        cursor.close();

        // return book list
        return series;
    }


    // Delete specific record from series table
    public void deleteSeries(Series s) {
        SQLiteDatabase db = this.getWritableDatabase();

        //delete series
        db.delete(TABLE_SERIES, KEY_SERIES_ID + " = ?",
                new String[] { Integer.toString(s.getID())});

        db.close();

        //delete all episodes for series from episodes table
        deleteEpisodes(s.getID());
    }

    //Insert new record to episodes table
    public void createEpisode(Episode e){
        SQLiteDatabase db = this.getWritableDatabase();

        //place all values
        ContentValues values = new ContentValues();
        values.put(KEY_EPISODE_SERIES_IMDB_ID, e.getSeriesImdbID());
        values.put(KEY_EPISODE_SEASON_NUMBER, e.getSeasonNumber());
        values.put(KEY_EPISODE_NUMBER_IN_SEASON, e.getNumberInSeason());
        values.put(KEY_EPISODE_TITLE, e.getTitle());
        values.put(KEY_EPISODE_DESCRIPTION, e.getDescription());
        values.put(KEY_EPISODE_WEIGHTAGE, e.getWeightage());

        // Insert Row
        //2nd argument is String that specifies nullable column name
        db.insert(TABLE_EPISODES, null, values);

        db.close(); // Closing database connection
    }

    // get specific episode
    public ArrayList<Episode> getEpisode(int episodeID) {
        ArrayList<Episode> episodes = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_EPISODES + " WHERE " + KEY_EPISODE_ID + "=" + episodeID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                //populate new note object with row values
                Episode e = new Episode();
                e.setID(Integer.parseInt(cursor.getString(0)));
                e.setSeriesImdbID(cursor.getString(1));
                e.setSeasonNumber(Integer.parseInt(cursor.getString(2)));
                e.setNumberInSeason(Integer.parseInt(cursor.getString(3)));
                e.setTitle(cursor.getString(4));
                e.setDescription(cursor.getString(5));
                e.setWeightage(Integer.parseInt(cursor.getString(6)));

                episodes.add(e);
            } while (cursor.moveToNext());
        }

        //close the cursor
        cursor.close();

        // return book list
        return episodes;
    }

    //delete episodes for a given series ID
    public void deleteEpisodes(int seriesID) {
        SQLiteDatabase db = this.getWritableDatabase();

        //delete series
        db.delete(TABLE_EPISODES, KEY_EPISODE_SERIES_IMDB_ID + " = ?",
                new String[] { Integer.toString(seriesID)});

        db.close();
    }

}
