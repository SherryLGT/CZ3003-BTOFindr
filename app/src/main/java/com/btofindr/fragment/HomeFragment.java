package com.btofindr.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.adapter.HistoryAdapter;
import com.btofindr.controller.Utility;
import com.btofindr.model.Block;
import com.btofindr.model.BlockItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.btofindr.fragment.SearchResultFragment.selectedBlock;

/**
 * This fragment is for home page of the program.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
 */

public class HomeFragment extends Fragment {

    private ImageView ivMain;
    private TextView tvMain;
    private Button btnSearch;
    private ProgressDialog dialog;
    private ListView lvRecentlyViewed;
    private ScrollView sv_recently_viewed;
    private HistoryAdapter ha;
    private Gson gson;
    private TextView tv_noRecentlyViewed;
    private ArrayList<BlockItem> blockItems;
    private ArrayList<Block> blockList;
    private ArrayList<Integer> recentlyViewed;
    public static boolean noHistory;
    private View rootView;

    /**
     * Default constructor for HomeFragment
     */
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create a View to display contents on the layout.
     *
     * @param inflater           The LayoutInflater object that is used to inflate any view
     * @param container          The parent view that fragment UI is attached to
     * @param savedInstanceState Previous state of the fragment
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ivMain = (ImageView) rootView.findViewById(R.id.iv_main);
        tvMain = (TextView) rootView.findViewById(R.id.tv_main);
        btnSearch = (Button) rootView.findViewById(R.id.btn_search);
        lvRecentlyViewed = (ListView) rootView.findViewById(R.id.lv_recently_viewed);
        tv_noRecentlyViewed = (TextView) rootView.findViewById(R.id.tv_norv);
        sv_recently_viewed = (ScrollView) rootView.findViewById(R.id.sv_recently_viewed);
        ivMain.setImageResource(R.drawable.main);
        tvMain.setText("Tampines North Height");
        setHasOptionsMenu(true);
        // Handles on click event on search button to navigate to search page
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fl_container, new SearchFragment()).addToBackStack("SearchFragment").commit();
                ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_search));
            }
        });

        dialog = new ProgressDialog(this.getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        new loadData().execute();
        return rootView;
    }


    private class loadData extends AsyncTask<Void, Integer, Object> {
        @Override
        protected void onPreExecute() {
            dialog.show();
            gson = new Gson();
            blockList = new ArrayList<Block>();
        }

        @Override
        protected void onPostExecute(Object o) {
            ha = new HistoryAdapter(getActivity(), blockItems, recentlyViewed, rootView);
            lvRecentlyViewed.setOnItemClickListener(new blockItemClickListener());

            final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                    new SwipeToDismissTouchListener<>(
                            new ListViewAdapter(lvRecentlyViewed),
                            new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                                @Override
                                public boolean canDismiss(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismiss(ListViewAdapter view, int position) {
                                    int blockId = blockItems.get(position).getBlockId();
                                    ha.remove(blockId, 2);
                                    recentlyViewed.remove((Integer) blockId);

                                    if (Utility.writeToFile("history", gson.toJson(recentlyViewed), getActivity())) {
                                        Toast.makeText(getContext(), "Removed from History", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getActivity(), "Error deleting from History. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                    ha.notifyDataSetChanged();
                                }
                            });


            //touchListener.setDismissDelay(TIME_TO_AUTOMATICALLY_DISMISS_ITEM);
            lvRecentlyViewed.setOnTouchListener(touchListener);
            lvRecentlyViewed.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
            lvRecentlyViewed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


            if (!noHistory) {
                lvRecentlyViewed.setAdapter(ha);
                sv_recently_viewed.setVisibility(VISIBLE);
            } else {
                tv_noRecentlyViewed.setVisibility(VISIBLE);
            }
            dialog.dismiss();
        }

        @Override
        protected Object doInBackground(Void... params) {

            ArrayList<Integer> history = gson.fromJson(Utility.readFromFile("history", getActivity()), new TypeToken<List<Integer>>() {
            }.getType());

            if (history == null) {
                history = new ArrayList<Integer>();
            }

            blockItems = new ArrayList<BlockItem>();
            recentlyViewed = history;
            for (int i = (history.size() - 1); i >= 0; i--) {
                BlockItem blockItem = new BlockItem();
                String response = Utility.getRequest("Block/GetBlockWithUnits?blockId=" + history.get(i));
                Block b = gson.fromJson(response, Block.class);
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

            if (blockList.isEmpty() && !noHistory) {
                // getFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment()).commit();
                // getFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment()).addToBackStack("FavouriteFragment").commit();
                noHistory = true;
            } else if (!blockList.isEmpty() && noHistory) {
                noHistory = false;
                // getFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment()).commit();
                // getFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment()).addToBackStack("FavouriteFragment").commit();
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
