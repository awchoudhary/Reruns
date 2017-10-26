package com.awaeschoudhary.reruns;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView seriesRecyclerView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragmentLayout = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, fragmentLayout);

        //get all series and populate the recyclerView
        ArrayList<Series> seriesList = DbHandler.getInstance(getContext()).getAllSeries();

        seriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SeriesRecycleViewAdapter adapter = new SeriesRecycleViewAdapter(getActivity(), seriesList);
        seriesRecyclerView.setAdapter(adapter);
        seriesRecyclerView.setNestedScrollingEnabled(true);

        return fragmentLayout;
    }
}
