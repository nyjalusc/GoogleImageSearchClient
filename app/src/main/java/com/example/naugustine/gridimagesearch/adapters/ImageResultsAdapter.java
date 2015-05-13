package com.example.naugustine.gridimagesearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.naugustine.gridimagesearch.R;
import com.example.naugustine.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    private static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
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
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
//            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setViewValues(viewHolder, imageResult);
        return convertView;
    }

    private void setViewValues(ViewHolder viewHolder, ImageResult imageResult) {
        // Reset the image view; Will show old image if the view is recycled
        viewHolder.ivImage.setImageResource(0);
        // Remotely download the image data and show it in the view
        Picasso.with(getContext()).load(imageResult.getThumbURL()).into(viewHolder.ivImage);
        // Set the title
//        viewHolder.tvTitle.setText(Html.fromHtml(imageResult.getTitle()));
    }
}
