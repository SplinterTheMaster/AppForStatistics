package com.example.myappstatistics.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myappstatistics.CollectService;
import com.example.myappstatistics.Model.AppUsage;
import com.example.myappstatistics.R;

import org.litepal.LitePal;

import java.util.List;

// launch Activity

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
        Button deleteButton = (Button)findViewById(R.id.delete_all_button);
        deleteButton.setOnClickListener(this);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.show_results_button :
                List<AppUsage> data = LitePal.findAll(AppUsage.class);
                if (data.isEmpty()){
                    Toast.makeText(MainActivity.this, "数据库为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent showIntent = new Intent(MainActivity.this, ShowActivity.class);
                startActivity(showIntent);
                break;
            case R.id.start_service_button :
                Intent startIntent = new Intent(MainActivity.this, CollectService.class);
                startService(startIntent);
                Toast.makeText(MainActivity.this, "已开启服务", Toast.LENGTH_SHORT).show();
                break;
            case R.id.stop_service_button :
                Intent stopIntent = new Intent(MainActivity.this, CollectService.class);
                Toast.makeText(MainActivity.this, "已关闭服务", Toast.LENGTH_SHORT).show();
                stopService(stopIntent);
                break;
            case R.id.apply_permission_button :
                Intent applyIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(applyIntent);
                break;
            case R.id.delete_all_button :
                LitePal.deleteAll(AppUsage.class );
                Toast.makeText(MainActivity.this, "已清空数据库", Toast.LENGTH_SHORT).show();
                break;
            default :
                break;
        }
    }
}
