package com.example.myappstatistics.Model;

import android.graphics.drawable.Drawable;

public class AppItem {


    public String appName;
    public Drawable appIcon;
    public String startTime;
    public String duration;

    public AppItem(String name, Drawable icon , String startTime, String duration){
        this.appName = name;
        this.appIcon = icon;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


}
