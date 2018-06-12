package com.example.administrator.myapplication.Utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class TabFragment2 extends Fragment implements UpdateableFragment{
    TextView textView, runningtimetext, temptext ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.tab_fragment_2, container, false);
        textView =  v.findViewById(R.id.textView1);
        runningtimetext = v.findViewById(R.id.running_time);
        temptext = v.findViewById(R.id.temp);
        return  v;
       }

    @Override
    public void update(MyDataObject xyzData) {
        // this method will be called for every fragment in viewpager
        textView.setText(xyzData.curr_task);
        runningtimetext.setText(xyzData.running_time);
        temptext.setText(xyzData.curr_temp);
    }
}