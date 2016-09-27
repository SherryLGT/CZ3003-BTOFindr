package com.btofindr.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.btofindr.R;
import com.btofindr.fragment.SearchFragment;
import com.btofindr.model.Project;
import com.btofindr.model.UnitType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProjectController extends AsyncTask<Object, Object, Object> implements Utility {

    ProgressDialog dialog;
    SearchFragment fragment;
    Context context;
    LinearLayout llTownLeft, llTownRight, llRoomLeft, llRoomRight;
    Button btn;

    boolean odd;
    float scale;
    String[] data;
    ArrayList<Project> projectList;
    ArrayList<UnitType> unitTypeList;
    Project project;
    UnitType unitType;

    public ProjectController(SearchFragment fragment, LinearLayout llTownLeft, LinearLayout llTownRight, LinearLayout llRoomLeft, LinearLayout llRoomRight) {
        this.fragment = fragment;
        this.llTownLeft = llTownLeft;
        this.llTownRight = llTownRight;
        this.llRoomLeft = llRoomLeft;
        this.llRoomRight = llRoomRight;
    }

    @Override
    protected void onPreExecute() {
        context = fragment.getContext();
        odd = true;
        scale = fragment.getActivity().getResources().getDisplayMetrics().density;
        dialog = ProgressDialog.show(context, null, "Loading...", true);

        projectList = new ArrayList<Project>();
        unitTypeList = new ArrayList<UnitType>();
    }

    @Override
    protected void onPostExecute(Object o) {
        dialog.dismiss();

        for(Project project : projectList) {
            final Button btn = generateButton();
            btn.setText(project.getTownName());
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!btn.isSelected()) {
                        btn.setSelected(true);
                    }
                    else {
                        btn.setSelected(false);
                    }
                }
            });
            if(odd) {
                llTownLeft.addView(btn);
                odd = false;
            }
            else {
                llTownRight.addView(btn);
                odd = true;
            }
        }

        odd = true;
        for(UnitType unitType : unitTypeList) {
            final Button btn = generateButton();
            btn.setText(unitType.getUnitTypeName());
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!btn.isSelected()) {
                        btn.setSelected(true);
                    }
                    else {
                        btn.setSelected(false);
                    }
                }
            });
            if(odd) {
                llRoomLeft.addView(btn);
                odd = false;
            }
            else {
                llRoomRight.addView(btn);
                odd = true;
            }
        }
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Object doInBackground(Object... params) {
        data = getRequest("Project/GetTownNames");
        for(String townName : data) {
            project = new Project();
            project.setTownName(townName);
            projectList.add(project);
        }

        data = getRequest("UnitType/GetUnitTypes");
        for(String type : data) {
            unitType = new UnitType();
            unitType.setUnitTypeName(type);
            unitTypeList.add(unitType);
        }

        return null;
    }

    public String[] getRequest(String getURL) {
        try {
            URL url = new URL(API_URL + getURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                Gson gson = new GsonBuilder().create();
                String[] data = gson.fromJson(response.toString(), String[].class);
                return data;
            } finally {
                urlConnection.disconnect();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Button generateButton() {
        btn = new Button(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getPixels(35));
        params.setMargins(getPixels(3), getPixels(5), getPixels(3), getPixels(5));
        btn.setPadding(getPixels(3), getPixels(3), getPixels(3), getPixels(3));
        btn.setLayoutParams(params);
        btn.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_button));
        btn.setTextColor(ContextCompat.getColorStateList(context, R.color.text_selector));

        return btn;
    }

    public void onClickListener(Button btn) {
        btn.setPressed(true);
    }

    // dp to pixel
    public int getPixels(int dp) {
        return ((int) (dp * scale + 0.5f));
    }
}