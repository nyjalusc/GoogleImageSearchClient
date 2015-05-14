package com.example.naugustine.gridimagesearch.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchClient {
    private static final String API_BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
    private static final int RESULTS_PER_PAGE = 8;
    private AsyncHttpClient client;

    public SearchClient() {
        this.client = new AsyncHttpClient();
    }

    // Constructs the complete api URL
    private String getApiUrl(String query, int page) {
        return API_BASE_URL + query + "&rsz=" + RESULTS_PER_PAGE + "&start=" + page;
    }

    // Performs a GET Request
    public void getImages(final String query, int page, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl(query, page);
            client.get(url, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
