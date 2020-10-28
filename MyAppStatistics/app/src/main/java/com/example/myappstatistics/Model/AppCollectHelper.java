package com.example.myappstatistics.Model;

public class AppCollectHelper {
    public String appName;
    public long firstTime;
    public long lastTime;

    public AppCollectHelper(String appName, long firstTime, long lastTime) {
        this.appName = appName;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
