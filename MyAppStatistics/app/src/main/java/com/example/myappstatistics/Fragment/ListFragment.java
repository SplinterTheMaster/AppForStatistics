package com.example.myappstatistics.Fragment;


import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myappstatistics.Model.AppItem;
import com.example.myappstatistics.Model.AppItemAdapter;
import com.example.myappstatistics.Model.AppUsage;
import com.example.myappstatistics.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

// show the data from LitePal in a recyclerView

public class ListFragment extends Fragment {
    public static final String TAG = "ListFragment";
    private View thisView;
    private List<AppItem> appItemList = new ArrayList<>();
    public ListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        List<AppUsage> data = LitePal.findAll(AppUsage.class);
        Log.i(TAG, String.valueOf(data.get(0).getStartTime()));
        thisView = view;
        initList(data);
        RecyclerView recyclerView = (RecyclerView)thisView.findViewById(R.id.list_fragment_recycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(thisView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        AppItemAdapter adapter = new AppItemAdapter(appItemList);
        recyclerView.setAdapter(adapter);
        return view;
    }
    private void initList(List<AppUsage> allData){
        for (int i = allData.size() - 1; i >= 0; i--){
            Drawable appPic = getActivity().getDrawable(R.drawable.ic_launcher_foreground);;
            try {
                appPic = getActivity().getPackageManager().getApplicationIcon(allData.get(i).getAppName());
            } catch (PackageManager.NameNotFoundException e){
                Log.e(TAG, "appNameNotFound: " + allData.get(i).getAppName());
            }
            AppItem appItem = new AppItem(allData.get(i).getAppName(), appPic,
                    allData.get(i).getStartTime(), allData.get(i).getDuration());
            appItemList.add(appItem);
        }
    }

}
