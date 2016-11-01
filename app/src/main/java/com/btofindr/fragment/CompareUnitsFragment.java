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
import android.widget.TextView;
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

import static com.btofindr.fragment.CompareUnitsResultFragment.compareBlockItems;
import static com.btofindr.fragment.CompareUnitsResultFragment.compareUnitItems;

/**
 * Fragment class for Compare Units page in the application
 */
public class CompareUnitsFragment extends Fragment {

    private ProgressDialog dialog;
    private ArrayList<BlockItem> blockItems;

    private ListView lvUnits;
    private TextView errmsg;
    private Button btnCompare;

    private ArrayList<Integer> favouriteList;
    private ArrayList<Unit> unitList;

    private Gson gson;
    private String response;
    private ArrayList<UnitItem> unitItems;

    public static ArrayList<UnitItem> selectedUnitList;
    public static ArrayList<BlockItem> blockList;

    public static boolean noFav = true;
    View rootView;

    /**
     * Constructor for the CompareUnitsFragment Class
     */
    public CompareUnitsFragment() {
    }


    /**
     * Method to Create the view for the page
     * Creating a progress dialog to show that data is loading
     * Loading the data into the view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return an View object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_compare, container, false);
        lvUnits = (ListView) rootView.findViewById(R.id.lv_units);
        errmsg = (TextView) rootView.findViewById(R.id.tv_err_msg);
        btnCompare = (Button) rootView.findViewById(R.id.btn_compare);

        dialog = new ProgressDialog(this.getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);


        btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedUnitList.size()!=2){
                    Toast.makeText(getContext(), "Please select two units to compare", Toast.LENGTH_LONG).show();

                }else{
                    compareUnitItems = selectedUnitList;
                    compareBlockItems = blockList;
                    getFragmentManager().beginTransaction().replace(R.id.fl_container, new CompareUnitsResultFragment()).addToBackStack("CompareUnitsResultFragment").commit();
                    ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_compare_units_results));
                }
            }
        });


        new loadData().execute();

        return rootView;


    }

    /**
     * Method to resume the activity and set the action bar title to "Compare units"
     */
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_compare_units));
    }

    /**
     * AsyncTask Class to load the data for the list view and page
     */
    private class loadData extends AsyncTask<Void, Integer, Object> {
        /**
         * Method to initialize variables used to store the data retrieved from the web service
         * blockItems to store the BlockItems retrieved
         * unitItems to store the UnitItems retrieved
         * unitList to store the Units retrieved
         * To show the progress dialog before loading of data
         */
        @Override
        protected void onPreExecute() {
            dialog.show();
            gson = new Gson();
            unitList = new ArrayList<Unit>();
            blockItems = new ArrayList<BlockItem>();
            unitItems = new ArrayList<UnitItem>();
        }

        /**
         * Method to set the adapter to the list view and load the data into the list
         * Setting on click listener to each list view items
         * Disabling the list view from being clickable
         * Hiding the progress dialog after loading the data
         * Checking if there is any items in favourites
         *
         * @param o
         */
        @Override
        protected void onPostExecute(Object o) {
            dialog.hide();

            selectedUnitList = new ArrayList<UnitItem>();
            blockList = new ArrayList<BlockItem>();

            if(favouriteList == null || favouriteList.size() == 0){
                errmsg.setVisibility(View.VISIBLE);
                lvUnits.setVisibility(View.INVISIBLE);
            }else {
                CompareAdapter adapter = new CompareAdapter(getActivity(), unitItems, blockItems, rootView);
                errmsg.setVisibility(View.INVISIBLE);

                lvUnits.setAdapter(adapter);
                lvUnits.setClickable(false);
            }

        }

        /**
         * Method to get the units based on the favourite list
         * Getting the unit from the webservice by GET request and converting into an object
         * Creating new UnitItem and BlockItem objects
         * Setting each UnitItem and BlockItem attributes accordingly
         * Checking if there is any favourited items before adding data into the list view
         *
         * @param params
         * @return
         */
        @Override
        protected Object doInBackground(Void... params) {

            favouriteList = gson.fromJson(Utility.readFromFile("favourites", getActivity()), new TypeToken<List<Integer>>() {
            }.getType());

            /**
             * If there is items in the favouriteList, then we will proceed to add in the data into the list view
             * Else we will then create a new empty ArrayList<>
             */
            if(favouriteList!=null){

                for (int i = 0; i < favouriteList.size(); i++) {
                    UnitItem item = new UnitItem();
                    BlockItem blockItem = new BlockItem();

                    Unit unit = gson.fromJson(Utility.getRequest("Unit/GetUnit?unitId=" + favouriteList.get(i)), Unit.class);

                    unitList.add(unit);
                    UnitType unitType = unit.getUnitType();
                    Block block = unitType.getBlock();

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
            }else {
                favouriteList = new ArrayList<Integer>();
            }

            return null;
        }
    }

}
