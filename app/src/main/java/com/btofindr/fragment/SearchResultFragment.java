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
import android.widget.Spinner;

import com.btofindr.R;
import com.btofindr.adapter.BlockAdapter;
import com.btofindr.controller.Utility;
import com.btofindr.model.Block;
import com.btofindr.model.BlockItem;
import com.btofindr.model.SearchParameter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sherry on 31/08/2016.
 */

public class SearchResultFragment extends Fragment {

    private ProgressDialog dialog;
    private ArrayList<BlockItem> blockItems;
    private Spinner spinSort;
    private ListView lvBlocks;

    private Gson gson;
    private String response;
    private String[] data;
    private SearchParameter parameter;
    private ArrayList<Block> blockList;
    public static Block selectedBlock;

    public SearchResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_result, container, false);

        spinSort = (Spinner) rootView.findViewById(R.id.spin_sort);
        lvBlocks = (ListView) rootView.findViewById(R.id.lv_blocks);

        parameter = SearchFragment.parameter;
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        spinSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        parameter.setOrderBy('P');
                        new loadData().execute();
                        break;
                    case 1:
                        parameter.setOrderBy('T');
                        new loadData().execute();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }

    private class loadData extends AsyncTask<Void, Integer, Object> {
        @Override
        protected void onPreExecute() {
            dialog.show();
            gson = new Gson();

            blockList = new ArrayList<Block>();
            blockItems = new ArrayList<BlockItem>();
        }

        @Override
        protected void onPostExecute(Object o) {
            lvBlocks.setAdapter(new BlockAdapter(getContext(), blockItems));
            lvBlocks.setOnItemClickListener(new blockItemClickListener());
            dialog.dismiss();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String string = gson.toJson(parameter);
            response = Utility.postRequest("Block/SearchBlocks", gson.toJson(parameter));
            blockList = gson.fromJson(response, new TypeToken<List<Block>>() {
            }.getType());
            for (Block block : blockList) {
                BlockItem item = new BlockItem();
                item.setIcon(block.getProject().getProjectImage());
                item.setProjectName(block.getProject().getProjectName());
                item.setBlockNo(block.getBlockNo());
                item.setStreet(block.getStreet());
                item.setUnitTypes(block.getUnitTypes());
                item.setMinPrice(block.getMinPrice());
                item.setMaxPrice(block.getMaxPrice());
                blockItems.add(item);
            }

            return blockList;
        }
    }

    private class blockItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedBlock = blockList.get(position);
            getFragmentManager().beginTransaction().replace(R.id.fl_container, new BlockFragment()).addToBackStack("BlockFragment").commit();
        }
    }
}
