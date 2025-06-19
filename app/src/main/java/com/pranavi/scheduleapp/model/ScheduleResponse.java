package com.pranavi.scheduleapp.model;

import java.util.List;

/**
 * Data model classes representing the JSON schedule response.
 * Used with Gson for JSON deserialization.
 */
public class ScheduleResponse {

    public List<GameSection> GameSection;

    /**
     * Represents a section of games like Regular Season.
     */
    public static class GameSection {
        public String Heading;
        public List<Game> Game;
    }

    /**
     * Represents a single game.
     */
    public static class Game {
        public String Type;
        public boolean IsHome;
        public String HomeScore;
        public String AwayScore;
        public Opponent Opponent;
        public DateInfo Date;
        public String ScheduleHeader;
        public List<Button> Buttons;

        /**
         * Represents the opponent team.
         */
        public static class Opponent {
            public String FullName;
            public String TriCode;
        }

        /**
         * Represents the game date.
         */
        public static class DateInfo {
            public String Timestamp;
        }

        /**
         * Represents a game button.
         */
        public static class Button {
            public String Title;
            public String URL;
        }
    }

}
