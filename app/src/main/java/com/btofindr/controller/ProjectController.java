package com.btofindr.controller;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

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

    String[] data;
    ArrayList<Project> projectList;
    ArrayList<UnitType> unitTypeList;
    Project project;
    UnitType unitType;

    SearchFragment fragment;
    GridLayout townGridLayout;
    GridLayout roomTypeGridLayout;

    public ProjectController(SearchFragment fragment, GridLayout townGridLayout, GridLayout roomTypeGridLayout) {
        this.fragment = fragment;
        this.townGridLayout = townGridLayout;
        this.roomTypeGridLayout = roomTypeGridLayout;
    }

    @Override
    protected void onPreExecute() {
        projectList = new ArrayList<Project>();
        unitTypeList = new ArrayList<UnitType>();
    }

    @Override
    protected void onPostExecute(Object o) {
        for(Project proj : projectList) {
            Button btn = new Button(fragment.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, (int) fragment.getActivity().getResources().getDimension(R.dimen.round_button_height), 1.0f);
            btn.setLayoutParams(params);
            btn.setBackground(ContextCompat.getDrawable(fragment.getContext(), R.drawable.rounded_button));
            btn.setTextColor(ContextCompat.getColorStateList(fragment.getContext(), R.color.text_selector));
            btn.setText(proj.getTownName());
            townGridLayout.addView(btn);
        }

        for(UnitType uType : unitTypeList) {
            Button btn = new Button(fragment.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) fragment.getActivity().getResources().getDimension(R.dimen.round_button_height), 1.0f);
            btn.setLayoutParams(params);
            btn.setBackground(ContextCompat.getDrawable(fragment.getContext(), R.drawable.rounded_button));
            btn.setTextColor(ContextCompat.getColorStateList(fragment.getContext(), R.color.text_selector));
            btn.setText(uType.getUnitTypeName());
            roomTypeGridLayout.addView(btn);
        }

        cancel(true);
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
}