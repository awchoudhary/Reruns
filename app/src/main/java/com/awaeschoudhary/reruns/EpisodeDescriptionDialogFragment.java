package com.awaeschoudhary.reruns;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awaeschoudhary on 10/14/17.
 */

public class EpisodeDescriptionDialogFragment extends DialogFragment{

    @BindString(R.string.episode_detail_dialog_button_ok) String okButtonText;

    @BindView(R.id.textview_season_and_episode_number)
    TextView seasonAndEpisodeNumberView;

    @BindView(R.id.textview_episode_name)
    TextView episodeNameView;

    @BindView(R.id.textview_episode_description)
    TextView episodeDescriptionView;


    //codes for bundle args
    private static final String EPISODE = "episode";

    private Episode episode;

    public static EpisodeDescriptionDialogFragment newInstance(Episode episode) {
        EpisodeDescriptionDialogFragment dialog = new EpisodeDescriptionDialogFragment();

        //supply args here
        Bundle args = new Bundle();
        args.putSerializable(EPISODE, episode);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        episode = (Episode) getArguments().getSerializable(EPISODE);

        // inflate view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_episode_description, null);

        ButterKnife.bind(this, dialogView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);

        seasonAndEpisodeNumberView.setText("Season " + episode.getSeasonNumber() + ", Episode " + episode.getNumberInSeason());
        episodeNameView.setText(episode.getTitle());
        episodeDescriptionView.setText(episode.getDescription());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(okButtonText, new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog,int id){
                        dismiss();
                    }
                });

        return builder.create();
    }
}
