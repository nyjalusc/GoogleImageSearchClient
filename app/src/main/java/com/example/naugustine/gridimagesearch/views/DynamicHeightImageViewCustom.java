package com.example.naugustine.gridimagesearch.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.etsy.android.grid.util.DynamicHeightImageView;

public class DynamicHeightImageViewCustom extends DynamicHeightImageView {

    private Path clipPath;
    private RectF rect;

    public DynamicHeightImageViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightImageViewCustom(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        clipPath = new Path();
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        rect.left = 0;
        rect.top = 0;
        rect.right = this.getWidth();
        rect.bottom = this.getHeight();
        clipPath.addRoundRect(rect, 8, 8, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}