package com.example.sok.navigationdrawer.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sok.navigationdrawer.R;
import com.example.sok.navigationdrawer.data.Group;

public class GroupViewHolder extends RecyclerView.ViewHolder {
    TextView name;

    public GroupViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.group_name);
    }

    public void bindView(Group item) {
        name.setText(item.getName());
    }
}