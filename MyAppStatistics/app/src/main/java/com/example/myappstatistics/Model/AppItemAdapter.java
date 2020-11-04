package com.example.myappstatistics.Model;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myappstatistics.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//Adapter of recyclerView in Fragment

public class AppItemAdapter extends RecyclerView.Adapter<AppItemAdapter.ViewHolder> {
    private List<AppItem> mAppItemList;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView appName;
        ImageView appIcon;
        TextView startTime;
        TextView duration;
        public ViewHolder(View view){
            super(view);
            appName = (TextView)view.findViewById(R.id.textview_package_name);
            appIcon = (ImageView)view.findViewById(R.id.app_icon);
            startTime = (TextView)view.findViewById(R.id.textview_start_time);
            duration = (TextView)view.findViewById(R.id.textview_duration);

        }
    }
    public AppItemAdapter(List<AppItem> appItemList){
        mAppItemList = appItemList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        AppItem appItem = mAppItemList.get(position);
        viewHolder.appName.setText(appItem.getAppName());
        viewHolder.appIcon.setImageDrawable(appItem.getAppIcon());
        long st = appItem.getStartTime();
        viewHolder.startTime.setText(dateFormat.format(new Date(st)));
        viewHolder.duration.setText(appItem.getDuration()/1000+"秒");
    }
    public int getItemCount(){
        return mAppItemList.size();
    }
}
