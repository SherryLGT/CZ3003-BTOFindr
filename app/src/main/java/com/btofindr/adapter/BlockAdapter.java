package com.btofindr.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.btofindr.R;
import com.btofindr.controller.Utility;
import com.btofindr.model.BlockItem;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Sherry on 31/08/2016.
 */

public class BlockAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BlockItem> blockItems;

    private ImageView icon;
    private String url;
    private Bitmap bitmap;

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

        icon = (ImageView) convertView.findViewById(R.id.iv_icon);
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView address = (TextView) convertView.findViewById(R.id.tv_address);
        TextView unitTypes = (TextView) convertView.findViewById(R.id.tv_unit_types);
        TextView priceRange = (TextView) convertView.findViewById(R.id.tv_price_range);

        url = blockItems.get(position).getIcon();
        new getImage().execute();
        title.setText(blockItems.get(position).getProjectName());
        address.setText(blockItems.get(position).getBlockNo() + " " + blockItems.get(position).getStreet());
        String unitTypeNames = "";
        for (int i = 0; i < blockItems.get(position).getUnitTypes().size(); i++) {
            unitTypeNames += blockItems.get(position).getUnitTypes().get(i).getUnitTypeName();

            if (i != blockItems.get(position).getUnitTypes().size() - 1) {
                unitTypeNames += ", ";
            }
        }
        unitTypes.setText(unitTypeNames);
        priceRange.setText("Price: $" + Utility.formatPrice(blockItems.get(position).getMinPrice()) + " - $" + Utility.formatPrice(blockItems.get(position).getMaxPrice()));

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

    private class getImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            bitmap = null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            icon.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }
}
