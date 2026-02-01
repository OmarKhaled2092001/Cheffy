package com.example.cheffy.ui.plan.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheffy.R;
import com.example.cheffy.ui.plan.model.DayItem;

import java.util.ArrayList;
import java.util.List;

public class CalendarDayAdapter extends RecyclerView.Adapter<CalendarDayAdapter.DayViewHolder> {

    private List<DayItem> days = new ArrayList<>();
    private final OnDayClickListener listener;
    private int selectedPosition = 0;

    private interface InternalDayClickListener {
        void onDayClick(int position, DayItem day);
    }

    public CalendarDayAdapter(OnDayClickListener listener) {
        this.listener = listener;
    }

    public void setDays(List<DayItem> days) {
        this.days = days != null ? days : new ArrayList<>();
        for (int i = 0; i < this.days.size(); i++) {
            if (this.days.get(i).isSelected()) {
                selectedPosition = i;
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void setSelectedDay(String dayOfWeek) {
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).getDayName().equals(dayOfWeek)) {
                int oldPosition = selectedPosition;
                selectedPosition = i;
                notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);
                break;
            }
        }
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_day, parent, false);

        return new DayViewHolder(view, (position, day) -> {
            int oldPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onDayClicked(day.getDayName());
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        DayItem day = days.get(position);
        boolean isSelected = (position == selectedPosition);
        holder.bind(day, isSelected);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardDay;
        private final TextView tvDayName;
        private final TextView tvDayNumber;

        private DayItem currentDay;

        DayViewHolder(@NonNull View itemView, InternalDayClickListener internalListener) {
            super(itemView);
            cardDay = itemView.findViewById(R.id.cardDay);
            tvDayName = itemView.findViewById(R.id.tvDayName);
            tvDayNumber = itemView.findViewById(R.id.tvDayNumber);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && internalListener != null && currentDay != null) {
                    internalListener.onDayClick(position, currentDay);
                }
            });
        }

        void bind(DayItem day, boolean isSelected) {
            this.currentDay = day;

            tvDayName.setText(day.getDayShortName());
            tvDayNumber.setText(String.valueOf(day.getDayNumber()));

            if (isSelected) {
                cardDay.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.primary_color));
                tvDayName.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                tvDayNumber.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            } else {
                cardDay.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                tvDayName.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_gray));
                tvDayNumber.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
            }
        }
    }
}