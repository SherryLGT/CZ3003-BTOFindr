package com.btofindr.activity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.btofindr.R;
import com.btofindr.adapter.NavDrawerAdapter;
import com.btofindr.fragment.CompareUnitsFragment;
import com.btofindr.fragment.FavouriteFragment;
import com.btofindr.fragment.HistoryFragment;
import com.btofindr.fragment.HomeFragment;
import com.btofindr.fragment.ProfileFragment;
import com.btofindr.fragment.RecommendedFragment;
import com.btofindr.model.NavDrawerItem;

import java.util.ArrayList;

/**
 * This activity is used to display the layout of the whole program.
 * It contains the navigation drawer and layout for content.
 * Any changes to the display will be displayed in the content layout.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
 */

public class MainActivity extends AppCompatActivity {

    private String[] navDrawerTitles;
    private TypedArray navDrawerIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerAdapter navDrawerAdapter;
    private DrawerLayout navDrawerLayout;
    private ListView lvNavDrawer;
    private ActionBarDrawerToggle navDrawerToggle;
    private FrameLayout flContainer;
    public static float scale;

    @Override
    @SuppressWarnings("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        lvNavDrawer = (ListView) findViewById(R.id.lv_nav_drawer);
        flContainer = (FrameLayout) findViewById(R.id.fl_container);

        // Set HomeFragment to the content layout
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment()).commit();

        // Set up navigation
        navDrawerTitles = getResources().getStringArray(R.array.nav_drawer_titles);
        navDrawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(navDrawerIcons.getResourceId(0, -1), navDrawerTitles[0])); // Home
        navDrawerItems.add(new NavDrawerItem(navDrawerIcons.getResourceId(1, -1), navDrawerTitles[1])); // Compare Units
        navDrawerItems.add(new NavDrawerItem(navDrawerIcons.getResourceId(2, -1), navDrawerTitles[2])); // Favourites
        navDrawerItems.add(new NavDrawerItem(navDrawerIcons.getResourceId(3, -1), navDrawerTitles[3])); // History
        navDrawerItems.add(new NavDrawerItem(navDrawerIcons.getResourceId(4, -1), navDrawerTitles[4])); // Recommended
        navDrawerItems.add(new NavDrawerItem(navDrawerIcons.getResourceId(5, -1), navDrawerTitles[5])); // Profile
        navDrawerIcons.recycle();

        navDrawerAdapter = new NavDrawerAdapter(getApplicationContext(), navDrawerItems);
        lvNavDrawer.setAdapter(navDrawerAdapter);
        lvNavDrawer.setOnItemClickListener(new navDrawerItemClickListener());

        navDrawerToggle = new ActionBarDrawerToggle(this, navDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        navDrawerLayout.addDrawerListener(navDrawerToggle);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        lvNavDrawer.setItemChecked(0, true);

        // Get display size (dp)
        scale = this.getResources().getDisplayMetrics().density;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_edit);
        item.setVisible(false);
        return true;
    }

    /**
     * Called when a navigation drawer item is clicked
     */
    private class navDrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);

            navDrawerItems.get(0).setIcon(R.drawable.ic_home);
            navDrawerItems.get(1).setIcon(R.drawable.ic_compare);
            navDrawerItems.get(2).setIcon(R.drawable.ic_favourites);
            navDrawerItems.get(3).setIcon(R.drawable.ic_history);
            navDrawerItems.get(4).setIcon(R.drawable.ic_recommended);
            navDrawerItems.get(5).setIcon(R.drawable.ic_profile);

            switch (position) {
                case 0: // Home
                    getSupportActionBar().setTitle(R.string.app_name);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment()).commit();
                    break;
                case 1: // Compare Units
                    getSupportActionBar().setTitle(R.string.title_compare_units);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new CompareUnitsFragment()).addToBackStack("CompareUnitsFragment").commit();
                    break;
                case 2: // Favourites
                    getSupportActionBar().setTitle(R.string.title_favourites);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new FavouriteFragment()).addToBackStack("FavouriteFragment").commit();
                    break;
                case 3: // History
                    getSupportActionBar().setTitle(R.string.title_history);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new HistoryFragment()).addToBackStack("HistoryFragment").commit();
                    break;
                case 4: // Recommended
                    getSupportActionBar().setTitle(R.string.title_recommended);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new RecommendedFragment()).addToBackStack("RecommendedFragment").commit();
                    break;
                case 5: // Profile
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new ProfileFragment()).addToBackStack("ProfileFragment").commit();
                    getSupportActionBar().setTitle(R.string.title_profile);
                    break;
                default:
                    break;
            }

            lvNavDrawer.setItemChecked(position, true);
            lvNavDrawer.setSelection(position);
            navDrawerLayout.closeDrawer(lvNavDrawer);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
        } else if (navDrawerLayout.isDrawerOpen(lvNavDrawer)) {
            navDrawerLayout.closeDrawer(lvNavDrawer);
        } else {
            navDrawerLayout.openDrawer(lvNavDrawer);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setNegativeButton("No", null).show();
        } else {
            getSupportFragmentManager().popBackStack();
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        lvNavDrawer.setItemChecked(0, true);
    }

    /**
     * Called whenever there is a change in content.
     * To set title to match content of layout.
     *
     * @param title The title to be set into the action bar
     */
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
