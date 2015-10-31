package com.example.sok.navigationdrawer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sok.navigationdrawer.R;
import com.example.sok.navigationdrawer.app.Constants;

public class InfoFragment extends Fragment implements View.OnClickListener, Constants {
    private OnChangeTripStateListener onChangeTripStateListener;

    public interface OnChangeTripStateListener {
        void onChangeTripState(int currentState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        rootView.findViewById(R.id.start).setOnClickListener(this);
        rootView.findViewById(R.id.stop).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onChangeTripStateListener = (OnChangeTripStateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                onChangeTripStateListener.onChangeTripState(TRIP_STATE_RUNNING);
                break;
            case R.id.stop:
                onChangeTripStateListener.onChangeTripState(TRIP_STATE_STOPPED);
                break;
            default:
                break;
        }
    }
}
