package com.pranavi.scheduleapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pranavi.scheduleapp.adapter.ScheduleAdapter;
import com.pranavi.scheduleapp.model.GameItem;
import com.pranavi.scheduleapp.model.HeaderItem;
import com.pranavi.scheduleapp.model.ScheduleListItem;
import com.pranavi.scheduleapp.model.ScheduleResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Main activity that displays the NFL schedule.
 * Handles fetching schedule data from a remote JSON,
 * manages theme toggling (light/dark),
 * and filtering the schedule by game type (All, Home, Away).
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private static final String JSON_URL =
            "http://files.yinzcam.com.s3.amazonaws.com/iOS/interviews/ScheduleExercise/schedule.json";
    private ProgressBar progressBar;
    private TextView errorText;
    private List<ScheduleListItem> allItemsUnfiltered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.teamSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView toggleBtn = toolbar.findViewById(R.id.themeToggleBtn);

        toggleBtn.setImageResource(isDark ? R.drawable.ic_moon_filled : R.drawable.ic_moon_outline);

        toggleBtn.setOnClickListener(v -> {
            boolean newMode = !prefs.getBoolean("dark_mode", false);

            AppCompatDelegate.setDefaultNightMode(
                    newMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );

            prefs.edit().putBoolean("dark_mode", newMode).apply();

            recreate();
        });


        fetchScheduleData();
    }

    /**
     * Fetches schedule data from a remote JSON asynchronously,
     * parses it, and updates the UI.
     * Displays progress indicator and error messages as needed.
     */
    private void fetchScheduleData() {
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);

        new Thread(() -> {
            try {
                URL url = new URL(JSON_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);

                ScheduleResponse response = new Gson().fromJson(sb.toString(),
                        ScheduleResponse.class);

                if (response != null && response.GameSection != null) {
                    List<ScheduleListItem> allItems = new ArrayList<>();

                    for (ScheduleResponse.GameSection section : response.GameSection) {
                        allItems.add(new HeaderItem(section.Heading));
                        for (ScheduleResponse.Game game : section.Game) {
                            allItems.add(new GameItem(game));
                        }
                    }

                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        this.allItemsUnfiltered = allItems;
                        adapter = new ScheduleAdapter(MainActivity.this, allItems);
                        recyclerView.setAdapter(adapter);
                        RadioGroup filter = findViewById(R.id.gameFilter);
                        filter.setOnCheckedChangeListener((group, checkedId) -> {
                            if (allItemsUnfiltered == null) return;

                            List<ScheduleListItem> filtered = new ArrayList<>();
                            for (ScheduleListItem item : allItemsUnfiltered) {
                                if (item instanceof HeaderItem) {
                                    filtered.add(item);
                                } else if (item instanceof GameItem) {
                                    ScheduleResponse.Game game = ((GameItem) item).game;

                                    if (checkedId == R.id.radio_home && game.IsHome) {
                                        filtered.add(item);
                                    } else if (checkedId == R.id.radio_away && !game.IsHome) {
                                        filtered.add(item);
                                    } else if (checkedId == R.id.radio_all) {
                                        filtered.add(item);
                                    }
                                }
                            }

                            adapter = new ScheduleAdapter(MainActivity.this, filtered);
                            recyclerView.setAdapter(adapter);
                        });


                    });
                } else {
                    showError();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError();
            }
        }).start();
    }

    /**
     * Displays the error message and hides the progress bar.
     * Called when there's an issue fetching the schedule data.
     */
    private void showError() {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            errorText.setVisibility(View.VISIBLE);
        });
    }

}
