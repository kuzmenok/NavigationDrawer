package com.example.sok.navigationdrawer.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFilterBindableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends AbstractRecyclerBindableAdapter<T, VH> {

    private final Object lock = new Object();
    private List<T> originalValues;
    private List<T> objects;
    private ArrayFilter filter;
    private OnFilterObjectCallback onFilterObjectCallback;

    protected AbstractFilterBindableAdapter() {
        objects = new ArrayList<>();
        originalValues = new ArrayList<>();
    }

    @Override
    public void addAll(List<? extends T> data) {
        if (objects.containsAll(data)) {
            return;
        }
        objects.clear();
        objects.addAll(data);
        originalValues.clear();
        originalValues.addAll(data);
        notifyItemRangeInserted(getHeadersCount(), data.size());
    }

    public void addShowed(List<? extends T> data) {
        objects.clear();
        objects.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void removeChild(int position) {
        objects.remove(position);
        originalValues.remove(position);
        objects.remove(position);
        notifyItemRemoved(position + getHeadersCount());
        int positionStart = position + getHeadersCount();
        int itemCount = objects.size() - position;
        notifyItemRangeChanged(positionStart, itemCount);
    }

    public void setOnFilterObjectCallback(OnFilterObjectCallback objectCallback) {
        onFilterObjectCallback = objectCallback;
    }

    @Override
    public T getItem(int position) {
        return objects.get(position);
    }

    @Override
    public int getRealItemCount() {
        return objects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected abstract String itemToString(T item);

    public Filter getFilter() {
        if (filter == null) {
            filter = new ArrayFilter();
        }
        return filter;
    }

    public interface OnFilterObjectCallback {
        void handle(int countFilterObject);
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (originalValues == null) {
                synchronized (lock) {
                    originalValues = new ArrayList<>(objects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<T> list;
                synchronized (lock) {
                    list = new ArrayList<>(originalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<T> values;
                synchronized (lock) {
                    values = new ArrayList<>(originalValues);
                }

                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    final String valueText = itemToString(value).toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            objects = (List<T>) results.values;
            if (onFilterObjectCallback != null) {
                onFilterObjectCallback.handle(results.count);
            }
            notifyDataSetChanged();
        }
    }
}
