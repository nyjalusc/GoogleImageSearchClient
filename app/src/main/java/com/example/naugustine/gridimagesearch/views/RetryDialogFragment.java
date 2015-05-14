package com.example.naugustine.gridimagesearch.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.example.naugustine.gridimagesearch.activities.ImageDisplayActivity;
import com.example.naugustine.gridimagesearch.models.ImageResult;

public class RetryDialogFragment extends DialogFragment {
    public RetryDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static RetryDialogFragment newInstance(View v, ImageResult imageResult, String title) {
        RetryDialogFragment frag = new RetryDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Would you like to retry?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ImageDisplayActivity activity = (ImageDisplayActivity) getActivity();
                activity.showFullImage();
                // on success
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }
}
