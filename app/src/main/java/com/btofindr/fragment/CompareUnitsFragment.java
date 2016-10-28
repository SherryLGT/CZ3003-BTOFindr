package com.btofindr.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.adapter.CompareAdapter;
import com.btofindr.controller.Utility;
import com.btofindr.model.Block;
import com.btofindr.model.BlockItem;
import com.btofindr.model.Project;
import com.btofindr.model.Unit;
import com.btofindr.model.UnitItem;
import com.btofindr.model.UnitType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.btofindr.fragment.CompareResultsFragment.compareBlockItems;
import static com.btofindr.fragment.CompareResultsFragment.compareUnitItems;


public class CompareUnitsFragment extends Fragment {

    private ProgressDialog dialog;
    private ArrayList<BlockItem> blockItems;

    private ListView lvUnits;
    private Button btnCompare;

    private ArrayList<Integer> globalFavourites;
    private ArrayList<Unit> unitList;

    private Gson gson;
    private String response;
    private ArrayList<UnitItem> unitItems;

    public static ArrayList<UnitItem> selectedUnitList;
    public static ArrayList<BlockItem> blockList;

    public static boolean noFav = true;
    View rootView;

    public CompareUnitsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_compare, container, false);
        lvUnits = (ListView) rootView.findViewById(R.id.lv_units);

        btnCompare = (Button) rootView.findViewById(R.id.btn_compare);


        dialog = new ProgressDialog(this.getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        new loadData().execute();

        btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedUnitList.size()!=2){
                    Toast.makeText(getContext(), "Please select two units to compare", Toast.LENGTH_LONG).show();

                }else{
                    compareUnitItems = selectedUnitList;
                    compareBlockItems = blockList;
                    getFragmentManager().beginTransaction().replace(R.id.fl_container, new CompareResultsFragment()).addToBackStack("CompareResultsFragment").commit();
                    ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_compare_units_results));
                }
            }
        });

        return rootView;


    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_compare_units));
    }

    private class loadData extends AsyncTask<Void, Integer, Object> {
        @Override
        protected void onPreExecute() {
           dialog.show();
            gson = new Gson();
            unitList = new ArrayList<Unit>();
            blockItems = new ArrayList<BlockItem>();
            unitItems = new ArrayList<UnitItem>();
        }

        @Override
        protected void onPostExecute(Object o) {
            dialog.hide();
            CompareAdapter adapter = new CompareAdapter(getActivity(), unitItems, blockItems, globalFavourites, rootView);
            lvUnits.setAdapter(adapter);
            lvUnits.setClickable(false);

            selectedUnitList = new ArrayList<UnitItem>();
            blockList = new ArrayList<BlockItem>();

        }

        @Override
        protected Object doInBackground(Void... params) {

            ArrayList<Integer> favourites = gson.fromJson(Utility.readFromFile("favourites", getActivity()), new TypeToken<List<Integer>>() {
            }.getType());

            if(favourites==null){
                favourites = new ArrayList<Integer>();
            }

            globalFavourites = favourites;
            Unit unit = new Unit();
            for(int i=0; i<favourites.size();i++){
                UnitItem item = new UnitItem();
                BlockItem blockItem = new BlockItem();
                Project project = new Project();
                Block block = new Block();
                UnitType unitType = new UnitType();
                response = Utility.getRequest("Unit/GetUnit?unitId="+favourites.get(i));
                unit = gson.fromJson(response,Unit.class);

                unitList.add(unit);

                unitType = unit.getUnitType();

                block = unitType.getBlock();

                item.setPrice(unit.getPrice());
                item.setUnitId(unit.getUnitId());
                item.setUnitNo(unit.getUnitNo());
                item.setUnitType(unit.getUnitType());
                item.setUnitId(unit.getUnitId());

                blockItem.setProjectName(block.getProject().getProjectName());
                blockItem.setBlockId(block.getBlockId());
                blockItem.setStreet(block.getStreet());
                blockItem.setBlockNo(block.getBlockNo());
                blockItem.setIcon(block.getProject().getProjectImage());

                unitItems.add(item);
                blockItems.add(blockItem);
            }

            return null;
        }
    }

}
