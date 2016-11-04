package com.btofindr.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.btofindr.R;
import com.btofindr.controller.Utility;
import com.btofindr.model.Floor;

import java.util.ArrayList;

/**
 * This is the adapter for Floor.
 * The bridge between UI components and
 * the data set that fill data into the UI components.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
 */

public class FloorAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Floor> floorItems;

    /**
     * Constructor for a FloorAdapter.
     *
     * @param context The current state of the application
     * @param floorItems The data to be placed in the UI components
     */
    public FloorAdapter(Context context, ArrayList<Floor> floorItems) {
        this.context = context;
        this.floorItems = floorItems;
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
            convertView = inflater.inflate(R.layout.floor_list_item, null);
        }

        TextView tvFloor = (TextView) convertView.findViewById(R.id.tv_floor);
        TextView tvPriceRange = (TextView) convertView.findViewById(R.id.tv_price_range);

        tvFloor.setText("FLOOR " + floorItems.get(position).getFloor());
        tvPriceRange.setText("$" + Utility.formatPrice(floorItems.get(position).getMinPrice()) + " - $" + Utility.formatPrice(floorItems.get(position).getMaxPrice()));

        return convertView;
    }

    /**
     * Get size of the floorItems ArrayList.
     * @return
     */
    @Override
    public int getCount() {
        return floorItems.size();
    }

    /**
     * Get a particular floor item.
     * @return
     */
    @Override
    public Object getItem(int position) {
        return floorItems.get(position);
    }

    /**
     * Get position of floor item in the list.
     * @param position The position of the item
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
}
