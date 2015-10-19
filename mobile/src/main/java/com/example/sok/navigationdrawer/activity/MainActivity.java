package com.example.sok.navigationdrawer.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.sok.navigationdrawer.R;
import com.example.sok.navigationdrawer.fragment.AllGroupsFragment;
import com.example.sok.navigationdrawer.fragment.CurrentGroupFragment;
import com.example.sok.navigationdrawer.fragment.PreferencesFragment;
import com.example.sok.navigationdrawer.fragment.TabsFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initNavigationView();
        replaceFragment(new AllGroupsFragment());
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.all_groups_item:
                        fragment = new AllGroupsFragment();
                        break;
                    case R.id.current_group_item:
                        fragment = new CurrentGroupFragment();
                        break;
                    case R.id.tabs_item:
                        fragment = new TabsFragment();
                        break;
                    case R.id.preferences_item:
                        fragment = new PreferencesFragment();
                        break;
                }
                replaceFragment(fragment);
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
    }
}