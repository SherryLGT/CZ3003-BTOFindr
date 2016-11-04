package com.btofindr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.adapter.UnitAdapter;
import com.btofindr.controller.Utility;
import com.btofindr.model.Floor;
import com.btofindr.model.Unit;
import com.btofindr.model.UnitItem;
import com.btofindr.model.UnitType;

import java.util.ArrayList;

import static com.btofindr.activity.MainActivity.scale;
import static com.btofindr.fragment.BlockFragment.block;
import static com.btofindr.fragment.BlockFragment.selectedFloor;
import static com.btofindr.fragment.BlockFragment.selectedUnitType;

/**
 * This fragment is for displaying of units in a floor of a block.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 06/10/2016
 */

public class FloorFragment extends Fragment {

    private TextView tvHeader;
    private ListView lvUnits;

    private ArrayList<Floor> floorItems;
    private ArrayList<UnitItem> unitItems;

    /**
     * Default constructor for FloorFragment
     */
    public FloorFragment() {}

    /**
     * Create a View to display contents on the layout.
     *
     * @param inflater The LayoutInflater object that is used to inflate any view
     * @param container The parent view that fragment UI is attached to
     * @param savedInstanceState Previous state of the fragment
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_floor, container, false);

        tvHeader = (TextView) rootView.findViewById(R.id.tv_header);
        lvUnits = (ListView) rootView.findViewById(R.id.lv_units);

        floorItems = new ArrayList<Floor>();
        unitItems = new ArrayList<UnitItem>();
        // Get relevant information for display of units in a floor of a block
        for(UnitType unitType : block.getUnitTypes()) {
            if(unitType.getUnitTypeName().equals(selectedUnitType)) {
                for(Unit unit : unitType.getUnits()) {
                    Floor tempFloor = null;
                    for(Floor floor : floorItems)
                    {
                        if(floor.getFloor().equals(unit.getUnitNo().substring(0, 3))){
                            // If floor is available, use the same floor
                            tempFloor = floor;
                        }
                    }

                    if(tempFloor == null) {
                        // If floor is not available, add a new floor
                        tempFloor = new Floor(unit.getUnitNo().substring(0, 3));
                        floorItems.add(tempFloor);
                    }
                }
            }
        }

        // Set up units for display
        for(UnitType unitType : block.getUnitTypes()) {
            if (unitType.getUnitTypeName().equals(selectedUnitType)) {
                unitType.setBlock(block);
                for (Unit unit : unitType.getUnits()) {
                    if (unit.getUnitNo().substring(0, 3).equals(floorItems.get(selectedFloor).getFloor())) {
                        unitItems.add(new UnitItem(unit.getUnitId(), unit.getUnitNo(), unit.getPrice()));
                    }
                }
            }
        }
        lvUnits.setAdapter(new UnitAdapter(getContext(), unitItems));

        View divider = new View(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utility.getPixels(1, scale));
        params.setMargins(Utility.getPixels(10, scale), 0, 0, Utility.getPixels(10, scale));
        divider.setLayoutParams(params);
        divider.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray));

        tvHeader.setText((block.getProject().getProjectName() + " - " + selectedUnitType + " LEVEL " + floorItems.get(selectedFloor).getFloor()).toUpperCase());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_search_results));
    }
}
