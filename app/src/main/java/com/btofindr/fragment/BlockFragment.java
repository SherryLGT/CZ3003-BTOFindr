package com.btofindr.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.adapter.FloorAdapter;
import com.btofindr.controller.Utility;
import com.btofindr.model.Block;
import com.btofindr.model.Floor;
import com.btofindr.model.Unit;
import com.btofindr.model.UnitType;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Sherry on 31/08/2016.
 */

public class BlockFragment extends Fragment {

    private ProgressDialog dialog;
    private ImageView ivProjectImage;
    private TextView tvTitle, tvAddress, tvUnitTypes, tvPriceRange;
    private Button btnViewMapPlan;
    private LinearLayout llUnitTypes;
    private TextView tvQuotaChinese, tvQuoteMalay, tvQuotaOthers;
    private ListView lvFloors;

    private Gson gson;
    private float scale;
    private Block block;
    private static int selectedView = -1;

    public BlockFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_block, container, false);

        ivProjectImage = (ImageView) rootView.findViewById(R.id.iv_project);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvAddress = (TextView) rootView.findViewById(R.id.tv_address);
        tvUnitTypes = (TextView) rootView.findViewById(R.id.tv_unit_types);
        tvPriceRange = (TextView) rootView.findViewById(R.id.tv_price_range);
        btnViewMapPlan = (Button) rootView.findViewById(R.id.btn_view_map_plan);
        llUnitTypes = (LinearLayout) rootView.findViewById(R.id.ll_unit_types);
        tvQuotaChinese = (TextView) rootView.findViewById(R.id.tv_quota_chinese);
        tvQuoteMalay = (TextView) rootView.findViewById(R.id.tv_quota_malay);
        tvQuotaOthers = (TextView) rootView.findViewById(R.id.tv_quota_others);
        lvFloors = (ListView) rootView.findViewById(R.id.lv_floors);

        block = SearchResultFragment.selectedBlock;
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        new getImage().execute();
        new loadData().execute();

        tvTitle.setText(block.getProject().getProjectName());
        tvAddress.setText(block.getBlockNo() + " " + block.getStreet());
        String unitTypeNames = "";
        for (int i = 0; i < block.getUnitTypes().size(); i++) {
            unitTypeNames += block.getUnitTypes().get(i).getUnitTypeName();

            if(i != block.getUnitTypes().size() - 1) {
                unitTypeNames += ", ";
            }
        }
        tvUnitTypes.setText(unitTypeNames);
        tvPriceRange.setText("Price: $" + Utility.formatPrice(block.getMinPrice()) + " - $" + Utility.formatPrice(block.getMaxPrice()));
        btnViewMapPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.view_map_plan_choice);
                builder.setSingleChoiceItems(R.array.map_plan_list, selectedView, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selectedView = which;
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });

        return rootView;
    }

    private class getImage extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            bitmap = null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ivProjectImage.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                InputStream in = new java.net.URL(block.getProject().getProjectImage()).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }

    private class loadData extends AsyncTask<Void, Integer, Object> {
        @Override
        protected void onPreExecute() {
            dialog.show();
            gson = new Gson();
            scale = getActivity().getResources().getDisplayMetrics().density;
        }

        @Override
        protected void onPostExecute(Object o) {
            for(UnitType unitType : block.getUnitTypes()) {
                Button tab = new Button(getContext());
                tab.setText(unitType.getUnitTypeName());

                tvQuotaChinese.setText("Chinese: "  + unitType.getQuotaChinese());
                tvQuoteMalay.setText("Malay: "  + unitType.getQuotaMalay());
                tvQuotaOthers.setText("Indian/Others: "  + unitType.getQuotaOthers());

                final ArrayList<Floor> floorItems = new ArrayList<Floor>();
                for(Unit unit : unitType.getUnits()) {
                    Floor tempFloor = null;
                    for(Floor floor : floorItems)
                    {
                        if(floor.getFloor().equals(unit.getUnitNo().substring(0, 3))){
                            tempFloor = floor;
                        }
                    }

                    if(tempFloor == null) {
                        tempFloor = new Floor(unit.getUnitNo().substring(0, 3), 1000000.0, 0.0);
                        floorItems.add(tempFloor);
                    }
                    if(unit.getPrice() < tempFloor.getMinPrice()) {
                        tempFloor.setMinPrice(unit.getPrice());
                    }
                    if(unit.getPrice() > tempFloor.getMaxPrice()) {
                        tempFloor.setMaxPrice(unit.getPrice());
                    }
                }

                tab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lvFloors.setAdapter(new FloorAdapter(getContext(), floorItems));
                    }
                });
                llUnitTypes.addView(tab);
            }
            llUnitTypes.getChildAt(0).performClick();
            if(lvFloors.getAdapter() != null) {
                int itemsCount = lvFloors.getAdapter().getCount();
                int totalDividerHeight = lvFloors.getDividerHeight() * (itemsCount - 1);
                int totalItemsHeight = 0;
                for(int i = 0; i < itemsCount; i++) {
                    View item = lvFloors.getAdapter().getView(0, null, lvFloors);
                    item.measure(0, 0);
                    totalItemsHeight += item.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = lvFloors.getLayoutParams();
                params.height = totalItemsHeight + totalDividerHeight;
                lvFloors.setLayoutParams(params);
                lvFloors.requestLayout();
            }
            dialog.dismiss();
        }

        @Override
        protected Object doInBackground(Void... params) {
            block = gson.fromJson(Utility.getRequest("Block/GetBlockWithUnits?blockId="+block.getBlockId()), Block.class);

            return block;
        }
    }
}
