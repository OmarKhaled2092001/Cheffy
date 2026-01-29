package com.example.cheffy.ui.mealdetails.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheffy.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MealFooterAdapter extends RecyclerView.Adapter<MealFooterAdapter.FooterViewHolder> {

    private String instructions;
    private String youtubeUrl;
    private boolean showInstructions = false;
    private LifecycleOwner lifecycleOwner;
    private boolean hasData = false;

    public void setData(String instructions, String youtubeUrl) {
        this.instructions = instructions;
        this.youtubeUrl = youtubeUrl;
        this.hasData = (instructions != null || youtubeUrl != null);
        notifyDataSetChanged();
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setInstructionsVisible(boolean visible) {
        if (this.showInstructions != visible) {
            this.showInstructions = visible;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public FooterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_footer, parent, false);
        return new FooterViewHolder(view, lifecycleOwner);
    }

    @Override
    public void onBindViewHolder(@NonNull FooterViewHolder holder, int position) {
        holder.bind(instructions, youtubeUrl, showInstructions);
    }

    @Override
    public int getItemCount() {
        return hasData ? 1 : 0;
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvInstructions;
        private final CardView cardVideo;
        private final YouTubePlayerView youtubePlayerView;
        private boolean isYouTubeInitialized = false;

        public FooterViewHolder(@NonNull View itemView, LifecycleOwner lifecycleOwner) {
            super(itemView);

            tvInstructions = itemView.findViewById(R.id.tvInstructions);
            cardVideo = itemView.findViewById(R.id.cardVideo);
            youtubePlayerView = itemView.findViewById(R.id.youtubePlayerView);

            if (lifecycleOwner != null) {
                lifecycleOwner.getLifecycle().addObserver(youtubePlayerView);
            }
        }

        public void bind(String instructions, String youtubeUrl, boolean showInstructions) {
            if (showInstructions && instructions != null) {
                tvInstructions.setText(formatInstructions(instructions));
                tvInstructions.setVisibility(View.VISIBLE);
            } else {
                tvInstructions.setVisibility(View.GONE);
            }

            String videoId = extractYoutubeVideoId(youtubeUrl);
            if (videoId != null && !videoId.isEmpty()) {
                cardVideo.setVisibility(View.VISIBLE);
                if (!isYouTubeInitialized) {
                    isYouTubeInitialized = true;
                    youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            youTubePlayer.cueVideo(videoId, 0);
                        }
                    });
                }
            } else {
                cardVideo.setVisibility(View.GONE);
            }
        }

        private String formatInstructions(String instructions) {
            if (instructions == null || instructions.isEmpty()) {
                return "No instructions available.";
            }

            String cleaned = normalizeText(instructions);

            String[] steps = getStepsFromText(cleaned);

            return buildBulletedList(steps);
        }

        private String normalizeText(String text) {
            return text.replace("\r\n", "\n")
                    .replace("\r", "\n");
        }

        private String[] getStepsFromText(String text) {
            if (text.split("\n").length > 1) {
                return text.split("\n");
            } else {
                return text.split("(?<=\\.)\\s+(?=[A-Z])");
            }
        }

        private String buildBulletedList(String[] steps) {
            StringBuilder formatted = new StringBuilder();

            for (String step : steps) {
                String trimmed = step.trim();

                if (isValidStep(trimmed)) {
                    String finalStep = removeLeadingNumber(trimmed);

                    formatted.append("• ")
                            .append(finalStep)
                            .append("\n\n");
                }
            }
            return formatted.toString().trim();
        }

        private String removeLeadingNumber(String step) {
            return step.replaceAll("^\\d+\\.\\s*", "");
        }

        private boolean isValidStep(String step) {
            return !step.isEmpty()
                    && !step.matches("^\\d+\\.?$")
                    && !step.equalsIgnoreCase("step");
        }



        private String extractYoutubeVideoId(String youtubeUrl) {
            if (youtubeUrl == null || youtubeUrl.isEmpty()) return null;

            String pattern = "(?:youtube\\.com/watch\\?v=|youtu\\.be/)([a-zA-Z0-9_-]{11})";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(youtubeUrl);

            if (matcher.find()) {
                return matcher.group(1);
            }
            return null;
        }
    }
}
