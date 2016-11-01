package com.btofindr.fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.Menu;
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
import com.btofindr.adapter.FavouriteAdapter;
import com.btofindr.controller.Utility;
import com.btofindr.model.Block;
import com.btofindr.model.BlockItem;
import com.btofindr.model.Project;
import com.btofindr.model.SearchParameter;
import com.btofindr.model.UnitItem;
import com.btofindr.model.Unit;
import com.btofindr.model.UnitType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
import static android.view.View.VISIBLE;
import static com.btofindr.fragment.UnitDetailsFragment.recommend;

public class FavouriteFragment extends Fragment {
    private ArrayList<Integer> globalFavourites;
    private ProgressDialog dialog;
    private ArrayList<BlockItem> blockItems;
    private ListView lvUnits;
    private ArrayList<Unit> unitList;
    private FloatingActionButton deleteBtn;
    private TextView tvNoFav;
    public static MenuItem mMenuItem;
    private Gson gson;
    private String response;
    private ArrayList<UnitItem> unitItems;
    public static Unit selectedUnit;
    public static UnitItem selectedUnitItem;
    public static BlockItem selectedBlockItem;
    public static int editMode;
    public static ArrayList<Integer> selectedCheckbox;
    public static ArrayList<Integer> selectedCheckboxPosition;
    public static boolean noFav = true;
    FavouriteAdapter fa;
    View rootView;
    public FavouriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        lvUnits = (ListView) rootView.findViewById(R.id.lv_units);
        tvNoFav = (TextView) rootView.findViewById(R.id.tv_nofave);
        deleteBtn = (FloatingActionButton) rootView.findViewById(R.id.FAB);
        editMode = 0;
        setHasOptionsMenu(true);

        dialog = new ProgressDialog(this.getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        new loadData().execute();
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(!noFav){
            menu.getItem(0).setVisible(true);
        }
        else{
            menu.getItem(0).setVisible(false);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mMenuItem =  item;
        switch (item.getItemId()) {
            case R.id.action_edit:
                if(lvUnits.getChoiceMode()==ListView.CHOICE_MODE_NONE){
                    lvUnits.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    selectedCheckbox = new ArrayList<Integer>();
                    selectedCheckboxPosition = new ArrayList<Integer>();
                    deleteBtn.setVisibility(VISIBLE);
                }
                else{
                    lvUnits.setChoiceMode(ListView.CHOICE_MODE_NONE);
                    deleteBtn.setVisibility(View.INVISIBLE);
                }

                editMode = lvUnits.getChoiceMode();

                lvUnits.setAdapter(fa);

                return false;

            default:
                break;
        }

        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_favourites));
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
            fa = new FavouriteAdapter(getActivity(), unitItems, blockItems, globalFavourites, rootView);
            lvUnits.setOnItemClickListener(new unitItemClickListener());

            final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                    new SwipeToDismissTouchListener<>(
                            new ListViewAdapter(lvUnits),
                            new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                                @Override
                                public boolean canDismiss(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismiss(ListViewAdapter view, int position) {
                                    int unitID = unitItems.get(position).getUnitId();
                                    fa.remove(unitID,2);
                                    if(fa.getCount()==0){
                                        setHasOptionsMenu(false);
                                    }

                                    globalFavourites.remove((Integer)unitID);

                                    if(Utility.writeToFile("favourites", gson.toJson(globalFavourites), getActivity())){
                                        new removeFavouriteCount().execute(unitID);
                                        deleteBtn.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getActivity(), "Removed from Favourites", Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), "Error deleting from Favourites. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                    fa.notifyDataSetChanged();
                                }
                            });


            lvUnits.setOnTouchListener(touchListener);
            lvUnits.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
            lvUnits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (touchListener.existPendingDismisses()) {
                        touchListener.undoPendingDismiss();

                    } else {
                        selectedUnit = unitList.get(position);
                        selectedBlockItem = blockItems.get(position);
                        selectedUnitItem = unitItems.get(position);
                        recommend = false;
                        getFragmentManager().beginTransaction().replace(R.id.fl_container, new UnitDetailsFragment()).addToBackStack("UnitDetailsFragment").commit();
                    }
                }
            });

            if(!noFav) {
                lvUnits.setAdapter(fa);
            }
            else{
                tvNoFav.setVisibility(VISIBLE);
            }
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
            if(unitList.isEmpty()&&!noFav){
                getFragmentManager().beginTransaction().replace(R.id.fl_container, new FavouriteFragment()).addToBackStack("FavouriteFragment").commit();
                noFav = true;
            }else if(!unitList.isEmpty()&&noFav){
                noFav = false;
                getFragmentManager().beginTransaction().replace(R.id.fl_container, new FavouriteFragment()).addToBackStack("FavouriteFragment").commit();
            }

            return unitList;
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

    private class removeFavouriteCount extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            Utility.getRequest("Unit/RemoveFaveUnit?unitId="+params[0].toString());
            return null;
        }
    }
}
