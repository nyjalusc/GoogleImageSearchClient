package com.example.naugustine.gridimagesearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.naugustine.gridimagesearch.R;
import com.example.naugustine.gridimagesearch.models.ImageResult;
import com.example.naugustine.gridimagesearch.views.DynamicHeightImageViewCustom;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    private static class ViewHolder {
        DynamicHeightImageViewCustom ivImage;
    }

    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ImageResult imageResult = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // Create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
            viewHolder.ivImage = (DynamicHeightImageViewCustom) convertView.findViewById(R.id.ivImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setViewValues(viewHolder, imageResult, position);
        return convertView;
    }

    // Sets the value of items in the view
    private void setViewValues(ViewHolder viewHolder, ImageResult imageResult, int position) {
        // Reset the image view; Will show old image if the view is recycled
        viewHolder.ivImage.setImageResource(0);
        double positionHeight = getPositionRatio(imageResult);
        viewHolder.ivImage.setHeightRatio(positionHeight);
        // Remotely download the image data and show it in the view
        Picasso.with(getContext()).load(imageResult.getThumbURL()).error(R.drawable.error_loading).into(viewHolder.ivImage);
    }

    // Calculates the ratio on the basis of height and width of the image
    private double getPositionRatio(ImageResult imageResult) {
        Double width = Double.parseDouble(imageResult.getThumbWidth());
        Double height = Double.parseDouble(imageResult.getThumbHeight());
        // This ratio works best
        return height / width;
    }
}
