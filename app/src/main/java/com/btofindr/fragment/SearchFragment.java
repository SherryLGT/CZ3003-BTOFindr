package com.btofindr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.btofindr.R;
import com.btofindr.controller.ProjectController;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import org.w3c.dom.Text;

public class SearchFragment extends Fragment {

    LinearLayout llTownLeft, llTownRight, llRoomLeft, llRoomRight;
    Spinner spinEthic;
    CrystalRangeSeekbar sbPriceRange;
    TextView tvPriceRange;
    Button btnSearch;

    String[] townNames, roomTypes;
    char ethic;
    int minxPrice, maxPrice;

    public SearchFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        llTownLeft = (LinearLayout) rootView.findViewById(R.id.ll_town_left);
        llTownRight = (LinearLayout) rootView.findViewById(R.id.ll_town_right);
        llRoomLeft = (LinearLayout) rootView.findViewById(R.id.ll_room_left);
        llRoomRight = (LinearLayout) rootView.findViewById(R.id.ll_room_right);
        spinEthic = (Spinner) rootView.findViewById(R.id.spin_ethic);
        sbPriceRange = (CrystalRangeSeekbar) rootView.findViewById(R.id.sb_price_range);
        tvPriceRange = (TextView) rootView.findViewById(R.id.tv_price_range);
        btnSearch = (Button) rootView.findViewById(R.id.btn_search);

        ProjectController projectCtrl = new ProjectController(this, llTownLeft, llTownRight, llRoomLeft, llRoomRight);
        projectCtrl.execute();

        sbPriceRange.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvPriceRange.setText("SGD$" + String.format("%,d", minValue) + " - SGD$" + String.format("%,d", maxValue));
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ethic = setEthic(spinEthic.getSelectedItem().toString());
                minxPrice = sbPriceRange.getSelectedMinValue().intValue();
                minxPrice = sbPriceRange.getSelectedMaxValue().intValue();
            }
        });

        return rootView;
    }

    public char setEthic(String ethic) {
        char character = '-';

        switch (ethic) {
            case "Chinese" :
                character =  'c';
                break;
            case "Malay" :
                character = 'M';
                break;
            case "Indian/Others" :
                character = 'O';
                break;
            default :
                break;
        }

        return character;
    }
}