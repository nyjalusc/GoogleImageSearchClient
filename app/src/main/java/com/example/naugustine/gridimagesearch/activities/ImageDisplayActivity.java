package com.example.naugustine.gridimagesearch.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.naugustine.gridimagesearch.R;
import com.example.naugustine.gridimagesearch.models.ImageResult;
import com.example.naugustine.gridimagesearch.net.ConnectivityChecker;
import com.example.naugustine.gridimagesearch.views.RetryDialogFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class ImageDisplayActivity extends ActionBarActivity implements RetryDialogFragment.DialogResultListener{

    private ProgressBar progressBar;
    private ImageViewTouch ivImageResult;
    private ImageResult imageResult;
    private ShareActionProvider miShareAction;
    private ConnectivityChecker connectivityChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // This needs to happen before any content is set for the activity
        Styling styling = new Styling();
        // Change the status of the status color
        styling.changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        // Change the color of actionbar
        styling.changeActionBarColor();
        // Render the detailed image
        showFullImage();
    }

    // Check for network connectivity; Show the alertdialog to retry the request
    private boolean networkCheck() {
        connectivityChecker = new ConnectivityChecker();
        if (!connectivityChecker.isNetworkAvailable(this)) {
            showAlertDialog("No internet", "It looks like you have lost network connectivity");
            return false;
        }
        return true;
    }

    // Extracts the model object from intent and loads the image in the view
    public void showFullImage() {
        progressBar = (ProgressBar) findViewById(R.id.pbLoadingImage);
        progressBar.setVisibility(View.INVISIBLE);
        // Check for internet connectivity
        if (!networkCheck()) {
            return;
        }
        // Get reference to the progressbar
        progressBar = (ProgressBar) findViewById(R.id.pbLoadingImage);
        // Explicitly setting it to visible because this method is also called from the dialog fragment
        progressBar.setVisibility(View.VISIBLE);
        // Get the url from intent
        imageResult = (ImageResult) getIntent().getSerializableExtra("result");
        // Find the image
        ivImageResult = (ImageViewTouch) findViewById(R.id.ivImageResult);
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
                showAlertDialog("Sorry!", "Failed to load the image");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // Shows error alert dialog
    private void showAlertDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        RetryDialogFragment alertDialog = RetryDialogFragment.newInstance(title, message);
        alertDialog.show(fm, "fragment_alert");
    }

    // Gets called when the child fragment finishes
    @Override
    public void getRetryRequest(boolean retryRequest) {
        if (retryRequest) {
            showFullImage();
        }
    }

    // Gets the image URI and setup the associated share intent to hook into the provider
    public void setupShareIntent() {
        // Fetch Bitmap Uri locally
        ImageViewTouch ivImage = (ImageViewTouch) findViewById(R.id.ivImageResult);
        Uri bmpUri = getLocalBitmapUriBasic(); // see previous remote images section
        // Create share intent as described above
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        // Attach share event to the menu item provider
        miShareAction.setShareIntent(shareIntent);
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUriBasic() {
        ImageViewTouch siv = (ImageViewTouch) findViewById(R.id.ivImageResult);
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

    // Inner class for logically grouping the styling utility methods
    private class Styling {
        private void changeStatusBarColor() {
            // Window.setStatusBarColor() is only applicable for API 21+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.black_light));
            }
        }

        private void changeActionBarColor() {
            Resources res = getResources();
            android.support.v7.app.ActionBar actionBar;
            actionBar = getSupportActionBar();
            // This will make the actionBar transparent
            actionBar.setBackgroundDrawable(new ColorDrawable(res.getColor(R.color.black_transparent)));
        }
    }
}
