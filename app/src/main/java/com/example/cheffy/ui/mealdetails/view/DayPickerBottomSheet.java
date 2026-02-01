package com.example.cheffy.ui.mealdetails.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheffy.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;


public class DayPickerBottomSheet extends BottomSheetDialogFragment {

    public interface OnDaySelectedListener {
        void onDaySelected(String dayOfWeek);
    }

    private OnDaySelectedListener listener;

    private static final String[] DAYS_FULL = {
            "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
    };

    public static DayPickerBottomSheet newInstance() {
        return new DayPickerBottomSheet();
    }

    public void setOnDaySelectedListener(OnDaySelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_day_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvDays = view.findViewById(R.id.rvDays);
        MaterialButton btnCancel = view.findViewById(R.id.btnCancel);

        rvDays.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvDays.setAdapter(new DayAdapter());

        btnCancel.setOnClickListener(v -> dismiss());
    }

    private class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

        @NonNull
        @Override
        public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_day_picker, parent, false);
            return new DayViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
            holder.bind(DAYS_FULL[position]);
        }

        @Override
        public int getItemCount() {
            return DAYS_FULL.length;
        }

        class DayViewHolder extends RecyclerView.ViewHolder {
            private final TextView tvDayFull;

            DayViewHolder(@NonNull View itemView) {
                super(itemView);
                tvDayFull = itemView.findViewById(R.id.tvDayFull);
            }

            void bind(String dayFull) {
                tvDayFull.setText(dayFull);

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onDaySelected(dayFull);
                    }
                    dismiss();
                });
            }
        }
    }
}
