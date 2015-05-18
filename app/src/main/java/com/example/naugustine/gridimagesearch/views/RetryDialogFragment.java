package com.example.naugustine.gridimagesearch.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class RetryDialogFragment extends DialogFragment {
    DialogResultListener dialogResultListener;
    private static final String additionalMessage = "\nClick yes if you would like to retry";

    public RetryDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    // This interface needs to be implemented by the parent activity
    public interface DialogResultListener {
        // This method will be used to send the result back from the fragment back to the activity
        // "Fragment => Activity"
        public void getRetryRequest(boolean result);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dialogResultListener = (DialogResultListener) activity;
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            dialogResultListener = (DialogResultListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DialogResultListener");
        }
    }

    // This method sends the result of the fragment back to the parent activity
    public void sendResult(boolean result) {
        // getRetryRequest() is implemented by the parent activity
        dialogResultListener.getRetryRequest(result);
    }

    // This is used to pass the data from "Activity => Fragment"
    public static RetryDialogFragment newInstance(String title, String message) {
        RetryDialogFragment frag = new RetryDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        message += additionalMessage;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult(true);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult(false);
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }
}
