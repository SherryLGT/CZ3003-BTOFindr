package com.btofindr.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.btofindr.R;
import com.btofindr.controller.Utility;
import com.btofindr.model.Profile;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Sherry on 31/08/2016.
 */

public class ProfileFragment extends Fragment {

    private ProgressDialog dialog;
    private EditText etPostalCode, etAvgIncome, etCPFBal, etCPFCon, etLoanTenure;
    private Button btnSave;
    private LinearLayout llFlatTypes;
    private Profile profile;
    private String[] unitTypes;
    private ArrayList<String> selUnitTypes;
    private Gson gson = new Gson();

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        etPostalCode = (EditText) rootView.findViewById(R.id.et_postal_code);
        etAvgIncome = (EditText) rootView.findViewById(R.id.et_average_income);
        etCPFBal = (EditText) rootView.findViewById(R.id.et_cpf_balance);
        etCPFCon = (EditText) rootView.findViewById(R.id.et_cpf_contribution);
        etLoanTenure = (EditText) rootView.findViewById(R.id.et_loan_tenure);
        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        llFlatTypes = (LinearLayout) rootView.findViewById(R.id.ll_flat_types);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        selUnitTypes = new ArrayList<String>();
        new loadData().execute();

        profile = gson.fromJson(Utility.readFromFile("profile", this.getContext()), Profile.class);
        if (profile == null) {
            profile = new Profile();
            etAvgIncome.setSelectAllOnFocus(true);
            etCPFBal.setSelectAllOnFocus(true);
            etCPFCon.setSelectAllOnFocus(true);
            etLoanTenure.setSelectAllOnFocus(true);
        }

        etPostalCode.setText(profile.getPostalCode());
        etAvgIncome.setText(String.valueOf(profile.getIncome()));
        etCPFBal.setText(String.valueOf(profile.getCurrentCpf()));
        etCPFCon.setText(String.valueOf(profile.getMonthlyCpf()));
        etLoanTenure.setText(String.valueOf(profile.getLoanTenure()));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new saveData().execute();

                profile.setPostalCode(etPostalCode.getText().toString());
                profile.setIncome(Double.parseDouble(etAvgIncome.getText().toString()));
                profile.setCurrentCpf(Double.parseDouble(etCPFBal.getText().toString()));
                profile.setMonthlyCpf(Double.parseDouble(etCPFCon.getText().toString()));
                profile.setLoanTenure(Integer.parseInt(etLoanTenure.getText().toString()));

                if (Utility.writeToFile("profile", gson.toJson(profile), getContext())) {
                    Toast.makeText(ProfileFragment.this.getContext(), "Profile Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileFragment.this.getContext(), "Error Saving Profile. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    private class loadData extends AsyncTask<Void, Integer, Object> {
        @Override
        protected void onPreExecute() {
            dialog.show();

            gson = new Gson();
        }

        @Override
        protected void onPostExecute(Object o) {
            dialog.dismiss();

            for (String unitType : unitTypes) {
                final CheckBox checkBox = new CheckBox(getContext());
                checkBox.setText(unitType);

                if (selUnitTypes.contains(unitType))
                    checkBox.setChecked(true);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selUnitTypes.add(buttonView.getText().toString());
                        } else {
                            selUnitTypes.remove(buttonView.getText().toString());
                        }
                    }
                });
                llFlatTypes.addView(checkBox);
            }

        }

        @Override
        protected Object doInBackground(Void... params) {

            unitTypes = gson.fromJson(Utility.getRequest("UnitType/GetUnitTypes"), String[].class);

            String[] data = gson.fromJson(Utility.getRequest("UnitTypePublisher/GetSubscriptions?deviceId=" + FirebaseInstanceId.getInstance().getToken()), String[].class);
            for (String type : data) {
                selUnitTypes.add(type);
            }

            return null;
        }
    }

    private class saveData extends AsyncTask<Void, Integer, Object> {
        @Override
        protected void onPreExecute() {
            dialog.show();

            gson = new Gson();
        }

        @Override
        protected void onPostExecute(Object o) {
            dialog.dismiss();
        }

        @Override
        protected Object doInBackground(Void... params) {

            for (String unitType : unitTypes) {
                if (selUnitTypes.contains(unitType))
                    Utility.getRequest("UnitTypePublisher/Subscribe?unitTypeName=" + unitType + "&deviceId=" + FirebaseInstanceId.getInstance().getToken());
                else
                    Utility.getRequest("UnitTypePublisher/Unsubscribe?unitTypeName=" + unitType + "&deviceId=" + FirebaseInstanceId.getInstance().getToken());
            }

            return null;
        }
    }
}
