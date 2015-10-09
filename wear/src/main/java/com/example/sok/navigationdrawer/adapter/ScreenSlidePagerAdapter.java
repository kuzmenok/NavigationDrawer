package com.example.sok.navigationdrawer.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

import com.example.sok.navigationdrawer.fragment.ControlsFragment;
import com.example.sok.navigationdrawer.fragment.InfoFragment;
import com.example.sok.navigationdrawer.fragment.ListMessagesFragment;

public class ScreenSlidePagerAdapter extends FragmentGridPagerAdapter {

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getFragment(int row, int column) {
        switch (column) {
            case 0:
                return new ListMessagesFragment();
            case 1:
                return new InfoFragment();
            case 2:
                return new ControlsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int row) {
        return 3;
    }
}
