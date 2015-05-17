package com.example.naugustine.gridimagesearch.net;

import android.util.Log;

import com.example.naugustine.gridimagesearch.models.AdvancedFilters;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.HashMap;

public class SearchClient {
    private static final String API_BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
    private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?";
    private static final int RESULTS_PER_PAGE = 8;
    private HashMap<EndpointArgType, String> endpointKeyMap;
    private AsyncHttpClient client;

    public enum EndpointArgType {
        QUERY, VERSION, RESULTS_PER_PAGE, PAGE, SIZE, COLOR, TYPE, SAFETY_LEVEL, SITE
    }

    public SearchClient() {
        this.client = new AsyncHttpClient();
        initializeMap();
    }

    // Map of REST endpoint ArgType => endpoint argument
    private void initializeMap() {
        endpointKeyMap = new HashMap<>();
        endpointKeyMap.put(EndpointArgType.QUERY, "q");
        endpointKeyMap.put(EndpointArgType.VERSION, "v");
        endpointKeyMap.put(EndpointArgType.RESULTS_PER_PAGE, "rsz");
        endpointKeyMap.put(EndpointArgType.PAGE, "start");
        endpointKeyMap.put(EndpointArgType.SIZE, "imgsz");
        endpointKeyMap.put(EndpointArgType.COLOR, "imgcolor");
        endpointKeyMap.put(EndpointArgType.TYPE, "imgtype");
        endpointKeyMap.put(EndpointArgType.SAFETY_LEVEL, "safe");
        endpointKeyMap.put(EndpointArgType.SITE, "as_sitesearch");
    }

    // Constructs the complete api URL
    private String getApiUrl(String query, int page) {
        return API_BASE_URL + query + "&rsz=" + RESULTS_PER_PAGE + "&start=" + page;
    }

    // Generates a complete URL by parsing the advanced filter settings
    private String getCompleteURL(String query, int page, AdvancedFilters advancedFilters) {
        String result = BASE_URL;
        // Mandatory arguments
        result += endpointKeyMap.get(EndpointArgType.VERSION) + "=1.0";
        result += getEndpointArg(endpointKeyMap.get(EndpointArgType.QUERY), query);

        // Optional Arguments
        // "Any" is a key to indicate no filter setting
        String size = advancedFilters.getSize();
        if (!size.equals("Any")) {
            result += getEndpointArg(endpointKeyMap.get(EndpointArgType.SIZE), size.toLowerCase());
        }

        String color = advancedFilters.getColor();
        if (!color.equals("Any")) {
            result += getEndpointArg(endpointKeyMap.get(EndpointArgType.COLOR), color.toLowerCase());
        }

        String type = advancedFilters.getType();
        if (!type.equals("Any")) {
            result += getEndpointArg(endpointKeyMap.get(EndpointArgType.TYPE), type.toLowerCase());
        }

        String safetyLevel = advancedFilters.getSafetyLevel();
        // "moderate" is the default safety level
        if (!safetyLevel.equals("moderate")) {
            result += getEndpointArg(endpointKeyMap.get(EndpointArgType.SAFETY_LEVEL), safetyLevel.toLowerCase());
        }

        String site = advancedFilters.getSite();
        if (!site.isEmpty()) {
            result += getEndpointArg(endpointKeyMap.get(EndpointArgType.SITE), site.toLowerCase());
        }

        result += getEndpointArg(endpointKeyMap.get(EndpointArgType.RESULTS_PER_PAGE), RESULTS_PER_PAGE + "");
        result += getEndpointArg(endpointKeyMap.get(EndpointArgType.PAGE), page + "");

        return result;
    }

    private String getEndpointArg(String key, String value) {
        return "&" + key + "=" + value;
    }

    // Performs a GET Request
    public void getImages(final String query, int page, AdvancedFilters advancedFilters, JsonHttpResponseHandler handler) {
        try {
            String url = getCompleteURL(query, page, advancedFilters);
            Log.d("URL", url);
            client.get(url, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
