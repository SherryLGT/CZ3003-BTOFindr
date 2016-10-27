package com.btofindr.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.btofindr.R;
import com.btofindr.controller.Utility;
import com.btofindr.model.BlockItem;
import com.btofindr.model.UnitItem;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;

import static com.btofindr.R.id.relLayout1;
import static com.btofindr.fragment.CompareUnitsFragment.blockList;
import static com.btofindr.fragment.CompareUnitsFragment.selectedUnitList;

/**
 * Created by Amandaa on 10/12/2016.
 */

public class CompareAdapter extends BaseAdapter   {
    private Context context;
    private ArrayList<UnitItem> unitItems;
    private ArrayList<BlockItem> blockItems;

    private Gson gson;
    private ImageView icon;
    private String url;
    private Bitmap bitmap;
    View rootView;
    ListView lvUnits;
    TextView tv_nofave;


    private ArrayList<Integer> favourites;

    public CompareAdapter(Context context, ArrayList<UnitItem> unitItems,
                          ArrayList<BlockItem> blockItems, ArrayList<Integer> favourites, View rootView){
        this.context = context;
        this.unitItems = unitItems;
        this.blockItems = blockItems;
        this.favourites = favourites;
        this.rootView = rootView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.compare_list_item, null);
        }

        RelativeLayout l1 = (RelativeLayout) convertView.findViewById(relLayout1);
        icon = (ImageView) convertView.findViewById(R.id.iv_icon);//project image
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView address = (TextView) convertView.findViewById(R.id.tv_address);
        TextView unitNumber = (TextView) convertView.findViewById(R.id.tv_unit_no);
        TextView unitType = (TextView) convertView.findViewById(R.id.tv_unit_types);
        TextView price = (TextView) convertView.findViewById(R.id.tv_price);
        tv_nofave = (TextView)rootView.findViewById(R.id.tv_nofave);

        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkboxUnit);


        url = blockItems.get(position).getIcon();
        new getImage().execute();

        title.setText(blockItems.get(position).getProjectName());
        unitNumber.setText(unitItems.get(position).getUnitNo()+" ");
        address.setText(blockItems.get(position).getBlockNo() + " " + blockItems.get(position).getStreet());
        price.setText("Price: $" + Utility.formatPrice(unitItems.get(position).getPrice()));
        unitType.setText("("+unitItems.get(position).getUnitType().getUnitTypeName()+")"); //room type
        cb.setTag(position);

        cb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                CheckBox cb = (CheckBox) v;

                int position = (Integer) v.getTag();
                UnitItem unit = unitItems.get((Integer) v.getTag());

                if(selectedUnitList.contains(unit)){
                    selectedUnitList.remove(unit);
                    blockList.remove(blockItems.get(position));
                }
                else{
                    if(selectedUnitList.size()<2) {
                        selectedUnitList.add(unit);
                        BlockItem a = blockItems.get(position);
                        blockItems.get(position).getBlockId();
                        blockList.add(a);
                    }else {
                        Toast.makeText(context, "You can only select two units", Toast.LENGTH_SHORT).show();
                        cb.toggle();
                    }
                }

            }
        });

        lvUnits = (ListView)rootView.findViewById(R.id.lv_units);


        return convertView;
    }

    @Override
    public int getCount() {
        return unitItems.size();
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public Object getItem(int position) {
        return unitItems.get(position);
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
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }
}
