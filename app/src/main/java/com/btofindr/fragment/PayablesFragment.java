package com.btofindr.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.controller.Utility;
import com.btofindr.model.Profile;
import com.btofindr.model.Unit;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.w3c.dom.Text;


public class PayablesFragment extends Fragment {

    private TextView tvUnitNumber, tvUnitType, tvPrice, tvApplicationFee, tvBookingFee, tvPaymentCPF, tvPaymentCash,
        tvStampDutyCPF, tvStampDutyCash, tvBalanceCPF, tvBalanceCash;
    private Profile profile;
    private Gson gson = new Gson();
    private String response;

    private Unit unit;

    public PayablesFragment() {
        // Required empty public constructor
    }

    public static PayablesFragment newInstance(int selectedUnitID){
        PayablesFragment pf = new PayablesFragment();
        Bundle bd = new Bundle(1);
        bd.putInt("selectedUnitID", selectedUnitID);
        pf.setArguments(bd);
        return pf;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payables, container, false);
        String para="";

        ((MainActivity) getContext()).setActionBarTitle("CALCULATE PAYABLES");
        Bundle args = getArguments();
        int selectedUnitID = args.getInt("selectedUnitID", 0);

        tvUnitNumber = (TextView) rootView.findViewById(R.id.unit_number_value);
        tvUnitType = (TextView) rootView.findViewById(R.id.unit_type_value);
        tvPrice = (TextView) rootView.findViewById(R.id.unit_price_value);
        tvApplicationFee = (TextView) rootView.findViewById(R.id.application_fee_value);
        tvBookingFee = (TextView) rootView.findViewById(R.id.booking_fee_value);
        tvPaymentCPF = (TextView) rootView.findViewById(R.id.by_cpf_value);
        tvPaymentCash = (TextView) rootView.findViewById(R.id.by_cash_value);
        tvStampDutyCPF = (TextView) rootView.findViewById(R.id.stamp_duty_by_cpf_value);
        tvStampDutyCash = (TextView) rootView.findViewById(R.id.stamp_duty_by_cash_value);
        tvBalanceCPF = (TextView) rootView.findViewById(R.id.balance_cpf_value);
        tvBalanceCash = (TextView) rootView.findViewById(R.id.balance_cash_value);

        profile = gson.fromJson(Utility.readFromFile("profile", this.getContext()), Profile.class);

        new loadData().execute();


        return rootView;
    }
    private class loadData extends AsyncTask<Void, Integer, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*Log.d("Hi", "Download Commencing");

            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Downloading Database...");


            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected String doInBackground(Void... params) {
            Bundle args = getArguments();
            int selectedUnitID = args.getInt("selectedUnitID", 0);
            response = Utility.postRequest("Unit/GetUnitWithPayables?unitId=" + selectedUnitID,
                    gson.toJson(profile));
            unit = gson.fromJson( response,Unit.class);

            if(response == null)
                Log.d("Payables", "null");

            return "Executed!";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            if(profile == null) {
                tvUnitNumber.setText("-");
                tvUnitType.setText("-");
                tvPrice.setText("-");
                tvApplicationFee.setText("$0.00");
                tvBookingFee.setText("$0.00");
                tvPaymentCPF.setText("$0.00");
                tvPaymentCash.setText("$0.00");
                tvStampDutyCPF.setText("$0.00");
                tvStampDutyCash.setText("$0.00");
                tvBalanceCPF.setText("$0.00");
                tvBalanceCash.setText("$0.00");
                final AlertDialog.Builder adb = new AlertDialog.Builder(getContext());

                adb.setTitle("You do not have a profile set-up yet, set up profile now?");

                //adb.setIcon(android.R.drawable.ic_dialog_alert);

                adb.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {

                        ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,
                                new ProfileFragment()).addToBackStack("ProfileFragment").commit();
                        ((MainActivity) getContext()).setActionBarTitle("PROFILE");
                        dialog.dismiss();

                    } });


                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    } });
                adb.show();
            }
            else {
                tvUnitNumber.setText(unit.getUnitNo());
                tvUnitType.setText(unit.getUnitType().getUnitTypeName());
                tvPrice.setText(Utility.formatPrice(unit.getPrice()));
                tvApplicationFee.setText("$" + Utility.formatPrice(unit.getFees().getApplFee()));
                tvBookingFee.setText("$" +Utility.formatPrice(unit.getFees().getOptionFee()));
                tvPaymentCPF.setText("$" +Utility.formatPrice(unit.getFees().getSigningFeesCpf()));
                tvPaymentCash.setText("$" +Utility.formatPrice(unit.getFees().getSigningFeesCash()));
                tvStampDutyCPF.setText("$" +Utility.formatPrice(unit.getFees().getCollectionFeesCpf()));
                tvStampDutyCash.setText("$" +Utility.formatPrice(unit.getFees().getCollectionFeesCash()));
                tvBalanceCPF.setText("$" +Utility.formatPrice(unit.getFees().getMonthlyCpf()));
                tvBalanceCash.setText("$" +Utility.formatPrice(unit.getFees().getMonthlyCash()));

            }
        }
    }



}
