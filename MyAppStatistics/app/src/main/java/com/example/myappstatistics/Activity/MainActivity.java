package com.example.myappstatistics.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.myappstatistics.CollectService;
import com.example.myappstatistics.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showButton = (Button)findViewById(R.id.show_results_button);
        showButton.setOnClickListener(this);
        Button startButton = (Button)findViewById(R.id.start_service_button);
        startButton.setOnClickListener(this);
        Button stopButton = (Button)findViewById(R.id.stop_service_button);
        stopButton.setOnClickListener(this);
        Button applyButton = (Button)findViewById(R.id.apply_permission_button);
        applyButton.setOnClickListener(this);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.show_results_button :
                Intent showIntent = new Intent(MainActivity.this, ShowActivity.class);
                startActivity(showIntent);
                break;
            case R.id.start_service_button :
                Intent startInten = new Intent(MainActivity.this, CollectService.class);
                startService(startInten);
                break;
            case R.id.stop_service_button :
                Intent stopIntent = new Intent(MainActivity.this, CollectService.class);
                stopService(stopIntent);
                break;
            case R.id.apply_permission_button :
                Intent applyIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(applyIntent);
                break;
            default :
                break;
        }
    }
}
