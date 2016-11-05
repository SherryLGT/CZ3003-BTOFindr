package com.btofindr.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.controller.Utility;
import com.btofindr.model.BlockItem;
import com.btofindr.model.Unit;
import com.btofindr.model.UnitItem;

import java.io.InputStream;

import static com.btofindr.activity.MainActivity.scale;
import static com.btofindr.fragment.FavouriteFragment.selectedUnit;
import static com.btofindr.fragment.FavouriteFragment.selectedUnitItem;
import static com.btofindr.fragment.FavouriteFragment.selectedBlockItem;

import static com.btofindr.fragment.BlockFragment.block;
import static com.btofindr.fragment.BlockFragment.selectedView;
/**
 * Created by Shi Qi on 10/18/2016.
 */

/**
 * Fragment class for Unit details page in the application
 */
public class UnitDetailsFragment extends Fragment {

    private ProgressDialog dialog;
    private ImageView ivProjectImage;
    private TextView tvTitle, tvAddress, tvUnitType, tvPrice, tvUnitNumber;
    private Button btnViewMapPlan, btnCalculatePayables;
    private LinearLayout llUnitDetails;

    public static boolean recommend = false;
    public static boolean favourite = false;
    public static Unit unit;
    public static BlockItem blockItem;
    public static UnitItem unitItem;

    /**
     * Constructor class
     */
    public UnitDetailsFragment() {
    }

    /**
     * Inflate view, link with XML.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_unit_details, container, false);

        selectedView = -1;

        ivProjectImage = (ImageView) rootView.findViewById(R.id.iv_project);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvAddress = (TextView) rootView.findViewById(R.id.tv_address);
        tvUnitType = (TextView) rootView.findViewById(R.id.tv_unit_type);
        tvUnitNumber = (TextView) rootView.findViewById(R.id.tv_unit_number);
        tvPrice = (TextView) rootView.findViewById(R.id.tv_price);
        btnViewMapPlan = (Button) rootView.findViewById(R.id.btn_view_map_plan);
        btnCalculatePayables = (Button) rootView.findViewById(R.id.btn_calculate_payables);
        llUnitDetails = (LinearLayout) rootView.findViewById(R.id.ll_unit_details);

        unit = selectedUnit;
        unitItem = selectedUnitItem;
        blockItem = selectedBlockItem;
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        new getImage().execute();

        tvTitle.setText(blockItem.getProjectName());
        tvUnitNumber.setText(unitItem.getUnitNo() + " ");
        tvPrice.setText("Price: $" + Utility.formatPrice(unitItem.getPrice()));
        tvUnitType.setText("(" + unitItem.getUnitType().getUnitTypeName() + ")"); //room type
        tvAddress.setText(blockItem.getBlockNo() + " " + blockItem.getStreet());

        //view map plan
        btnViewMapPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.view_map_plan_choice);
                builder.setSingleChoiceItems(R.array.map_plan_list, selectedView, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selectedView = which;
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedView == -1) {
                            Toast.makeText(getContext(), "Please select a choice", Toast.LENGTH_SHORT).show();
                        } else {
                            block = unit.getUnitType().getBlock();
                            getFragmentManager().beginTransaction().replace(R.id.fl_container, new MapPlanFragment()).addToBackStack("MapPlanFragment").commit();
                        }
                    }
                }).setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });

        //calculate payables
        btnCalculatePayables.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putInt("selectedUnitID", unit.getUnitId());
                PayablesFragment pf = new PayablesFragment();
                pf.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.fl_container, pf).addToBackStack("PayablesFragment").commit();
                //wait ah
            }


        });

        return rootView;
    }

    /**
     * Method to resume the activity and set the action bar title to "RECOMMENDED" or "FAVOURITES"
     */
    @Override
    public void onResume() {
        super.onResume();
        if (recommend && !favourite) {
            ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_recommended));
        } else if (!recommend && favourite) {
            ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.title_favourites));
        }
    }

    /**
     * AsyncTask Class to load the image
     */
    private class getImage extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;

        /**
         * Method to initialize the bitmap variable to null before executing the loading of an image
         */
        @Override
        protected void onPreExecute() {
            bitmap = null;
        }

        /**
         * Method to set the image to the icon of each list view item after image is being loaded
         *
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ivProjectImage.setImageBitmap(bitmap);
        }

        /**
         * Method to load the image from the webservice
         *
         * @param params
         * @return a Bitmap variable containing the loaded image
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                InputStream in = new java.net.URL(blockItem.getIcon()).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }

    /**
     * For button layout settings
     *
     * @return
     */
    private Button generateButton() {
        Button btn = new Button(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        btn.setLayoutParams(params);
        btn.setPadding(Utility.getPixels(10, scale), Utility.getPixels(10, scale), Utility.getPixels(10, scale), Utility.getPixels(10, scale));
        btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tab_list_selector));
        btn.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.text_selector_black_gray));

        return btn;
    }


}
