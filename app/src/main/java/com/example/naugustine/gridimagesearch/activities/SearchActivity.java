package com.example.naugustine.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.example.naugustine.gridimagesearch.R;
import com.example.naugustine.gridimagesearch.adapters.ImageResultsAdapter;
import com.example.naugustine.gridimagesearch.factories.ImageResultFactory;
import com.example.naugustine.gridimagesearch.interfaces.EndlessScrollListener;
import com.example.naugustine.gridimagesearch.models.AdvancedFilters;
import com.example.naugustine.gridimagesearch.models.ImageResult;
import com.example.naugustine.gridimagesearch.net.ConnectivityChecker;
import com.example.naugustine.gridimagesearch.net.SearchClient;
import com.example.naugustine.gridimagesearch.views.AdvancedFiltersDialog;
import com.example.naugustine.gridimagesearch.views.RetryDialogFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity implements RetryDialogFragment.DialogResultListener {
    private String searchQuery;
    private com.etsy.android.grid.StaggeredGridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultFactory imageResultFactory;
    private ImageResultsAdapter aImageResults;
    private SearchClient searchClient;
    private ProgressBar progressBar;
    private static AdvancedFilters advancedFilters;
    private ConnectivityChecker connectivityChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        connectivityChecker = new ConnectivityChecker();
        searchClient = new SearchClient();
        setupViewListeners();
        setupAdapter();
        // Get reference to the progressbar
        progressBar = (ProgressBar) findViewById(R.id.pbLoadingImage);
        progressBar.setVisibility(View.INVISIBLE);
        initFiltersModel();
    }

    // Initialize a single instance of Advanced Filter settings
    private void initFiltersModel() {
        advancedFilters = new AdvancedFilters();
        advancedFilters.init();
    }

    // Instantiates datasources, adapter and the connects the adapter to the view
    private void setupAdapter() {
        // Creates the datasource
        imageResults = new ArrayList<>();
        // Creates the adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        // Connect the adapter to the gridView
        gvResults.setAdapter(aImageResults);
    }

    // Gets references to views
    private void setupViewListeners() {
        gvResults = (com.etsy.android.grid.StaggeredGridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch Image display activity
                // Create intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                // Get the image result to display
                ImageResult result = imageResults.get(position);
                // Pass image result into the intent
                i.putExtra("result", result);
                // Start activity
                startActivity(i);
            }
        });
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Check for internet connectivity
                if (!networkCheck()) {
                    return;
                }
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadData(searchQuery, page);
            }
        });
    }

    // Invokes the searchClient to execute the request
    private void loadData(String query, int page) {
        // Execute GET request
        searchClient.getImages(query, page, advancedFilters, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Remove the progressbar
                progressBar.setVisibility(View.GONE);
                imageResultFactory = new ImageResultFactory();
                // Load the models in adapter; this will also modify the data in underlying datasource
                // No need to use notifyDataSetChanged() if using this method
                aImageResults.addAll(imageResultFactory.getImageResults(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showAlertDialog("Sorry!", "Failed to fetch results");
            }
        });
    }

    // Invoke the advanced filters settings fragment
    private void showAdvancedFilters() {
        FragmentManager fm = getSupportFragmentManager();
        // This is how you pass data from an activity to a fragment
        // Passing the single instance of filters model
        AdvancedFiltersDialog settingsDialog = AdvancedFiltersDialog.newInstance(advancedFilters);
        settingsDialog.show(fm, "fragment_advanced_filters");
    }

    // Check for network connectivity; Show the alertdialog to retry the request
    private boolean networkCheck() {
        if (!connectivityChecker.isNetworkAvailable(this)) {
            showAlertDialog("No internet", "It looks like you have lost network connectivity");
            return false;
        }
        return true;
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
            startRequest();
        }
    }

    // Main method of this activity
    private void startRequest() {
        // Check for internet connectivity
        if (!networkCheck()) {
            // Do not proceed without internet
            return;
        }
        // Show the progress bar while the request is processed
        progressBar.setVisibility(View.VISIBLE);
        aImageResults.clear();
        // Fetch first page of result and populate it in the adapter
        loadData(searchQuery, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        // Get reference to SearchView
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Show the progress bar while the request is processed
                searchQuery = query;
                startRequest();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miSettings) {
            showAdvancedFilters();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
