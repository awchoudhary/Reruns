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

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awaeschoudhary on 10/14/17.
 */

public class LoadSeriesDialogFragment extends DialogFragment{

    @BindString(R.string.load_series_dialog_button_save) String saveButtonText;
    @BindString(R.string.load_series_dialog_button_cancel) String cancelButtonText;
    @BindString(R.string.load_series_dialog_prompt) String dialogMessage;
    @BindString(R.string.error_series_already_loaded) String errorAlreadyLoaded;

    @BindView(R.id.load_series_dialog_input_id) TextView idInput;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // inflate view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_load_series, null);

        ButterKnife.bind(this, dialogView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);

        builder.setMessage(dialogMessage);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(saveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog,int id){
                        createAndExtractSeries();
                        dismiss();
                    }
                })
                .setNegativeButton(cancelButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoadSeriesDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private void createAndExtractSeries(){
        String idInputString = idInput.getText().toString().trim();

        //We will not extract a series that has already been extracted
        List<Series> series = DbHandler.getInstance(getContext()).getSeriesByImdbId(idInputString);

        if(series.size() > 0){
            Toast.makeText(getContext(), errorAlreadyLoaded, Toast.LENGTH_SHORT).show();
            return;
        }

        ExtractAndSaveSeriesTask task = new ExtractAndSaveSeriesTask(getContext());

        task.execute(idInputString);
    }
}
