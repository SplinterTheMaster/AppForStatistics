package com.example.myappstatistics.Model;

import org.litepal.crud.LitePalSupport;

public class AppUsage extends LitePalSupport {

    private String appName;
    private long startTime;
    private int duration;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
