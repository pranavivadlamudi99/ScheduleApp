package com.pranavi.scheduleapp.model;

/**
 * Represents a game item in the schedule list.
 * Implements ScheduleListItem interface.
 */
public class GameItem implements ScheduleListItem {
    public ScheduleResponse.Game game;

    /**
     * Creates a new GameItem.
     *
     * @param game the game object
     */
    public GameItem(ScheduleResponse.Game game) {
        this.game = game;
    }
}
