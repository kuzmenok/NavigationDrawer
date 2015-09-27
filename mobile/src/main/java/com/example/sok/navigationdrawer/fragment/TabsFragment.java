package com.example.sok.navigationdrawer.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sok.navigationdrawer.R;
import com.example.sok.navigationdrawer.adapter.TabsPagerFragmentAdapter;

public class TabsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabs, container, false);

        intiTabs(rootView);

        return rootView;
    }

    private void intiTabs(View rootView) {
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
