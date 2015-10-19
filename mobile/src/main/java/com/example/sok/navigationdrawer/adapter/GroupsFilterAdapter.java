package com.example.sok.navigationdrawer.adapter;

import android.view.View;

import com.example.sok.navigationdrawer.R;
import com.example.sok.navigationdrawer.data.Group;
import com.example.sok.navigationdrawer.view.GroupViewHolder;

public class GroupsFilterAdapter extends AbstractFilterBindableAdapter<Group, GroupViewHolder> {

    @Override
    protected String itemToString(Group item) {
        return item.getName();
    }

    @Override
    protected int layoutId(int type) {
        return R.layout.list_item_group;
    }

    @Override
    protected void onBindItemViewHolder(GroupViewHolder viewHolder, final int position, int type) {
        viewHolder.bindView(getItem(position));
    }

    @Override
    protected GroupViewHolder viewHolder(View view, int type) {
        return new GroupViewHolder(view);
    }
}
