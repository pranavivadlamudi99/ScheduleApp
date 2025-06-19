# ScheduleApp

ScheduleApp is a simple Android app that shows an NFL team's game schedule using data from a remote JSON API.

## Features

- Filters: All Games, Home, Away
- Light/Dark mode toggle
- Displays Game scores, dates, times
- Team logos are loaded with the help of Glide
- Game Center and Highlights buttons are added


## Layouts

- `activity_main.xml`: Main UI with toolbar, filter, RecyclerView
- `header_schedule.xml`: Toolbar with title and theme toggle
- `item_filter.xml`: Radio buttons for filter
- `item_header.xml`: Header like "Regular Season"
- `item_schedule.xml`: Game card with logos, scores, and buttons

## Theme

- Dark mode toggle via toolbar moon icon
- Preferences saved across launches

## Notes

- Team logos: `https://s3.amazonaws.com/yc-app-resources/nfl/logos/nfl_{tricode}_light.png`
- App icon generated via appicon.co
- Fonts from: [Google Fonts](https://fonts.google.com/)
