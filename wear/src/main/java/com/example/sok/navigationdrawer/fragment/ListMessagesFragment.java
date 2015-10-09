package com.example.sok.navigationdrawer.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sok.navigationdrawer.R;
import com.example.sok.navigationdrawer.adapter.ListMessagesAdapter;
import com.example.sok.navigationdrawer.data.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListMessagesFragment extends Fragment implements WearableListView.ClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        List<Message> textMessages = new ArrayList<>();
        textMessages.add(new Message("Zhenya", "Hey, let's go for a ride!", new Date(System.currentTimeMillis())));
        textMessages.add(new Message("Stanislav", "Great idea!", new Date(System.currentTimeMillis())));
        textMessages.add(new Message("Stanislav", "Today?", new Date(System.currentTimeMillis())));
        textMessages.add(new Message("Zhenya", "Yes, at 6 o'clock.", new Date(System.currentTimeMillis())));
        textMessages.add(new Message("Stanislav", "Ok, see you", new Date(System.currentTimeMillis())));
        textMessages.add(new Message("Stanislav", "Good! So long.", new Date(System.currentTimeMillis())));
        WearableListView listView = (WearableListView) rootView.findViewById(R.id.list_messages);
        listView.setAdapter(new ListMessagesAdapter(getActivity(), textMessages));
        listView.setClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(WearableListView.ViewHolder v) {
        Integer tag = (Integer) v.itemView.getTag();
    }

    @Override
    public void onTopEmptyRegionClick() {
        Log.d("Wear", "ListMessagesFragment: onTopEmptyRegionClick()");
    }
}
