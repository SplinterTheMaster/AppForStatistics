package com.example.myappstatistics;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.myappstatistics.Activity.MainActivity;
import com.example.myappstatistics.Model.AppItem;
import com.example.myappstatistics.Model.AppUsage;

import org.litepal.LitePal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

// implemented by usagestats

public class CollectService extends Service {
    public static final String TAG = "TestService";
    public static final long INTERVAL =  10 * 60 * 1000;//8* 60 * 60 *1000
    private static final String DESKTOP = "com.huawei.android.launcher";
    private static final String SYSTEM = "com.android.packageinstaller";
    private static final String THIS = "com.example.myappstatistics";
    private static final long ERR = 1000;
//    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private MyThread myThread = null;
    private static class MyThread extends Thread {
        private Context context;
        private boolean isRun = true;
        private String currAppName = null;
        private String prevAppName = null;
        private Queue<UsageEvents.Event> mQueue = new LinkedList<>();
        private MyThread(Context context) {
            this.context = context;
        }
        public void setStop() {
            isRun = false;
        }
        @Override
        public void run() {
            //List<AppUsage> data = LitePal.findAll(AppUsage.class);
            while (isRun) {
                long endTime = System.currentTimeMillis();
                long startTime = endTime - INTERVAL;
                getEventList(context, startTime, endTime);
                if (mQueue.size() > 1 && mQueue.peek().getEventType() == UsageEvents.Event.ACTIVITY_PAUSED){
                    Log.i(TAG, "run: doooooooooooooooooooooooooooooooooooo");
                    mQueue.poll();
                }
                if (mQueue.size() >= 2){
                    upDateDataBase();
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        private void getEventList(Context context, long beginTime, long endTime){
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
            UsageEvents.Event currentEvent;
            UsageStatsManager usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
            UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()){
                currentEvent = new UsageEvents.Event();
                usageEvents.getNextEvent(currentEvent);
                String currName = currentEvent.getPackageName();
                int currType = currentEvent.getEventType();
                if (currName.equals(DESKTOP)){continue;}
                if (currName.equals(SYSTEM)){continue;}
                //if (currName.equals(THIS)){continue;}
                //if (currName.length()> 10 && currName.substring(0,11).equals(SYSTEM)){continue;}
                if (currType == UsageEvents.Event.ACTIVITY_RESUMED || currType == UsageEvents.Event.ACTIVITY_PAUSED){
                    mQueue.offer(currentEvent);
                    Log.i(TAG, "getList: "+" current: "+currName +" type: "+
                            currType +"time :" +dateFormat.format(new Date(currentEvent.getTimeStamp())) );
                }
            }
        }
        private void upDateDataBase(){
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
            UsageEvents.Event startEvent = null, endEvent = null;
            long startTime = mQueue.peek().getTimeStamp();
            String startName = mQueue.peek().getPackageName();
            while(mQueue.size() >= 2){
                startEvent = mQueue.poll();
                if (startEvent.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED){
                    continue;
                }
                endEvent = mQueue.poll();
                //Log.i(TAG, "upDateDataBase: " + endEvent.getPackageName()  );
                if (mQueue.isEmpty()){
                    AppUsage appUsage = new AppUsage();
                    appUsage.setAppName(startName);
                    appUsage.setStartTime(startTime);
                    appUsage.setDuration(endEvent.getTimeStamp() - startTime);
                    //Log.i(TAG, "TOP"+ dateFormat.format(new Date(startTime)));
                    appUsage.save();
                    return;
                }else if (!mQueue.peek().getPackageName().equals(startName)) {
                    AppUsage appUsage = new AppUsage();
                    appUsage.setAppName(startName);
                    appUsage.setStartTime(startTime);
                    appUsage.setDuration(endEvent.getTimeStamp() - startTime);
                    //Log.i(TAG, "MID "+ dateFormat.format(new Date(startTime)));
                    appUsage.save();
                    startName = mQueue.peek().getPackageName();
                    startTime = mQueue.peek().getTimeStamp();

                }else if ( mQueue.peek().getTimeStamp() - endEvent.getTimeStamp()  > 1000
                        && mQueue.peek().getPackageName().equals(startName)){
                    AppUsage appUsage = new AppUsage();
                    appUsage.setAppName(startName);
                    appUsage.setStartTime(startTime);
                    appUsage.setDuration(endEvent.getTimeStamp() - startTime);
                    //Log.i(TAG, "DOWN "+ dateFormat.format(new Date(startTime)));
                    appUsage.save();
                    startName = mQueue.peek().getPackageName();
                    startTime = mQueue.peek().getTimeStamp();
                }
            }
                /*else if (mQueue.peek().getPackageName().equals(startName) &&
                        mQueue.peek().getTimeStamp() <= endEvent.getTimeStamp() + ERR){

                    }*/

            }

        /*
        }*/
    }//Thread
    /*new SaveThread(startName, startTime,
                            endEvent.getTimeStamp() - startTime).start();*/
    /*private static class SaveThread extends Thread{
        private String appName;
        private long startTime;
        private long duration;
        public SaveThread(String appName, long startTime, long duration) {
            this.appName = appName;
            this.startTime = startTime;
            this.duration = duration;
        }

        @Override
        public void run() {
            AppUsage appUsage = new AppUsage();
            appUsage.setAppName(appName);
            appUsage.setStartTime(startTime);
            appUsage.setDuration(duration);
            appUsage.save();
            Log.i(TAG, "afterTime : "+  startTime);
        }
    }*/
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
        setForeground();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myThread.setStop();
        Log.i(TAG, "Service is stop.");
    }
    private void setForeground(){
        NotificationManager manager=(NotificationManager)getSystemService (NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel ("channel_1","信息收集服务",NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel (channel);
        Notification notification=new Notification.Builder (this,"channel_1")
                .setContentTitle ("信息收集")
                .setContentText ("正在进行信息收集")
                .setSmallIcon (R.mipmap.ic_launcher)
                .setLargeIcon (BitmapFactory.decodeResource (getResources (),R.mipmap.ic_launcher))
                .build ();
        startForeground (1,notification);
    }
}
/*        private void upDateDataBase(List<UsageEvents.Event> usageEvents){
            int currEvent = 0;
            int cursor = currEvent;
            long startTime = usageEvents.get(currEvent).getTimeStamp();
            long duration = 0;
            while (currEvent < usageEvents.size() - 1){
                if (usageEvents.get(currEvent).getPackageName().
                        equals(usageEvents.get(cursor).getPackageName())){
                    cursor ++;
                }else {
                    AppUsage appUsage = new AppUsage();
                    appUsage.setAppName(usageEvents.get(currEvent).getPackageName());
                    duration = usageEvents.get(cursor - 1).getTimeStamp() - startTime;
                    appUsage.setDuration(duration);
                    appUsage.setStartTime(startTime);
                    appUsage.save();
                    //if (duration!=0) {}
                    Log.i(TAG, "upDateDateBase:\tstartTime:  "+ startTime + "\tduration: "+duration+"\tName "+
                            usageEvents.get(currEvent).getPackageName() );
                    currEvent = cursor + 1;
                    startTime = usageEvents.get(currEvent).getTimeStamp();
                }
            }
        }*/
/*        private String getTopApp(Context context) {
            UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            String topActivity = "";
            if (m != null) {
                long now = System.currentTimeMillis();
                //获取60秒之内的应用数据
                List<UsageStats> stats =  m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now -  60 * 1000, now);
                Log.i(TAG, "Running app number in last 2 seconds : " + stats.size());

                //取得最近运行的一个app，即当前运行的app
                if (!stats.isEmpty()) {
                    int j = 0;
                    for (int i = 0; i < stats.size(); i++) {
                        if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()
                                && !stats.get(i).getPackageName().equals("com.huawei.android.launcher")) {
                            j = i;
                        }
                    }
                    topActivity = stats.get(j).getPackageName();
                }
                Log.i(TAG, "top running app is : "+topActivity);
            }
            return topActivity;
        }*/
/*private List<UsageEvents.Event> getEventList(Context context, long beginTime, long endTime){
            //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            List<UsageEvents.Event> activityEvents = new ArrayList<>();
            UsageEvents.Event currentEvent;
            UsageStatsManager usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
            UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()){
                currentEvent = new UsageEvents.Event();
                usageEvents.getNextEvent(currentEvent);
                String currName = currentEvent.getPackageName();
                int currType = currentEvent.getEventType();
                if (currName.equals(DESKTOP)){continue;}
                if (currName.equals(SYSTEM)){continue;}
                //if (currName.length()> 10 && currName.substring(0,11).equals(SYSTEM)){continue;}
                if (currType == UsageEvents.Event.ACTIVITY_RESUMED || currType == UsageEvents.Event.ACTIVITY_PAUSED){
                    activityEvents.add(currentEvent);
                    Log.i(TAG, "getEventList: "+activityEvents.size());
                }
            }
            return activityEvents;
        }

        private void upDateDataBase(List<UsageEvents.Event> usageEvents){
            if (usageEvents.get(0).getEventType() == UsageEvents.Event.ACTIVITY_RESUMED){
                int current = 0;
                int start = 0;
                long startTime = System.currentTimeMillis();
                long duration = 0;
                while (current < usageEvents.size() - 1){
                    duration = usageEvents.get(current+1).getTimeStamp() - usageEvents.get(current).getTimeStamp();
                    startTime = usageEvents.get(current).getTimeStamp();
                    start = current + 2;
                    while (usageEvents.get(start).getPackageName().
                            equals(usageEvents.get(current).getPackageName())
                    &&usageEvents.get(start-1).getTimeStamp()+ERR>usageEvents.get(start).getTimeStamp()
                    && start < usageEvents.size() - 1){
                        duration = duration + usageEvents.get(start+1).getTimeStamp() - usageEvents.get(start).getTimeStamp();
                        start = start + 2;
                    }
                    AppUsage appUsage = new AppUsage();
                    appUsage.setAppName(usageEvents.get(current).getPackageName());
                    appUsage.setStartTime(startTime);
                    appUsage.setDuration(duration);
                    appUsage.save();
                    Log.i(TAG, "upDateDataBase: " + usageEvents.get(current).getPackageName());
                    current = current + 2;
                }
            }
        }*/
/* AppUsage appUsage = new AppUsage();
                    appUsage.setAppName(currName);
                    appUsage.setStartTime(currentEvent.getTimeStamp());
                    if (currType == UsageEvents.Event.ACTIVITY_RESUMED)
                        appUsage.setDuration(0);
                    else appUsage.setDuration(1);
                    appUsage.save();*/