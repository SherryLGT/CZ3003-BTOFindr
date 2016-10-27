package com.btofindr.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

public class RecommendedFragment extends Fragment {

    private ProgressDialog dialog;

//    private ArrayList<BlockItem> blockItems;
//    private ListView lvBlocks;
//    private ArrayList<Block> blockList;
//    private TextView tvNoHistory;

    private String[] recommendedList;


    public static MenuItem mMenuItem2;

    private Gson gson;
    private String response;
    private ArrayList<UnitItem> unitItems;

    public static boolean noHistory = true;
    HistoryAdapter ha;
    View rootView;

    public RecommendedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
//        lvBlocks = (ListView) rootView.findViewById(R.id.lv_blocks);
//        tvNoHistory = (TextView) rootView.findViewById(R.id.tv_nohistory);

        setHasOptionsMenu(true);

        new loadData().execute();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        return rootView;
    }


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mMenuItem2 =  item;

        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_recommended));
    }

    private class loadData extends AsyncTask<Void, Integer, Object> {
        @Override
        protected void onPreExecute() {
            //dialog.show();
            gson = new Gson();

        }

        @Override
        protected void onPostExecute(Object o) {
            //ha = new HistoryAdapter(getActivity(), blockItems, globalHistory,rootView);

            //lvBlocks.setOnItemClickListener(new blockItemClickListener());


           dialog.dismiss();
        }

        @Override
        protected Object doInBackground(Void... params) {
            ArrayList<Integer> favourites = gson.fromJson(Utility.readFromFile("favourites", getActivity()), new TypeToken<List<Integer>>() {
            }.getType());

            if(favourites==null){
                favourites = new ArrayList<Integer>();
            }
            //send in the favourites list into the post request
            ArrayList<Unit> units = gson.fromJson(Utility.postRequest("Unit/GetRecommendedUnits", gson.toJson(favourites)),
                    new TypeToken<List<Unit>>(){}.getType());
            units.size();
            return null;
        }
    }

    private class blockItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           // selectedBlock = blockList.get(position);
            getFragmentManager().beginTransaction().replace(R.id.fl_container, new BlockFragment()).addToBackStack("BlockFragment").commit();
        }
    }

}
