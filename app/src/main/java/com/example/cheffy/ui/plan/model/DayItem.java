package com.example.cheffy.ui.plan.model;

public class DayItem {
    private final String dayName;
    private final String dayShortName;
    private final int dayNumber;
    private boolean isSelected;

    public DayItem(String dayName, String dayShortName, int dayNumber) {
        this.dayName = dayName;
        this.dayShortName = dayShortName;
        this.dayNumber = dayNumber;
        this.isSelected = false;
    }

    public String getDayName() {
        return dayName;
    }

    public String getDayShortName() {
        return dayShortName;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
