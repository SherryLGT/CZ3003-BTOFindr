package com.btofindr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.btofindr.R;
import com.btofindr.controller.Utility;
import com.btofindr.model.Profile;
import com.google.gson.Gson;

/**
 * Created by Sherry on 31/08/2016.
 */

public class ProfileFragment extends Fragment {

    private EditText etPostalCode, etAvgIncome, etCPFBal, etCPFCon, etLoanTenure;
    private Button btnSave;
    private Profile profile;
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
                profile.setPostalCode(etPostalCode.getText().toString());
                profile.setIncome(Double.parseDouble(etAvgIncome.getText().toString()));
                profile.setCurrentCpf(Double.parseDouble(etCPFBal.getText().toString()));
                profile.setMonthlyCpf(Double.parseDouble(etCPFCon.getText().toString()));
                profile.setLoanTenure(Integer.parseInt(etLoanTenure.getText().toString()));

                if(Utility.writeToFile("profile", gson.toJson(profile), getContext()))
                {
                    Toast.makeText(ProfileFragment.this.getContext(), "Profile Saved", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ProfileFragment.this.getContext(), "Error Saving Profile. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
}
