package com.example.naugustine.gridimagesearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
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
    private ShareActionProvider miShareAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        // Use the support actionbar and not the old getActionBar()
//        getSupportActionBar().hide();
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
                setupShareIntent();
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

    // Gets the image URI and setup the associated share intent to hook into the provider
    public void setupShareIntent() {
        // Fetch Bitmap Uri locally
        ImageView ivImage = (ImageView) findViewById(R.id.ivImageResult);
        Uri bmpUri = getLocalBitmapUriBasic(ivImage); // see previous remote images section
        // Create share intent as described above
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        // Attach share event to the menu item provider
        miShareAction.setShareIntent(shareIntent);
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUriBasic(ImageView imageView) {
        ImageView siv = (ImageView) findViewById(R.id.ivImageResult);
        Drawable mDrawable = siv.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                mBitmap, "Image Description", null);

        Uri uri = Uri.parse(path);
        return uri;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        // Get reference to the menuItem
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch reference to share action provider
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
