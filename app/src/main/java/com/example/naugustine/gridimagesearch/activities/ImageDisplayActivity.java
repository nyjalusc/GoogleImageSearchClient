package com.example.naugustine.gridimagesearch.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.naugustine.gridimagesearch.R;
import com.example.naugustine.gridimagesearch.models.ImageResult;
import com.example.naugustine.gridimagesearch.views.RetryDialogFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends ActionBarActivity {

    private ProgressBar progressBar;
    private ImageView ivImageResult;
    private ImageResult imageResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        // Use the support actionbar and not the old getActionBar()
        getSupportActionBar().hide();
        showFullImage();
    }

    // Extracts the model object from intent and loads the image in the view
    public void showFullImage() {
        // Get reference to the progressbar
        progressBar = (ProgressBar) findViewById(R.id.pbLoadingImage);
        // Explicitly setting it to visible because this method is also called from the dialog fragment
        progressBar.setVisibility(View.VISIBLE);
        // Get the url from intent
        imageResult = (ImageResult) getIntent().getSerializableExtra("result");
        // Find the image
        ivImageResult = (ImageView) findViewById(R.id.ivImageResult);
        // Load the image using Picasso
        Picasso.with(this).load(imageResult.getFullURL()).error(R.drawable.error_loading).into(ivImageResult, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                // May be show a dialog fragment
                showAlertDialog();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        RetryDialogFragment alertDialog = RetryDialogFragment.newInstance(ivImageResult, imageResult, "Failed to load");
        alertDialog.show(fm, "fragment_alert");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
