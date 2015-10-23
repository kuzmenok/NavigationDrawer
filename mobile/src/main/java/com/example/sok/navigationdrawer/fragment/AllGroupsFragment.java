package com.example.sok.navigationdrawer.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.example.sok.navigationdrawer.database.DatabaseHelper;
import com.example.sok.navigationdrawer.database.HelperFactory;
import com.example.sok.navigationdrawer.dialog.GroupDialog;

import java.sql.SQLException;

public class AllGroupsFragment extends Fragment implements Filter.FilterListener, GroupDialog.GroupDialogCallback {
    private GroupsFilterAdapter adapter;
    private RecyclerView recyclerView;
    private View noResultView;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_groups, container, false);
        setHasOptionsMenu(true);
        init(rootView);

        return rootView;
    }

    private void init(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new GroupsFilterAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGroupDialog();
            }
        });
        noResultView = rootView.findViewById(R.id.no_results_view);
    }

    private void showGroupDialog() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        GroupDialog dialog = new GroupDialog();
        dialog.setCallback(this);
        dialog.show(ft, dialog.getTag());
    }

    @Override
    public void onResume() {
        super.onResume();
        TESTfillAdapter();
    }

    private void TESTfillAdapter() {
        //test data
        try {
            DatabaseHelper helper = HelperFactory.getHelper();
            for (int i = 0; i < 20; i++) {
                Group group = new Group("group " + i);
                helper.getGroupDAO().create(group);
                adapter.add(group);
            }
        } catch (SQLException ignored) {
        }
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

    @Override
    public void onGroupCreated(Group newGroup) {
        adapter.add(newGroup);
    }
}
