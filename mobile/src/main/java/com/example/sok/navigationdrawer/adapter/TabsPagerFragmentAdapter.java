package com.example.sok.navigationdrawer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.sok.navigationdrawer.fragment.InfoFragment;
import com.example.sok.navigationdrawer.fragment.MapFragment;

public class TabsPagerFragmentAdapter extends FragmentStatePagerAdapter {

    public TabsPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InfoFragment();
            case 1:
                return new MapFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Info";
            case 1:
                return "Map";
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
