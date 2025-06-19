package com.pranavi.scheduleapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Custom ImageView with consistent scaling and padding.
 * Used for displaying team logos in schedule items.
 */
public class ScheduleImageView extends AppCompatImageView {

    /**
     * Constructor used when inflating from XML.
     *
     * @param context the context
     * @param attrs attribute set
     */
    public ScheduleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructor used when creating the view programmatically.
     *
     * @param context the context
     */
    public ScheduleImageView(Context context) {
        super(context);
        init();
    }

    /**
     * Applies default padding, scaling, and optional tint if needed.
     */
    private void init() {
        setScaleType(ScaleType.FIT_CENTER);
        int size = (int) getResources().getDisplayMetrics().density * 32;
        setLayoutParams(new ViewGroup.LayoutParams(size, size));
    }
}
