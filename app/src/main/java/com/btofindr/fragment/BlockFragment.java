package com.btofindr.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.adapter.FloorAdapter;
import com.btofindr.controller.Utility;
import com.btofindr.model.Block;
import com.btofindr.model.Floor;
import com.btofindr.model.Unit;
import com.btofindr.model.UnitType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.btofindr.activity.MainActivity.scale;
import static com.btofindr.fragment.SearchResultFragment.selectedBlock;

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
    private ArrayList<Integer> history;

    private Gson gson;
    public static int selectedView = -1;
    public static Block block;
    public static String selectedUnitType;
    public static int selectedFloor;

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

        block = selectedBlock;
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
                        if (selectedView == -1) {
                            Toast.makeText(getContext(), "Please select a choice", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            getFragmentManager().beginTransaction().replace(R.id.fl_container, new MapPlanFragment()).addToBackStack("MapPlanFragment").commit();
                        }
                    }
                }).setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });

        gson = new Gson();
        history = gson.fromJson(Utility.readFromFile("history", getContext()), new TypeToken<List<Integer>>() {
        }.getType());
        if(history==null){
            history = new ArrayList<Integer>();
        }
        if(history.contains(block.getBlockId())){
            history.remove((Integer) block.getBlockId());
        }
        history.add(block.getBlockId());
        if(Utility.writeToFile("history", gson.toJson(history), getContext()))
        {
            ArrayList<Integer> newhistory = gson.fromJson(Utility.readFromFile("history", getContext()), new TypeToken<List<Integer>>() {
            }.getType());
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_search_results));
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
                in.close();
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
        }

        @Override
        protected void onPostExecute(Object o) {
            llUnitTypes.setWeightSum(block.getUnitTypes().size());
            for(UnitType unitType : block.getUnitTypes()) {
                final Button tab = generateButton();
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
                        for(int i = 0; i < llUnitTypes.getChildCount(); i++) {
                            llUnitTypes.getChildAt(i).setSelected(false);
                        }
                        lvFloors.setAdapter(new FloorAdapter(getContext(), floorItems));
                        lvFloors.setOnItemClickListener(new floorItemClickListener());
                        tab.setSelected(true);
                        selectedUnitType = tab.getText().toString();
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

    private Button generateButton() {
        Button btn = new Button(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        btn.setLayoutParams(params);
        btn.setPadding(Utility.getPixels(10, scale), Utility.getPixels(10, scale), Utility.getPixels(10, scale), Utility.getPixels(10, scale));
        btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tab_list_selector));
        btn.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.text_selector_black_gray));

        return btn;
    }

    private class floorItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedFloor = position;
            getFragmentManager().beginTransaction().replace(R.id.fl_container, new FloorFragment()).addToBackStack("FloorFragment").commit();
        }
    }
}
