package com.btofindr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.btofindr.R;

public class SearchFragment extends Fragment {

    public SearchFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

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