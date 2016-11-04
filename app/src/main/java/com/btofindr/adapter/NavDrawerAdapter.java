package com.btofindr.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.btofindr.R;
import com.btofindr.model.NavDrawerItem;

import java.util.ArrayList;

/**
 * This is the adapter for Navigation Drawer Item.
 * The bridge between UI components and
 * the data set that fill data into the UI components.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
 */

public class NavDrawerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    /**
     * Constructor for a NavDrawerAdapter.
     *
     * @param context The current state of the application
     * @param navDrawerItems The data to be placed in the UI components
     */
    public NavDrawerAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    /**
     * Get a View that displays the data at the specific position in the data set.
     *
     * @param position The position of view in the list
     * @param convertView The old view
     * @param parent The parent that this view will be attached to
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nav_drawer_list_item, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.iv_icon);
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);

        icon.setImageResource(navDrawerItems.get(position).getIcon());
        title.setText(navDrawerItems.get(position).getTitle());

        return convertView;
    }

    /**
     * Get size of the navDrawerItems ArrayList.
     * @return
     */
    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    /**
     * Get a particular navigation drawer item.
     * @return
     */
    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    /**
     * Get position of navigation drawer item in the list.
     * @param position The position of the item
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
}