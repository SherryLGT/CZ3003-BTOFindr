package com.btofindr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.btofindr.R;

public class ProfileFragment extends Fragment {

    EditText etPostalCode, etAvgIncome, etCPFBal, etCPFCon, etLoanTenure;
    Button btnSave;

    public ProfileFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        etPostalCode = (EditText) rootView.findViewById(R.id.et_postal_code);
        etAvgIncome = (EditText) rootView.findViewById(R.id.et_average_income);
        etCPFBal = (EditText) rootView.findViewById(R.id.et_cpf_balance);
        etCPFCon = (EditText) rootView.findViewById(R.id.et_cpf_contribution);
        etLoanTenure = (EditText) rootView.findViewById(R.id.et_loan_tenure);
        btnSave = (Button) rootView.findViewById(R.id.btn_save);

        return rootView;
    }
}
