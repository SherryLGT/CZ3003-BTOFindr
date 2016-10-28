package com.btofindr.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.controller.Utility;
import com.btofindr.model.Block;
import com.btofindr.model.BlockItem;
import com.btofindr.model.Profile;
import com.btofindr.model.Unit;
import com.btofindr.model.UnitItem;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;


import static com.btofindr.fragment.BlockFragment.block;
import static com.btofindr.fragment.BlockFragment.selectedView;


/**
 * Created by Amandaa on 31/08/2016.
 */

public class CompareResultsFragment extends Fragment {

    private ProgressDialog dialog;
    private ImageView ivProjectImage;
    private TextView tvTitle, tvAddress;

    private Button btnViewMapPlan;
    private Button btnTab1;
    private Button btnTab2;

    private TextView tvUnitNumber, tvUnitType, tvPrice, tvApplicationFee, tvBookingFee, tvPaymentCPF, tvPaymentCash,
            tvStampDutyCPF, tvStampDutyCash, tvBalanceCPF, tvBalanceCash, tvBalanceTitle;
    private Profile profile;

    private Gson gson = new Gson();

    public static ArrayList<Bitmap> bitmap;
    public static ArrayList<UnitItem> compareUnitItems = new ArrayList<UnitItem>();
    public static ArrayList<BlockItem> compareBlockItems = new ArrayList<BlockItem>();
    public static ArrayList<Unit> unitList;
    public static ArrayList<Block> blockList;

    public CompareResultsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_compare_results, container, false);
        ivProjectImage = (ImageView) rootView.findViewById(R.id.iv_project);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvAddress = (TextView) rootView.findViewById(R.id.tv_address);

        btnViewMapPlan = (Button) rootView.findViewById(R.id.btn_view_map_plan);

        btnTab1 = (Button) rootView.findViewById(R.id.tab1);
        btnTab2 = (Button) rootView.findViewById(R.id.tab2);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);


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
        tvBalanceTitle = (TextView) rootView.findViewById(R.id.title_balance);
        profile = gson.fromJson(Utility.readFromFile("profile", this.getContext()), Profile.class);

        new loadData().execute();

        btnViewMapPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle(R.string.view_map_plan_choice);
                builder.setSingleChoiceItems(R.array.map_plan_list, selectedView, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selectedView = which;
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedView == -1) {
                            Toast.makeText(getContext(), "Please select a choice", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            getFragmentManager().beginTransaction().replace(R.id.fl_container, new MapPlanFragment()).addToBackStack("MapPlanFragment").commit();
                        }
                    }
                }).setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });

        btnTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInformation(0);
            }
        });

        btnTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInformation(1);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_compare_units_results));
    }

    private class loadData extends AsyncTask<Void, Integer, Object> {
        @Override
        protected void onPreExecute() {
            dialog.show();

        }

        @Override
        protected void onPostExecute(Object o) {

            btnTab1.setText(compareBlockItems.get(0).getProjectName());
            btnTab2.setText(compareBlockItems.get(1).getProjectName());

            loadInformation(0);
            dialog.dismiss();
        }

        @Override
        protected Object doInBackground(Void... params) {
            bitmap = new ArrayList<Bitmap>();
            unitList = new ArrayList<Unit>();
            blockList = new ArrayList<Block>();

            for (int i = 0; i <2 ; i++) {
                gson = new Gson();
                Unit u = gson.fromJson(Utility.postRequest("Unit/GetUnitWithPayables?unitId=" + compareUnitItems.get(i).getUnitId(),
                        gson.toJson(profile)), Unit.class);
                unitList.add(u);


                Block block = gson.fromJson(Utility.getRequest("Block/GetBlock?blockId="+compareBlockItems.get(i).getBlockId()),Block.class);
                blockList.add(block);

                try {
                    InputStream in = new java.net.URL(block.getProject().getProjectImage()).openStream();
                    Bitmap b = BitmapFactory.decodeStream(in);
                    bitmap.add(b);
                    in.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }


    private void loadInformation(int i) {

        block = blockList.get(i);
        tvTitle.setText(compareBlockItems.get(i).getProjectName());
        tvAddress.setText(compareBlockItems.get(i).getBlockNo() + " " + compareBlockItems.get(i).getStreet());
        ivProjectImage.setImageBitmap(bitmap.get(i));

        if (profile == null) {
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

            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,
                            new ProfileFragment()).addToBackStack("ProfileFragment").commit();
                    ((MainActivity) getContext()).setActionBarTitle("PROFILE");
                    dialog.dismiss();

                }
            });


            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            adb.show();
        }else {
            Unit unit = unitList.get(i);
            tvUnitNumber.setText(unit.getUnitNo());
            tvUnitType.setText(unit.getUnitType().getUnitTypeName());
            tvPrice.setText(Utility.formatPrice(unit.getPrice()));
            tvApplicationFee.setText("$" + Utility.formatPrice(unit.getFees().getApplFee()));
            tvBookingFee.setText("$" + Utility.formatPrice(unit.getFees().getOptionFee()));
            tvPaymentCPF.setText("$" + Utility.formatPrice(unit.getFees().getSigningFeesCpf()));
            tvPaymentCash.setText("$" + Utility.formatPrice(unit.getFees().getSigningFeesCash()));
            tvStampDutyCPF.setText("$" + Utility.formatPrice(unit.getFees().getCollectionFeesCpf()));
            tvStampDutyCash.setText("$" + Utility.formatPrice(unit.getFees().getCollectionFeesCash()));
            tvBalanceCPF.setText("$" + Utility.formatPrice(unit.getFees().getMonthlyCpf()));
            tvBalanceCash.setText("$" + Utility.formatPrice(unit.getFees().getMonthlyCash()));
            tvBalanceTitle.setText("Balance of the purchase price \n (Per month for " + profile.getLoanTenure() + " years):");
        }


    }
}
