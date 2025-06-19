package com.pranavi.scheduleapp.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.pranavi.scheduleapp.R;

/**
 * Custom TextView applying consistent app font and colors.
 */
public class ScheduleTextView extends AppCompatTextView {

    /**
     * Constructor for inflating from XML.
     *
     * @param context the context
     * @param attrs attribute set from XML
     */
    public ScheduleTextView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.scheduleTextViewStyle);
        init();
    }

    /**
     * Constructor for creating the view programmatically.
     *
     * @param context the context
     */
    public ScheduleTextView(Context context) {
        super(context, null, R.attr.scheduleTextViewStyle);
        init();
    }

    /**
     * Initializes custom font and styling.
     */
    private void init() {
        setTextColor(getResources().getColor(R.color.textPrimary));
        setTypeface(ResourcesCompat.getFont(getContext(), R.font.geologica_regular));

    }
}
