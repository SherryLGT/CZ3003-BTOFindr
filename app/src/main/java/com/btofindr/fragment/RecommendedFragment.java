package com.btofindr.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.adapter.RecommendedAdapter;
import com.btofindr.controller.Utility;
import com.btofindr.model.Block;
import com.btofindr.model.BlockItem;
import com.btofindr.model.Project;
import com.btofindr.model.Unit;
import com.btofindr.model.UnitItem;
import com.btofindr.model.UnitType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static android.view.View.VISIBLE;
import static com.btofindr.fragment.FavouriteFragment.selectedUnit;
import static com.btofindr.fragment.FavouriteFragment.selectedUnitItem;
import static com.btofindr.fragment.FavouriteFragment.selectedBlockItem;

import java.util.ArrayList;
import java.util.List;

import static com.btofindr.fragment.UnitDetailsFragment.favourite;
import static com.btofindr.fragment.UnitDetailsFragment.recommend;

/**
 * Fragment class for Recommended page in the application
 */
public class RecommendedFragment extends Fragment {
    private ProgressDialog dialog;

    private ArrayList<BlockItem> blockItems;
    private ArrayList<UnitItem> unitItems;
    private ArrayList<Unit> unitList;
    private ArrayList<Integer> favouriteList;
    private ListView lv_units;
    private TextView tv_nofave;

    View rootView;

    private Gson gson;

    /**
     * Constructor for the RecommendedFragment Class
     */
    public RecommendedFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        lv_units = (ListView) rootView.findViewById(R.id.lv_units);
        tv_nofave = (TextView) rootView.findViewById(R.id.tv_nofave);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        new loadData().execute();
        return rootView;
    }

    /**
     * Method to resume the activity and set the action bar title to "Recommended"
     */
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_recommended));
    }

    /**
     * AsyncTask Class to load the data for the list view and page
     */
    private class loadData extends AsyncTask<Void, Integer, Object> {
        /**
         * Method to initialize variables used to store the data retrieved from the web service
         * blockItems to store the BlockItems retrieved
         * unitItems to store the UnitItems retrieved
         * To show the progress dialog before loading of data
         */
        @Override
        protected void onPreExecute() {
            dialog.show();
            gson = new Gson();
            blockItems = new ArrayList<BlockItem>();
            unitItems = new ArrayList<UnitItem>();

        }

        /**
         * Method to set the adapter to the list view and load the data into the list
         * Setting on click listener to each list view items
         * Hiding the progress dialog after loading the data
         * Checking if there is any items in favourites
         *
         * @param o
         */
        @Override
        protected void onPostExecute(Object o) {

            dialog.dismiss();
            if (favouriteList.size() == 0) {
                tv_nofave.setText("NO RECOMMENDED");
                tv_nofave.setVisibility(VISIBLE);
            } else {
                RecommendedAdapter adapter = new RecommendedAdapter(getActivity(), unitItems, blockItems, rootView);
                lv_units.setAdapter(adapter);
                lv_units.setOnItemClickListener(new unitItemClickListener());
                lv_units.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        selectedUnit = unitList.get(position);
                        selectedBlockItem = blockItems.get(position);
                        selectedUnitItem = unitItems.get(position);
                        recommend = true;
                        favourite = false;
                        getFragmentManager().beginTransaction().replace(R.id.fl_container, new UnitDetailsFragment()).addToBackStack("UnitDetailsFragment").commit();

                    }
                });
            }
        }

        /**
         * Method to get the units based on the favourite list
         * Getting the unit items from the webservice by POST request and converting into an object
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
            if (favouriteList != null) {
                /**
                 * Retrieving the list of Unit from the web service using POST request
                 */
                unitList = gson.fromJson(Utility.postRequest("Unit/GetRecommendedUnits", gson.toJson(favouriteList)),
                        new TypeToken<List<Unit>>() {
                        }.getType());

                Unit unit = new Unit();
                for (int i = 0; i < unitList.size(); i++) {
                    UnitItem item = new UnitItem();
                    BlockItem blockItem = new BlockItem();

                    unit = unitList.get(i);
                    UnitType unitType = unit.getUnitType();
                    ;
                    Block block = unitType.getBlock();

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
            } else {
                favouriteList = new ArrayList<Integer>();
            }
            return null;
        }
    }

    /**
     * OnItemClickListener Class to handle the action when an item on a list view is being clicked
     */
    private class unitItemClickListener implements ListView.OnItemClickListener {
        /**
         * Method to handle the click action
         * To store the selected Unit into a global static variable
         * To store the selected UnitItem into a global static variable
         * To store the selected BlockItem into a global static variable
         * Transit to another page to show the details of the unit using UnitDetailsFragment
         *
         * @param parent
         * @param view
         * @param position
         * @param id
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedUnit = unitList.get(position);
            selectedBlockItem = blockItems.get(position);
            selectedUnitItem = unitItems.get(position);
            getFragmentManager().beginTransaction().replace(R.id.fl_container, new UnitDetailsFragment()).addToBackStack("UnitDetailsFragment").commit(); //click to floor page
        }
    }

}
