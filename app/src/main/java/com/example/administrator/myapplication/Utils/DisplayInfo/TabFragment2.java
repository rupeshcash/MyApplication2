package com.example.administrator.myapplication.Utils.DisplayInfo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class TabFragment2 extends Fragment implements UpdateableFragment{
    TextView curr_task, runningtimetext, temptext, JobSheetID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.tab_fragment_2, container, false);
        curr_task =  v.findViewById(R.id.curr_task);
        JobSheetID = v.findViewById(R.id.JobSheetID);
        runningtimetext = v.findViewById(R.id.running_time);
        temptext = v.findViewById(R.id.temp);
        return  v;
       }

    @Override
    public void update(MyDataObject xyzData) {
        // this method will be called for every fragment in viewpager
        curr_task.setText(xyzData.curr_task);
        runningtimetext.setText(xyzData.running_time);
        temptext.setText(xyzData.curr_temp);
        JobSheetID.setText("3312");
    }
}