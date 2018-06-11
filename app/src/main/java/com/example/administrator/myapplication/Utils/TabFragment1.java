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
    TextView textView ;
    String id =null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.tab_fragment_1, container, false);
        textView =  v.findViewById(R.id.textView1);
        return v;
    }




        @Override
        public void update(MyDataObject xyzData) {
            // this method will be called for every fragment in viewpager
            // so check if update is for this fragment
            textView.setText(xyzData.name);
        }


}