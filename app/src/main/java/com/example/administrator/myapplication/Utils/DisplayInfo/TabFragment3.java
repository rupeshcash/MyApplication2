package com.example.administrator.myapplication.Utils.DisplayInfo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class TabFragment3 extends Fragment implements UpdateableFragment{
    TextView opname, op_remarks, op_contact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.tab_fragment_3, container, false);
        opname=  v.findViewById(R.id.opname);
        op_remarks = v.findViewById(R.id.opremarks);
        return  v;
    }

    @Override
    public void update(MyDataObject xyzData) {
        // this method will be called for every fragment in viewpager
        opname.setText(xyzData.operator_name);
        op_remarks.setText(xyzData.remarks);
    }
}
