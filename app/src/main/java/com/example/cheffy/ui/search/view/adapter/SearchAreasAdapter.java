package com.example.cheffy.ui.search.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;
import com.example.cheffy.data.meals.models.remote.Area;

import java.util.ArrayList;
import java.util.List;

public class SearchAreasAdapter extends RecyclerView.Adapter<SearchAreasAdapter.AreaViewHolder> {

    private List<Area> areas = new ArrayList<>();
    private final OnAreaClickListener listener;

    public SearchAreasAdapter(OnAreaClickListener listener) {
        this.listener = listener;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_circle_grid, parent, false);
        return new AreaViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areas.get(position);
        holder.bind(area);
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public static class AreaViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgItem;
        private final TextView tvItem;
        private Area currentArea;

        AreaViewHolder(@NonNull View itemView, OnAreaClickListener listener) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            tvItem = itemView.findViewById(R.id.tvItem);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null && currentArea != null) {
                    listener.onAreaClick(currentArea);
                }
            });
        }

        void bind(Area area) {
            this.currentArea = area;
            tvItem.setText(area.getName());

            Glide.with(itemView.getContext())
                    .load(area.getFlagUrl())
                    .placeholder(R.drawable.welcome_background)
                    .error(R.drawable.welcome_background)
                    .centerCrop()
                    .into(imgItem);
        }
    }
}
