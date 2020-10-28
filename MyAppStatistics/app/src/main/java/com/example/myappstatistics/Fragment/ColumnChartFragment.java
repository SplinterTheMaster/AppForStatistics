package com.example.myappstatistics.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myappstatistics.R;


/**
 * A simple {@link Fragment} subclass.
 */

// show the data from LitePal

public class ColumnChartFragment extends Fragment {


    public ColumnChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bar_chart, container, false);
    }

}
