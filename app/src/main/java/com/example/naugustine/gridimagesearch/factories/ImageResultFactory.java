package com.example.naugustine.gridimagesearch.factories;

import com.example.naugustine.gridimagesearch.models.ImageResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageResultFactory {

    private ArrayList<ImageResult> imageResults;

    public ImageResultFactory() {
        imageResults = new ArrayList<>();
    }

    // Parses the JSON response and returns an arraylist of models
    public ArrayList<ImageResult> getImageResults(JSONObject response) {
        try {
            JSONArray imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
            prepareModels(imageResultsJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageResults;
    }

    // Converts JSONArray to imageResult model objects and populates the arraylist
    private void prepareModels(JSONArray results) throws JSONException {
        for (int i = 0; i < results.length(); i++) {
            ImageResult imageResult = new ImageResult();
            JSONObject result = results.getJSONObject(i);
            // Title
            imageResult.setTitle(result.getString("title"));
            // FullURL
            imageResult.setFullURL(result.getString("url"));
            // ThumbURL
            imageResult.setThumbURL(result.getString("tbUrl"));

            // add the model to ArrayList imageResults
            imageResults.add(imageResult);
        }
    }
}
