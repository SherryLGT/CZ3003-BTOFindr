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
import com.btofindr.model.UnitItem;

import java.util.ArrayList;

/**
 * Created by Sherry on 06/10/2016.
 */

public class UnitAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UnitItem> unitItems;

    private TextView unitNo, price;

    public UnitAdapter(Context context, ArrayList<UnitItem> unitItems){
        this.context = context;
        this.unitItems = unitItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.unit_list_item, null);
        }

        unitNo = (TextView) convertView.findViewById(R.id.tv_unit_no);
        price = (TextView) convertView.findViewById(R.id.tv_price);

        unitNo.setText("FLOOR " + unitItems.get(position).getUnitNo());
        price.setText("$" + Utility.formatPrice(unitItems.get(position).getPrice()));

        return convertView;
    }

    @Override
    public int getCount() {
        return unitItems.size();
    }

    @Override
    public Object getItem(int position) {
        return unitItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
