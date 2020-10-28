package com.example.myappstatistics.Model;

import android.graphics.drawable.Drawable;


//Item in recyclerView of ListFragment

public class AppItem {


    public String appName;
    public Drawable appIcon;
    public long startTime;
    public long duration;

    public AppItem(String name, Drawable icon , long startTime, long duration){
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


}
