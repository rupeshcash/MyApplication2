package com.example.administrator.myapplication.Utils;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

@SuppressLint("ValidFragment")
public class TabFragment1 extends Fragment implements UpdateableFragment{
    TextView wid,wname,weff ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.tab_fragment_1, container, false);
        wid = v.findViewById(R.id.wstationid);
        wname =v.findViewById(R.id.wname);
        weff = v.findViewById(R.id.wefficiency);
        return v;
    }


        @Override
        public void update(MyDataObject xyzData) {
           wid.setText(xyzData.id);
           wname.setText(xyzData.name);
           weff.setText(xyzData.workstation_eff);
        }



}