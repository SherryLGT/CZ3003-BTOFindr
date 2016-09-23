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
import com.btofindr.model.BlockItem;
import com.btofindr.model.UnitType;

import java.util.ArrayList;
import java.util.Iterator;

public class BlockAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BlockItem> blockItems;

    public BlockAdapter(Context context, ArrayList<BlockItem> blockItems){
        this.context = context;
        this.blockItems = blockItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.block_list_item, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.iv_icon);
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView address = (TextView) convertView.findViewById(R.id.tv_address);
        TextView unitTypes = (TextView) convertView.findViewById(R.id.tv_unit_types);
        TextView priceRange = (TextView) convertView.findViewById(R.id.tv_price_range);

        icon.setImageResource(blockItems.get(position).getIcon());
        title.setText(blockItems.get(position).getProjectName());
        address.setText(blockItems.get(position).getBlockNo() + " " + blockItems.get(position).getStreet());
        String unitTypeNames = "";
        for(Iterator it = blockItems.get(position).getUnitTypes().iterator(); it.hasNext();) {
            if(!it.hasNext()){
                unitTypeNames += it.next();
            }
            else {
                unitTypeNames += it.next() + ", ";
            }
        }
        unitTypes.setText(unitTypeNames);

        return convertView;
    }

    @Override
    public int getCount() {
        return blockItems.size();
    }

    @Override
    public Object getItem(int position) {
        return blockItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
