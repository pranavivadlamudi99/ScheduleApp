package com.pranavi.scheduleapp.model;

/**
 * Represents a section header in the schedule list, e.g., "REGULAR SEASON".
 * Implements ScheduleListItem interface.
 */
public class HeaderItem implements ScheduleListItem {
    public String title;

    /**
     * Creates a new HeaderItem.
     *
     * @param title the section title
     */
    public HeaderItem(String title) {
        this.title = title;
    }
}
