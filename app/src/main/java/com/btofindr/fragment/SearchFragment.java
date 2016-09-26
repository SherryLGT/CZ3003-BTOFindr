package com.btofindr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.btofindr.R;
import com.btofindr.controller.ProjectController;

import org.w3c.dom.Text;

public class SearchFragment extends Fragment {

    GridLayout glTown;
    GridLayout glRoomType;
    Spinner spinEthic;
    TextView tvPriceRange;
    Button btnSearch;

    public SearchFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        GridLayout glTown = (GridLayout) rootView.findViewById(R.id.gl_town);
        GridLayout glRoomType = (GridLayout) rootView.findViewById(R.id.gl_room_type);
        Spinner spinEthic = (Spinner) rootView.findViewById(R.id.spin_ethic);
        TextView tvPriceRange = (TextView) rootView.findViewById(R.id.tv_price_range);
        Button btnSearch = (Button) rootView.findViewById(R.id.btn_search);

        ProjectController projectCtrl = new ProjectController(this, glTown, glRoomType);
        projectCtrl.execute();

//        Button btn = new Button(this.getContext());
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.round_button_height), 1.0f);
//        btn.setId();
//        btn.setLayoutParams(params);
//        btn.setBackground(getResources().getDrawable(R.drawable.rounded_button));
//        btn.setTextColor(getResources().getColorStateList(R.color.text_selector));
//        btn.setText();

        return rootView;
    }
}