package com.vinuthana.vinvidyaadmin.viewPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by KISHAN on 08-07-2017.
 */

public class VPager extends FragmentPagerAdapter {

    ArrayList<Fragment> fragment = new ArrayList<>();
    ArrayList<String> str = new ArrayList<>();

    public VPager(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fgmt, String val) {
        fragment.add(fgmt);
        str.add(val);
    }

    @Override
    public Fragment getItem(int position) {
        return fragment.get(position);
    }

    @Override
    public int getCount() {
        return fragment.size();
    }

    public CharSequence getPageTitle(int position) {
        return str.get(position);
    }
}
