package com.example.myappstatistics.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myappstatistics.Fragment.AppUsageFragment;
import com.example.myappstatistics.Fragment.ColumnChartFragment;
import com.example.myappstatistics.Fragment.ListFragment;
import com.example.myappstatistics.Fragment.PieChartFragment;
import com.example.myappstatistics.Model.AppUsage;
import com.example.myappstatistics.R;
import org.litepal.LitePal;

import java.util.List;


public class ShowActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "DBTes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        TextView usageTV = (TextView)findViewById(R.id.usage_text_view);
        usageTV.setOnClickListener(this);
        TextView columnTV = (TextView)findViewById(R.id.column_text_view);
        columnTV.setOnClickListener(this);
        TextView pieTV = (TextView)findViewById(R.id.pie_text_view);
        pieTV.setOnClickListener(this);
        TextView listTV = (TextView)findViewById(R.id.list_text_view);
        listTV.setOnClickListener(this);

        List<AppUsage> allData = LitePal.findAll(AppUsage.class);
        for ( AppUsage i : allData){
            Log.i(TAG, i.getAppName()+"\t\t\t"+i.getStartTime()+"\t"+i.getDuration());
        }
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.usage_text_view:
                replaceFragment(new AppUsageFragment());
                break;
            case R.id.column_text_view:
                replaceFragment(new ColumnChartFragment());
                break;
            case R.id.pie_text_view:
                replaceFragment(new PieChartFragment());
                break;
            case R.id.list_text_view:
                replaceFragment(new ListFragment());
                break;
            default:
                break;
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.show_fragment , fragment);
        transaction.commit();
    }
}
