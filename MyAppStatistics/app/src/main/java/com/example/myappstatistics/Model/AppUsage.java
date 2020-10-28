package com.example.myappstatistics.Model;

import org.litepal.crud.LitePalSupport;

//table (appName, startTime, duration) in LitePal

public class AppUsage extends LitePalSupport {

    private String appName;
    private long startTime;
    private long duration;

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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
