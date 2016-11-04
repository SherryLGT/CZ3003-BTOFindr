package com.btofindr.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
 * This is the adapter for Block.
 * The bridge between UI components and
 * the data set that fill data into the UI components.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
 */

public class BlockAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BlockItem> blockItems;

    private ImageView icon;
    private String url;
    private Bitmap bitmap;

    /**
     * Constructor for a BlockAdapter.
     *
     * @param context The current state of the application
     * @param blockItems The data to be placed in the UI components
     */
    public BlockAdapter(Context context, ArrayList<BlockItem> blockItems) {
        this.context = context;
        this.blockItems = blockItems;
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
            // Initialize a LayoutInflater to inflate a view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.block_list_item, null);
        }

        icon = (ImageView) convertView.findViewById(R.id.iv_icon);
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView address = (TextView) convertView.findViewById(R.id.tv_address);
        TextView unitTypes = (TextView) convertView.findViewById(R.id.tv_unit_types);
        TextView priceRange = (TextView) convertView.findViewById(R.id.tv_price_range);
        TextView travelTime = (TextView) convertView.findViewById(R.id.tv_travel_time);

        url = blockItems.get(position).getIcon();
        new getImage().execute();
        title.setText(blockItems.get(position).getProjectName());
        address.setText(blockItems.get(position).getBlockNo() + " " + blockItems.get(position).getStreet());
        // Handles display of multiple unit types
        String unitTypeNames = "";
        for (int i = 0; i < blockItems.get(position).getUnitTypes().size(); i++) {
            unitTypeNames += blockItems.get(position).getUnitTypes().get(i).getUnitTypeName();

            if (i != blockItems.get(position).getUnitTypes().size() - 1) {
                unitTypeNames += ", ";
            }
        }
        unitTypes.setText(unitTypeNames);
        priceRange.setText("Price: $" + Utility.formatPrice(blockItems.get(position).getMinPrice()) + " - $" + Utility.formatPrice(blockItems.get(position).getMaxPrice()));
        travelTime.setText("Travelling Time: " + (blockItems.get(position).getTravelTime() / 60) + " mins (" + blockItems.get(position).getTravelDist() / 1000 + " km)");
        return convertView;
    }

    /**
     * Get size of the blockItems ArrayList.
     * @return
     */
    @Override
    public int getCount() {
        return blockItems.size();
    }

    /**
     * Get a particular block item.
     * @return
     */
    @Override
    public Object getItem(int position) {
        return blockItems.get(position);
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

    /**
     * An AsyncTask to load image from uri for a Block
     */
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }
}
