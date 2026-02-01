package com.example.cheffy.ui.onboarding.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheffy.R;
import com.example.cheffy.ui.onboarding.model.OnboardingItem;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {
    private final List<OnboardingItem> items;

    public OnboardingAdapter(List<OnboardingItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OnboardingAdapter.OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingAdapter.OnboardingViewHolder holder, int position) {
        OnboardingItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView tvTitle1, tvTitle2, tvDescription;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivFoodImage);
            tvTitle1 = itemView.findViewById(R.id.tvTitle1);
            tvTitle2 = itemView.findViewById(R.id.tvTitle2);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(OnboardingItem item) {
            imageView.setImageResource(item.getImageRes());
            tvTitle1.setText(item.getTitle1());
            tvTitle2.setText(item.getTitle2());
            tvDescription.setText(item.getDescription());
        }
    }
}
