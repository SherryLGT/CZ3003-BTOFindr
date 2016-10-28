package com.btofindr.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.adapter.FavouriteAdapter;
import com.btofindr.controller.Utility;
import com.btofindr.model.Block;
import com.btofindr.model.BlockItem;
import com.btofindr.model.Project;
import com.btofindr.model.Unit;
import com.btofindr.model.UnitItem;
import com.btofindr.model.UnitType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static com.btofindr.fragment.FavouriteFragment.selectedUnit;
import static com.btofindr.fragment.FavouriteFragment.selectedUnitItem;
import static com.btofindr.fragment.FavouriteFragment.selectedBlockItem;

import java.util.ArrayList;
import java.util.List;

import static com.btofindr.fragment.UnitDetailsFragment.recommend;

public class RecommendedFragment extends Fragment {

    private ProgressDialog dialog;

    private ArrayList<BlockItem> blockItems;
    private ArrayList<UnitItem> unitItems;
    private ArrayList<Unit> unitList;
    private ArrayList<Integer> globalFavourites;
    private ListView lv_units;

    View rootView;
    private String[] recommendedList;

    public static MenuItem mMenuItem2;

    private Gson gson;
    private String response;


    public RecommendedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        lv_units = (ListView) rootView.findViewById(R.id.lv_units);
//        tvNoHistory = (TextView) rootView.findViewById(R.id.tv_nohistory);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        new loadData().execute();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_recommended));
    }

    private class loadData extends AsyncTask<Void, Integer, Object> {
        @Override
        protected void onPreExecute() {
            dialog.show();
            gson = new Gson();
            blockItems = new ArrayList<BlockItem>();
            unitItems = new ArrayList<UnitItem>();

        }

        @Override
        protected void onPostExecute(Object o) {
            FavouriteAdapter fa = new FavouriteAdapter(getActivity(), unitItems, blockItems, globalFavourites, rootView);
            lv_units.setAdapter(fa);
            lv_units.setOnItemClickListener(new unitItemClickListener());
            dialog.dismiss();

            lv_units.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    selectedUnit = unitList.get(position);
                    selectedBlockItem = blockItems.get(position);
                    selectedUnitItem = unitItems.get(position);
                    recommend =true;
                    getFragmentManager().beginTransaction().replace(R.id.fl_container, new UnitDetailsFragment()).addToBackStack("UnitDetailsFragment").commit();

                }
            });
        }

        @Override
        protected Object doInBackground(Void... params) {
            ArrayList<Integer> favourites = gson.fromJson(Utility.readFromFile("favourites", getActivity()), new TypeToken<List<Integer>>() {
            }.getType());

            globalFavourites = favourites;

            if(favourites==null){
                favourites = new ArrayList<Integer>();
            }
            //send in the favourites list into the post request
            unitList = gson.fromJson(Utility.postRequest("Unit/GetRecommendedUnits", gson.toJson(favourites)),
                    new TypeToken<List<Unit>>(){}.getType());

            Unit unit = new Unit();
            for(int i=0; i<unitList.size();i++){
                UnitItem item = new UnitItem();
                BlockItem blockItem = new BlockItem();
                Project project = new Project();
                Block block = new Block();
                UnitType unitType = new UnitType();
                unit = unitList.get(i);
                unitType = unit.getUnitType();

                block = unitType.getBlock();

                item.setPrice(unit.getPrice());
                item.setUnitNo(unit.getUnitNo());
                item.setUnitType(unit.getUnitType());
                item.setUnitId(unit.getUnitId());

                blockItem.setProjectName(block.getProject().getProjectName());
                blockItem.setStreet(block.getStreet());
                blockItem.setBlockNo(block.getBlockNo());
                blockItem.setIcon(block.getProject().getProjectImage());

                unitItems.add(item);
                blockItems.add(blockItem);
            }
            return null;
        }
    }

    private class unitItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedUnit = unitList.get(position);
            selectedBlockItem = blockItems.get(position);
            selectedUnitItem = unitItems.get(position);
            getFragmentManager().beginTransaction().replace(R.id.fl_container, new UnitDetailsFragment()).addToBackStack("UnitDetailsFragment").commit(); //click to floor page
        }
    }

}
