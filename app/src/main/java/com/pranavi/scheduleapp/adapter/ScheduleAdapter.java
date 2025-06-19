package com.pranavi.scheduleapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pranavi.scheduleapp.R;
import com.pranavi.scheduleapp.WebViewActivity;
import com.pranavi.scheduleapp.model.GameItem;
import com.pranavi.scheduleapp.model.HeaderItem;
import com.pranavi.scheduleapp.model.ScheduleListItem;
import com.pranavi.scheduleapp.model.ScheduleResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * RecyclerView adapter that binds schedule data to views.
 * Supports two view types: section headers and game items.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_GAME = 1;

    private final Context context;
    private final List<ScheduleListItem> items;

    public ScheduleAdapter(Context context, List<ScheduleListItem> items) {
        this.context = context;
        this.items = items;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerText;
        HeaderViewHolder(View view) {
            super(view);
            headerText = view.findViewById(R.id.headerText);
        }
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView homeName, awayName, scoreHome, scoreAway, matchInfo, scoreSeparator;
        ImageView homeLogo, awayLogo;
        LinearLayout actionButtons;
        Button gameCenterBtn;
        Button highlightsBtn;


        GameViewHolder(View view) {
            super(view);
            homeName = view.findViewById(R.id.homeTeamName);
            awayName = view.findViewById(R.id.awayTeamName);
            scoreHome = view.findViewById(R.id.scoreHome);
            scoreAway = view.findViewById(R.id.scoreAway);
            matchInfo = view.findViewById(R.id.matchInfo);
            homeLogo = view.findViewById(R.id.homeTeamLogo);
            awayLogo = view.findViewById(R.id.awayTeamLogo);
            actionButtons = view.findViewById(R.id.actionButtons);
            gameCenterBtn = view.findViewById(R.id.gameCenterBtn);
            highlightsBtn = view.findViewById(R.id.highlightsBtn);
            scoreSeparator = view.findViewById(R.id.scoreSeparator);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof HeaderItem ? VIEW_TYPE_HEADER : VIEW_TYPE_GAME;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);
            return new GameViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderItem item = (HeaderItem) items.get(position);
            ((HeaderViewHolder) holder).headerText.setText(item.title);
        } else {
            GameItem item = (GameItem) items.get(position);
            ScheduleResponse.Game game = item.game;
            GameViewHolder h = (GameViewHolder) holder;

            if (game == null) {
                h.homeName.setText(context.getResources().getString(R.string.packers));
                h.awayName.setText("--");
                h.matchInfo.setText("--");
                return;
            }

            String opponentName = (game.Opponent != null && game.Opponent.FullName != null)
                    ? game.Opponent.FullName : "--";

            h.homeName.setText(game.IsHome ? context.getResources().getString(R.string.packers) : opponentName);
            h.awayName.setText(game.IsHome ? opponentName : context.getResources().getString(R.string.packers));

            String triHome = game.IsHome ? "gb" :
                    (game.Opponent != null && game.Opponent.TriCode != null ?
                            game.Opponent.TriCode.toLowerCase() : "nfl");

            String triAway = game.IsHome ?
                    (game.Opponent != null && game.Opponent.TriCode != null ?
                            game.Opponent.TriCode.toLowerCase() : "nfl") : "gb";

            Glide.with(context).load(getLogoUrl(triHome)).into(h.homeLogo);
            Glide.with(context).load(getLogoUrl(triAway)).into(h.awayLogo);

            if ("F".equals(game.Type)) {
                h.scoreHome.setText(game.IsHome ? game.HomeScore : game.AwayScore);
                h.scoreAway.setText(game.IsHome ? game.AwayScore : game.HomeScore);
            } else if ("B".equals(game.Type)) {
                h.scoreHome.setText("");
                h.scoreAway.setText("");
            } else {
                h.scoreHome.setText("-");
                h.scoreAway.setText("-");
            }

            String header = game.ScheduleHeader != null ? game.ScheduleHeader : "--";
            String timeOrStatus;

            if ("F".equals(game.Type)) {
                timeOrStatus = "Final";
            } else if ("B".equals(game.Type)) {
                timeOrStatus = "BYE WEEK";
            } else {
                timeOrStatus = formatTime(game.Date != null ? game.Date.Timestamp : null);
            }

            String fullLabel = header;

            if (timeOrStatus != null && !timeOrStatus.equals("--")) {
                fullLabel += " • " + timeOrStatus;
            }

            h.matchInfo.setText(fullLabel);

            if ("B".equals(game.Type)) {
                h.homeName.setVisibility(View.GONE);
                h.awayName.setVisibility(View.GONE);
                h.homeLogo.setVisibility(View.GONE);
                h.awayLogo.setVisibility(View.GONE);
                h.scoreHome.setVisibility(View.GONE);
                h.scoreAway.setVisibility(View.GONE);
                h.scoreSeparator.setVisibility(View.GONE);
                h.matchInfo.setText("BYE WEEK");
                h.matchInfo.setTextSize(40);
                h.matchInfo.setTextColor(context.getResources().getColor(R.color.textSecondary));

                h.actionButtons.setVisibility(View.GONE);
            } else {
                h.homeName.setVisibility(View.VISIBLE);
                h.awayName.setVisibility(View.VISIBLE);
                h.homeLogo.setVisibility(View.VISIBLE);
                h.awayLogo.setVisibility(View.VISIBLE);
                h.scoreHome.setVisibility(View.VISIBLE);
                h.scoreAway.setVisibility(View.VISIBLE);
                h.scoreSeparator.setVisibility(View.VISIBLE);
                h.matchInfo.setTextSize(14);
                h.matchInfo.setTextColor(context.getResources().getColor(R.color.textSecondary));
            }


            h.actionButtons.setVisibility(View.GONE);
            h.gameCenterBtn.setVisibility(View.GONE);
            h.highlightsBtn.setVisibility(View.GONE);

            if (game.Buttons != null && !game.Buttons.isEmpty()) {
                h.actionButtons.setVisibility(View.VISIBLE);

                for (ScheduleResponse.Game.Button btn : game.Buttons) {
                    if ("GAME CENTER".equalsIgnoreCase(btn.Title)) {
                        h.gameCenterBtn.setVisibility(View.VISIBLE);
                        h.gameCenterBtn.setOnClickListener(v -> {
                            if (btn.URL.startsWith("http")) {
                                Intent intent = new Intent(context, WebViewActivity.class);
                                intent.putExtra("url", btn.URL);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "This link is a deep link: " +
                                        btn.URL, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    if ("HIGHLIGHTS".equalsIgnoreCase(btn.Title)) {
                        h.highlightsBtn.setVisibility(View.VISIBLE);
                        h.highlightsBtn.setOnClickListener(v -> {
                            if (btn.URL.startsWith("http")) {
                                Intent intent = new Intent(context, WebViewActivity.class);
                                intent.putExtra("url", btn.URL);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "This link is a deep link: " +
                                        btn.URL, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }
    }



    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    private String getLogoUrl(String triCode) {
        return "https://s3.amazonaws.com/yc-app-resources/nfl/logos/nfl_" + triCode + "_light.png";
    }

    private String formatTime(String isoTime) {
        if (isoTime == null) return "--";
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.getDefault());
            parser.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = parser.parse(isoTime);

            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
            formatter.setTimeZone(TimeZone.getDefault());

            return formatter.format(date);
        } catch (Exception e) {
            return "--";
        }
    }

}
