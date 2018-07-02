package com.axovel.mytapzoapp.models;

/**
 * Created by axovel on 8/21/2017.
 */

public class VendarDataList  {

    public VendarDataList(int appImage, String appName) {
        this.appImage = appImage;
        this.appName = appName;
    }

    public int getAppImage() {
        return appImage;
    }

    public void setAppImage(int appImage) {
        this.appImage = appImage;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    private int appImage;
    private String appName;
}
