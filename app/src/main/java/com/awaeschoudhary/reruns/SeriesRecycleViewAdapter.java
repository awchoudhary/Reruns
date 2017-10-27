package com.awaeschoudhary.reruns;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by awaeschoudhary on 10/15/17.
 */

public class SeriesRecycleViewAdapter
        extends RecyclerView.Adapter<SeriesRecycleViewAdapter.SeriesViewHolder> {

    private Context context;
    private ArrayList<Series> seriesList;

    public SeriesRecycleViewAdapter(Context context, ArrayList<Series> seriesList){
        this.context = context;
        this.seriesList = seriesList;
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }

    @Override
    public SeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_series, parent, false);
        SeriesViewHolder viewHolder = new SeriesViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SeriesViewHolder seriesViewHolder, int position) {
        final Series series = seriesList.get(position);

        seriesViewHolder.seriesNameTextView.setText(series.getTitle());

        final RangeSeekBar seekBar = seriesViewHolder.seekBar;

        seekBar.setRangeValues(1, series.getNumberOfSeasons());

        seriesViewHolder.getEpisodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Episode e = GenerateEpisodeUtility.generateEpisode(series.getImdbID(), seekBar.getSelectedMinValue().intValue(),
                        seekBar.getSelectedMaxValue().intValue(), context);

                Toast.makeText(context, e.getSeasonNumber() + " " + e.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateEntries(Series series) {
        seriesList.add(series);
        notifyDataSetChanged();
    }

    public static class SeriesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view_series)
        CardView cardView;

        @BindView(R.id.text_view_series_name)
        TextView seriesNameTextView;

        @BindView(R.id.seekbar_series_range)
        RangeSeekBar seekBar;

        @BindView(R.id.button_get_episode)
        AppCompatButton getEpisodeButton;

        SeriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
