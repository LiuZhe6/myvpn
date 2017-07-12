package com.vpn.mine.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
/**
 * Created by coder on 17-7-12.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter{

    private ArrayList<Fragment> fragments;

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}

