package com.example.myappstatistics;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import com.example.myappstatistics.Model.AppUsage;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CollectService extends Service {
    public static final String TAG = "TestService";
    public static final long INTERVAL = 2*1000;
    private MyThread myThread = null;
    private static class MyThread extends Thread {
        private Context context;
        private boolean isRun = true;
        private String currAppName = null;
        private String prevAppName = null;
        private MyThread(Context context) {
            this.context = context;
        }
        public void setStop() {
            isRun = false;
        }
        @Override
        public void run() {
            long dur = INTERVAL;
            while (isRun) {
                try {
                    prevAppName = currAppName;
                    TimeUnit.MILLISECONDS.sleep(INTERVAL);
                    currAppName = getTopApp(context);
                    if (prevAppName != null){
                        if (prevAppName.equals(currAppName)){
                            dur += INTERVAL;
                        }else {
                            AppUsage appUsage = new AppUsage();
                            appUsage.setAppName(currAppName);
                            appUsage.setStartTime(System.currentTimeMillis());
                            appUsage.setDuration(dur);
                            appUsage.save();
                            dur = INTERVAL;
                        }
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        private String getTopApp(Context context) {
                UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                String topActivity = "";
                if (m != null) {
                    long now = System.currentTimeMillis();
                    //获取60秒之内的应用数据
                    List<UsageStats> stats =  m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 8 * 60 * 60 * 1000, now);
                    Log.i(TAG, "Running app number in last 2 seconds : " + stats.size());


                    //取得最近运行的一个app，即当前运行的app
                    if (!stats.isEmpty()) {
                        int j = 0;
                        for (int i = 0; i < stats.size(); i++) {
                            if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
                                j = i;
                            }
                        }
                        topActivity = stats.get(j).getPackageName();
                    }
                    Log.i(TAG, "top running app is : "+topActivity);
                }
                return topActivity;
            }
    }

    //@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myThread = new MyThread(this);
        myThread.start();
        Log.i(TAG, "Service is start.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myThread.setStop();
        Log.i(TAG, "Service is stop.");
    }
}
