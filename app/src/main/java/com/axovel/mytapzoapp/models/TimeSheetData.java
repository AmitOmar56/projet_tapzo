package com.axovel.mytapzoapp.models;

/**
 * Created by axovel on 9/8/2017.
 */

public class TimeSheetData {

    int timehour;
    int timeminute;
    int timeSession;

    public TimeSheetData(int timehour,int timeminute , int timeSession) {
        this.timehour = timehour;
        this.timeminute=timeminute;
        this.timeSession = timeSession;
    }

    public int getTimehour() {
        return timehour;
    }

    public void setTimehour(int timehour) {
        this.timehour = timehour;
    }

    public int getTimeminute() {
        return timeminute;
    }

    public void setTimeminute(int timeminute) {
        this.timeminute = timeminute;
    }

    public int getTimeSession() {
        return timeSession;
    }

    public void setTimeSession(int timeSession) {
        this.timeSession = timeSession;
    }



    }

