package com.axovel.mytapzoapp.models;

/**
 * Created by Soft1 on 07-Sep-17.
 */

public class Booking {

    private String title;
    private String day;
    private String date;
    private long timeMiles = 0;
    private boolean isSelected = false;
    private String fulldate;

    public String getFulldate() {
        return fulldate;
    }

    public void setFulldate(String fulldate) {
        this.fulldate = fulldate;
    }

    public long getTimeMiles() {
        return timeMiles;
    }

    public void setTimeMiles(long timeMiles) {
        this.timeMiles = timeMiles;
    }

    public Booking(String day, String date) {
        this.day = day;
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
