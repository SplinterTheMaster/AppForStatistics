package com.example.myappstatistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TAG = "TestService";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startCollectServiceIntent = new Intent(context, CollectService.class);
        Log.i(TAG, "onReceive: ");
        context.startService(startCollectServiceIntent);
    }
}
