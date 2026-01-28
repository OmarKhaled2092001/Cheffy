package com.example.cheffy.ui.home.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheffy.R;
import com.example.cheffy.data.meals.models.Area;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class CuisinesAdapter extends RecyclerView.Adapter<CuisinesAdapter.CuisineViewHolder> {

    private List<Area> areas = new ArrayList<>();
    private final OnCuisineClickListener listener;


    public CuisinesAdapter(OnCuisineClickListener listener) {
        this.listener = listener;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CuisineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_circle_card, parent, false);
        return new CuisineViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CuisineViewHolder holder, int position) {
        Area area = areas.get(position);
        holder.bind(area);
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public static class CuisineViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView imgCountry;
        private final TextView tvCountryName;
        private Area currentArea;

        CuisineViewHolder(@NonNull View itemView, OnCuisineClickListener listener) {
            super(itemView);
            imgCountry = itemView.findViewById(R.id.imgItem);
            tvCountryName = itemView.findViewById(R.id.tvItem);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null && currentArea != null) {
                    listener.onCuisineClick(currentArea);
                }
            });
        }

        void bind(Area area) {
            this.currentArea = area;

            tvCountryName.setText(area.getName());
            Glide.with(itemView.getContext())
                    .load(area.getFlagUrl())
                    .centerCrop()
                    .placeholder(R.drawable.welcome_background)
                    .into(imgCountry);
        }
    }
}
