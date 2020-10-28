package com.example.myappstatistics.Fragment;


import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myappstatistics.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */

// show the official usage of usagestats

public class AppUsageFragment extends Fragment {

    private static final String TAG = "AppUsageFragment";
    private static final String DESKTOP = "com.huawei.android.launcher";
    private static final String SYSTEM = "com.android";
    private View thisView;
    private String testApp = "com.tencent.mm";
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        thisView = view;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -3);
        long startTime = cal.getTimeInMillis();
        long endTime = System.currentTimeMillis();
        printEventList(thisView.getContext(), startTime,endTime);
        return view;
    }
    private void printEventList(Context context, long beginTime, long endTime){
        UsageEvents.Event currentEvent;
        UsageStatsManager usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()){
            currentEvent = new UsageEvents.Event();
            usageEvents.getNextEvent(currentEvent);
            String currName = currentEvent.getPackageName();
            if (currName.equals(DESKTOP))
                continue;
            if (currName.length()> 10 && currName.substring(0,11).equals(SYSTEM))
                continue;
            if (currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED )
                Log.i(TAG, currentEvent.getPackageName() + "\t"+ "Resumed" + dateFormat.format(new Date(currentEvent.getTimeStamp())));
            if (currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED)
                Log.i(TAG, currentEvent.getPackageName() + "\t" + "Paused" +dateFormat.format(new Date(currentEvent.getTimeStamp())));
        }
    }

}
/*
*  printUsageList(thisView.getContext(), startTime, endTime);
        HashMap here = getTimeSpent(thisView.getContext(),testApp,
                cal.getTimeInMillis(),System.currentTimeMillis() );
        for (Long i : getStart(thisView.getContext(),testApp,startTime,endTime)){
            Log.i(TAG,dateFormat.format(new Date(i)));
        }
        IdentityHashMap res = getAllStart(thisView.getContext(),startTime,endTime);
        Log.i(TAG, "MapSize: " + res.entrySet().size());
        for (Object i : res.entrySet()){
            Log.i(TAG, i.toString());
        }
* */
/*HashMap<String, Integer> getTimeSpent(Context context, String packageName, long beginTime, long endTime) {
        UsageEvents.Event currentEvent;
        List<UsageEvents.Event> allEvents = new ArrayList<>();
        HashMap<String, Integer> appUsageMap = new HashMap<>();

        UsageStatsManager usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);

        while (usageEvents.hasNextEvent()) {
            currentEvent = new UsageEvents.Event();
            usageEvents.getNextEvent(currentEvent);
            if(currentEvent.getPackageName().equals(packageName) || packageName == null) {
                if (currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED
                        || currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED) {
                    allEvents.add(currentEvent);
                    String key = currentEvent.getPackageName();
                    if (appUsageMap.get(key) == null)
                        appUsageMap.put(key, 0);
                }
            }
        }
        for (int i = 0; i < allEvents.size() - 1; i++) {
            UsageEvents.Event E0 = allEvents.get(i);
            UsageEvents.Event E1 = allEvents.get(i + 1);

            if (E0.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED
                    && E1.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED
                    && E0.getClassName().equals(E1.getClassName())) {
                int diff = (int)(E1.getTimeStamp() - E0.getTimeStamp());
                diff /= 1000;
                Integer prev = appUsageMap.get(E0.getPackageName());
                if(prev == null) prev = 0;
                appUsageMap.put(E0.getPackageName(), prev + diff);
            }
        }
        UsageEvents.Event lastEvent = allEvents.get(allEvents.size() - 1);
        if(lastEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
            int diff = (int)System.currentTimeMillis() - (int)lastEvent.getTimeStamp();
            diff /= 1000;
            Integer prev = appUsageMap.get(lastEvent.getPackageName());
            if(prev == null) prev = 0;
            appUsageMap.put(lastEvent.getPackageName(), prev + diff);
        }
        return appUsageMap;
    }
    private List<Long> getStart(Context context, String packageName, long beginTime, long endTime){
        UsageEvents.Event currentEvent;
        List<UsageEvents.Event> allEvents = new ArrayList<>();
        List<Long> results = new ArrayList<>();

        UsageStatsManager usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()){
            currentEvent = new UsageEvents.Event();
            usageEvents.getNextEvent(currentEvent);
            if (currentEvent.getPackageName().equals(packageName) &&
                    currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED){
                results.add(currentEvent.getTimeStamp());
            }
        }
        return results;
    }
    private IdentityHashMap<String,String> getAllStart(Context context, long beginTime, long endTime){
        UsageEvents.Event currentEvent;
        List<UsageEvents.Event> allEvents = new ArrayList<>();
        IdentityHashMap<String,String> results = new IdentityHashMap<>();

        UsageStatsManager usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()){
            currentEvent = new UsageEvents.Event();
            usageEvents.getNextEvent(currentEvent);
            if (currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED){
                results.put(new String(currentEvent.getPackageName()),
                        dateFormat.format(new Date(currentEvent.getTimeStamp())));
            }
        }
        //results.put(new String("test") , "00");
        //results.put(new String("test") , "01");
        return results;
    }

    private void printEventList(Context context, long beginTime, long endTime){
        UsageEvents.Event currentEvent;
        UsageStatsManager usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()){
            currentEvent = new UsageEvents.Event();
            usageEvents.getNextEvent(currentEvent);
            if (currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED)
                Log.i(TAG, currentEvent.getPackageName() + "\t" + dateFormat.format(new Date(currentEvent.getTimeStamp())));
        }
    }
    private void printUsageList(Context context, long beginTime, long endTime){
        UsageStats currentEvent;
        List<UsageStats> usageStatsList;
        UsageStatsManager usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,beginTime,endTime);
        for (UsageStats usageStats:usageStatsList){
            //String stime = dateFormat.format(new Date(usageStats.getFirstTimeStamp()));
            long ntime = usageStats.getFirstTimeStamp();
            String time = dateFormat.format(new Date(ntime));
            if (usageStats.getPackageName().equals("com.tencent.mm"))
                Log.i(TAG, usageStats.getPackageName() + "\t" + ntime +"\t"+ time );
        }
    }*/