package com.example.administrator.myapplication.Utils.DisplayInfo;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    MyDataObject matched_obj;
    public PagerAdapter(FragmentManager fm, int NumOfTabs, MyDataObject sam) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        matched_obj = sam;

    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
            {
                TabFragment1 tab1 = new TabFragment1();
                return tab1;
            }
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;
            default:
                return null;
        }
    }

    //call this method to update fragments in ViewPager dynamically
    public void update(MyDataObject xyzData) {
        this.matched_obj = xyzData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof UpdateableFragment) {
            ((UpdateableFragment) object).update(matched_obj);
        }
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(object);
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
