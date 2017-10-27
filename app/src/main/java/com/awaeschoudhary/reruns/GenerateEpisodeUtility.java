package com.awaeschoudhary.reruns;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by awaeschoudhary on 10/25/17.
 */

public class GenerateEpisodeUtility {

    public static Episode generateEpisode(String seriesId, int minSeason, int maxSeason, Context context){

        ArrayList<Episode> episodes = DbHandler.getInstance(context).getEpisodesBetweenSeasons(seriesId, minSeason, maxSeason);

        List<Integer> weightageSums = new ArrayList<>();

        //calculate frequency distribution for episodes and store them
        int sum = 0;

        for(Episode e : episodes){
            sum += e.getWeightage();

            weightageSums.add(sum);
        }

        //get a random number within the distribution and get the index for the episode using binary search
        Random randomGenerator = new Random();
        int random = randomGenerator.nextInt(sum) + 1;
        int index = Collections.binarySearch(weightageSums, random);

        //If number is not found, binary search returns -(Index value should be in) - 1
        if(index < 0){
            index = -(index) - 1;
        }

        return episodes.get(index);
    }
}
