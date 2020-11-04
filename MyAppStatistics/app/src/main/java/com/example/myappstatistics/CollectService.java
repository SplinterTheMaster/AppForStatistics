package com.example.myappstatistics;

import android.app.AlarmManager;
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
import android.os.SystemClock;
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
    public static final long INTERVAL =  60 * 60 * 1000;//8* 60 * 60 *1000
    private static final String DESKTOP = "com.huawei.android.launcher";
    private static final String SYSTEM = "com.android.packageinstaller";
    private static final String THIS = "com.example.myappstatistics";
    private static final long ERR = 10000;
    private Queue<UsageEvents.Event> mQueue = new LinkedList<>();
//    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service is start.");
        setForeground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "executed at " + new Date().toString());
                long endTime = System.currentTimeMillis();
                long startTime = endTime - INTERVAL - ERR;
                getEventList(CollectService.this, startTime, endTime);
                if (mQueue.size() > 1 && mQueue.peek().getEventType() == UsageEvents.Event.ACTIVITY_PAUSED){
                    mQueue.poll();
                }
                if (mQueue.size() >= 2){
                    upDateDataBase();
                }
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //long anHour = INTERVAL; // 这是十分钟的毫秒数
        long triggerAtTime = System.currentTimeMillis() + INTERVAL;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        Log.i(TAG, "sendBroadcast " );
        manager.setExact(AlarmManager.RTC_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }


    public void onDestroy() {
        super.onDestroy();
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
                saveToDataBase(startName, startTime, endEvent.getTimeStamp() - startTime);
                return;
            }else if (!mQueue.peek().getPackageName().equals(startName)) {
                saveToDataBase(startName, startTime, endEvent.getTimeStamp() - startTime);
                startName = mQueue.peek().getPackageName();
                startTime = mQueue.peek().getTimeStamp();

            }else if ( mQueue.peek().getTimeStamp() - endEvent.getTimeStamp()  > 1000
                    && mQueue.peek().getPackageName().equals(startName)){
                saveToDataBase(startName, startTime, endEvent.getTimeStamp() - startTime);
                startName = mQueue.peek().getPackageName();
                startTime = mQueue.peek().getTimeStamp();
            }
        }
    }

    private void saveToDataBase(String name, long start, long duration){
        if (LitePal.count(AppUsage.class) >0 &&
                LitePal.findLast(AppUsage.class).getStartTime() >= start
                || duration <= 0){
            return;
        }
        AppUsage appUsage = new AppUsage();
        appUsage.setAppName(name);
        appUsage.setStartTime(start);
        appUsage.setDuration(duration);
        appUsage.save();
        Log.i(TAG, "SAVED");
    }

}
