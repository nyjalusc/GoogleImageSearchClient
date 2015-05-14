package com.example.naugustine.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.naugustine.gridimagesearch.R;
import com.example.naugustine.gridimagesearch.adapters.ImageResultsAdapter;
import com.example.naugustine.gridimagesearch.factories.ImageResultFactory;
import com.example.naugustine.gridimagesearch.interfaces.EndlessScrollListener;
import com.example.naugustine.gridimagesearch.models.ImageResult;
import com.example.naugustine.gridimagesearch.net.SearchClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {
    private EditText etQuery;
    private String query;
    private com.etsy.android.grid.StaggeredGridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultFactory imageResultFactory;
    private ImageResultsAdapter aImageResults;
    private SearchClient searchClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchClient = new SearchClient();
        setupViews();
        setupAdapter();
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
    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
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
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
//                loadDataFromApi(page);
                loadData(query, page);
            }
        });
    }

    // Invokes the searchClient to execute the request
    private void loadData(String query, int page) {
        // Execute GET request
        searchClient.getImages(query, page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                imageResultFactory = new ImageResultFactory();
                // Load the models in adapter; this will also modify the data in underlying datasource
                // No need to use notifyDataSetChanged() if using this method
                aImageResults.addAll(imageResultFactory.getImageResults(response));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    // Fired when search button is clicked
    public void onImageSearch(View view) {
        // Get the query string
        query = etQuery.getText().toString();
        // Clear old data
        aImageResults.clear();
        // Fetch first page of result and populate it in the adapter
        loadData(query, 0);
    }
}
