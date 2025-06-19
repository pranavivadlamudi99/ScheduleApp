package com.pranavi.scheduleapp.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.button.MaterialButton;
import com.pranavi.scheduleapp.R;

/**
 * Custom Material Button used across the schedule UI.
 * Applies app-specific styling such as corner radius, font, and colors.
 */
public class ScheduleButton extends MaterialButton {

    /**
     * Constructor used when inflating the view from XML.
     *
     * @param context the context
     * @param attrs attribute set from XML
     */
    public ScheduleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructor used when creating the view programmatically.
     *
     * @param context the context
     */
    public ScheduleButton(Context context) {
        super(context);
        init();
    }

    /**
     * Initializes default appearance: shape, color, font, etc.
     */
    private void init() {
        setCornerRadius(100);
        setTextSize(14);
        setTypeface(ResourcesCompat.getFont(getContext(), R.font.opensans_regular));
        setBackgroundColor(getResources().getColor(R.color.primaryColor));
        setTextColor(getResources().getColor(R.color.white));
    }
}
