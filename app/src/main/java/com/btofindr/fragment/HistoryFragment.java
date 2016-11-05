package com.btofindr.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.adapter.HistoryAdapter;
import com.btofindr.controller.Utility;
import com.btofindr.model.Block;
import com.btofindr.model.BlockItem;
import com.btofindr.model.Unit;
import com.btofindr.model.UnitItem;
import com.btofindr.model.UnitType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.btofindr.fragment.SearchResultFragment.selectedBlock;

/**
 * Created by Shi Qi on 10/12/2016.
 */


/**
 * Fragment class for History page in the application
 */
public class HistoryFragment extends Fragment {
    private ArrayList<Integer> globalHistory;
    private ProgressDialog dialog;
    private ArrayList<BlockItem> blockItems;
    private ListView lvBlocks;
    private ArrayList<Block> blockList;
    private FloatingActionButton deleteBtn;
    private TextView tvNoHistory;
    public static MenuItem mMenuItem2;
    private Gson gson;
    private String response;
    private ArrayList<UnitItem> unitItems;
    public static int editModeHistory;
    public static ArrayList<Integer> selectedHistoryCheckbox;
    public static ArrayList<Integer> selectedHistoryCheckboxPosition;
    public static boolean noHistory = true;
    HistoryAdapter ha;
    View rootView;
    FragmentManager manager;
    FragmentTransaction ft;

    /**
     * Constructor for the HistoryFragment Class
     */
    public HistoryFragment() {
    }

    /**
     * Inflate view, link with XML.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        lvBlocks = (ListView) rootView.findViewById(R.id.lv_blocks);
        tvNoHistory = (TextView) rootView.findViewById(R.id.tv_nohistory);
        deleteBtn = (FloatingActionButton) rootView.findViewById(R.id.FAB);
        editModeHistory = 0;
        setHasOptionsMenu(true);
        manager = getActivity().getSupportFragmentManager();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        new loadData().execute();

        return rootView;
    }

    /**
     * For setting menu items
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(!noHistory){
            menu.getItem(0).setVisible(true);
        }
        else{
            menu.getItem(0).setVisible(false);

        }
    }

    /**
     * On selecting edit icon
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mMenuItem2 =  item;
        switch (item.getItemId()) {
            case R.id.action_edit:
                if(lvBlocks.getChoiceMode()==ListView.CHOICE_MODE_NONE){
                    lvBlocks.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    selectedHistoryCheckbox = new ArrayList<Integer>();
                    selectedHistoryCheckboxPosition = new ArrayList<Integer>();
                    deleteBtn.setVisibility(VISIBLE);
                }
                else{
                    lvBlocks.setChoiceMode(ListView.CHOICE_MODE_NONE);
                    deleteBtn.setVisibility(View.INVISIBLE);
                }
                editModeHistory = lvBlocks.getChoiceMode();
                lvBlocks.setAdapter(ha);
                return false;
            default:
                break;
        }
        return false;
    }

    /**
     * Method to resume the activity and set the action bar title to "HISTORY"
     */
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_history));
    }

    /**
     * AsyncTask Class to load the data for the list view and page
     */
    private class loadData extends AsyncTask<Void, Integer, Object> {
        /**
         * Method to initialize variables used to store the data retrieved from the web service
         */
        @Override
        protected void onPreExecute() {
            dialog.show();
            gson = new Gson();
            blockList = new ArrayList<Block>();
            blockItems = new ArrayList<BlockItem>();
            unitItems = new ArrayList<UnitItem>();
        }

        /**
         * Method to set the adapter to the list view and load the data into the list
         * @param o
         */
        @Override
        protected void onPostExecute(Object o) {
            ha = new HistoryAdapter(getActivity(), blockItems, globalHistory,rootView);

            lvBlocks.setOnItemClickListener(new blockItemClickListener());

            /**
             * Swipe listener (For deleting single items)
             */
            final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                    new SwipeToDismissTouchListener<>(
                            new ListViewAdapter(lvBlocks),
                            new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                                @Override
                                public boolean canDismiss(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismiss(ListViewAdapter view, int position) {
                                    int blockId = blockItems.get(position).getBlockId();

                                    ha.remove(blockId,2);
                                    if(ha.getCount()==0){
                                        setHasOptionsMenu(false);
                                        tvNoHistory.setVisibility(VISIBLE);
                                    }

                                    globalHistory.remove((Integer)blockId);

                                    if(Utility.writeToFile("history", gson.toJson(globalHistory), getActivity())){
                                        deleteBtn.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getActivity(), "Removed from History", Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), "Error deleting from History. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                    ha.notifyDataSetChanged();
                                }
                            });

            lvBlocks.setOnTouchListener(touchListener);
            lvBlocks.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
            lvBlocks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (touchListener.existPendingDismisses()) {
                        touchListener.undoPendingDismiss();

                    } else {
                        selectedBlock = blockList.get(position);
                        getFragmentManager().beginTransaction().replace(R.id.fl_container, new BlockFragment()).addToBackStack("BlockFragment").commit();
                    }
                }
            });

            if(ha.getCount()>=1) {
                lvBlocks.setAdapter(ha);
            }
            else{
                tvNoHistory.setVisibility(VISIBLE);
            }
            dialog.dismiss();
        }

        /**
         * Get item from history cache
         * @param params
         * @return
         */
        @Override
        protected Object doInBackground(Void... params) {
            ArrayList<Integer> history = gson.fromJson(Utility.readFromFile("history", getActivity()), new TypeToken<List<Integer>>() {
            }.getType());

            if(history==null){
                history = new ArrayList<Integer>();
            }

            globalHistory = history;
            for(int i=(history.size()-1); i>=0;i--){
                BlockItem blockItem = new BlockItem();
                UnitType unitType = new UnitType();
                response = Utility.getRequest("Block/GetBlockWithUnits?blockId="+history.get(i));
                Block b = gson.fromJson(response,Block.class);
                blockList.add(b);
                blockItem.setBlockId(b.getBlockId());
                blockItem.setProjectName(b.getProject().getProjectName());
                blockItem.setStreet(b.getStreet());
                blockItem.setBlockNo(b.getBlockNo());
                blockItem.setIcon(b.getProject().getProjectImage());
                blockItem.setMaxPrice(b.getMaxPrice());
                blockItem.setMinPrice(b.getMinPrice());
                blockItem.setUnitTypes(b.getUnitTypes());
                blockItems.add(blockItem);
            }
            if(blockList.isEmpty()&&!noHistory){
                //getFragmentManager().beginTransaction().replace(R.id.fl_container, new HistoryFragment()).addToBackStack("FavouriteFragment").commit();
                HistoryFragment historyFragment = new HistoryFragment();

                ft = manager.beginTransaction();
                //FragmentTransaction ft  = getFragmentManager().beginTransaction();
                ft.replace(R.id.fl_container, historyFragment);
                //ft.addToBackStack("HistoryFragment");
                ft.commit();

                noHistory = true;
            }else if(!blockList.isEmpty()&&noHistory){
                noHistory = false;
                //getFragmentManager().beginTransaction().replace(R.id.fl_container, new HistoryFragment()).addToBackStack("FavouriteFragment").commit();
                HistoryFragment historyFragment = new HistoryFragment();
                ft = manager.beginTransaction();
                //FragmentTransaction ft  = getFragmentManager().beginTransaction();
                ft.replace(R.id.fl_container, historyFragment);
                //ft.addToBackStack("HistoryFragment");
                ft.commit();
            }
            return blockList;
        }
    }

    /**
     * Listener for block item
     */
    private class blockItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedBlock = blockList.get(position);
            //getFragmentManager().beginTransaction().replace(R.id.fl_container, new BlockFragment()).addToBackStack("BlockFragment").commit();
            BlockFragment blockFragment = new BlockFragment();
            ft = manager.beginTransaction();
            //FragmentTransaction ft  = getFragmentManager().beginTransaction();
            ft.replace(R.id.fl_container, blockFragment);
            //ft.addToBackStack("HistoryFragment");
            ft.commit();
        }
    }

}
