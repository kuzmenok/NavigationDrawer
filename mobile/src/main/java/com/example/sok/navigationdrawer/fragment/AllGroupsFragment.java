package com.example.sok.navigationdrawer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.example.sok.navigationdrawer.R;
import com.example.sok.navigationdrawer.adapter.GroupsFilterAdapter;
import com.example.sok.navigationdrawer.data.Group;

import java.util.ArrayList;
import java.util.List;

public class AllGroupsFragment extends Fragment implements Filter.FilterListener {
    private GroupsFilterAdapter adapter;
    private RecyclerView recyclerView;
    private View noResultView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_groups, container, false);
        setHasOptionsMenu(true);

        noResultView = rootView.findViewById(R.id.no_results_view);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new GroupsFilterAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //test data
        List<Group> list = new ArrayList<>();
        list.add(new Group("one"));
        list.add(new Group("two"));
        list.add(new Group("three"));
        list.add(new Group("four"));
        list.add(new Group("five"));
        adapter.clear();
        adapter.addAll(list);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.all_groups_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText, AllGroupsFragment.this);
                return true;
            }
        });
    }

    @Override
    public void onFilterComplete(int count) {
        noResultView.setVisibility(count > 0 ? View.GONE : View.VISIBLE);
    }
}
