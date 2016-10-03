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
 * Created by Sherry on 31/08/2016.
 */

public class FloorAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Floor> floorItems;

    public FloorAdapter(Context context, ArrayList<Floor> floorItems) {
        this.context = context;
        this.floorItems = floorItems;
    }

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

    @Override
    public int getCount() {
        return floorItems.size();
    }

    @Override
    public Object getItem(int position) {
        return floorItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
