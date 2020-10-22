package com.example.myappstatistics.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.myappstatistics.Model.AppUsage;
import com.example.myappstatistics.R;
import org.litepal.LitePal;

import java.util.List;


public class ShowActivity extends AppCompatActivity {
    public static final String TAG = "DBTes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        List<AppUsage> allData = LitePal.findAll(AppUsage.class);
        for ( AppUsage i : allData){
            Log.i(TAG, i.getAppName()+"\t\t\t"+i.getStartTime()+"\t"+i.getDuration());
        }
    }
}
