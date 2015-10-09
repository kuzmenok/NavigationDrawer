package com.example.sok.navigationdrawer.adapter;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sok.navigationdrawer.R;
import com.example.sok.navigationdrawer.data.Message;

import java.util.List;

public final class ListMessagesAdapter extends WearableListView.Adapter {
    private List<Message> mMessages;
    private final Context mContext;
    private final LayoutInflater mInflater;

    public ListMessagesAdapter(Context context, List<Message> messages) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mMessages = messages;
    }

    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView author;
        private TextView text;
        public ItemViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.list_item_message, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView author = itemHolder.author;
        author.setText(mMessages.get(position).getAuthor());
        TextView text = itemHolder.text;
        text.setText("\t" + mMessages.get(position).getText());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
