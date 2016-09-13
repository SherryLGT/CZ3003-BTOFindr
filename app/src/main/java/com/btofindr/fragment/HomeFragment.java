package com.btofindr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.btofindr.R;

public class HomeFragment extends Fragment {

    private ImageView ivMain;
    private TextView tvMain;
    private Button btnSearch;
    private ListView lvRecentlyViewed;

    public HomeFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ivMain = (ImageView) rootView.findViewById(R.id.iv_main);
        tvMain = (TextView) rootView.findViewById(R.id.tv_main);
        btnSearch = (Button) rootView.findViewById(R.id.btn_search);
        lvRecentlyViewed = (ListView) rootView.findViewById(R.id.lv_recently_viewed);

        ivMain.setImageResource(R.drawable.main);
        tvMain.setText("Tampines North Height");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fl_container, new SearchFragment()).commit();
            }
        });

        return rootView;
    }
}
