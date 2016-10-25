package com.btofindr.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btofindr.R;

import java.io.InputStream;

import static com.btofindr.fragment.BlockFragment.block;
import static com.btofindr.fragment.BlockFragment.selectedView;

/**
 * Created by Sherry on 17/10/2016.
 */

public class MapPlanFragment extends Fragment {

    private ProgressDialog dialog;
    private TextView tvTitle;
    private ImageView ivImage;

    private String title[];
    private String url;
    private Bitmap bitmap;
    private int height;


    public MapPlanFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map_plan, container, false);

        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        ivImage = (ImageView) rootView.findViewById(R.id.iv_image);

        title = getContext().getResources().getStringArray(R.array.map_plan_list);

        tvTitle.setText((block.getProject().getProjectName() + " - " + title[selectedView]).toUpperCase());

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        height = getResources().getDisplayMetrics().heightPixels;
        switch(selectedView) {
            case 0: // Site Plan
                url = block.getSitePlan();
                height = (int) (height * 0.6);
                break;
            case 1: // Town Map
                url = block.getTownMap();
                height = (int) (height * 0.8);
                break;
            case 2: // Block Plan
                url = block.getBlockPlan();
                height = height * 2;
                break;
            case 3 : // Unit Distribution
                url = block.getUnitDist();
                height = (int) (height * 0.7);
                break;
            case 4: // Typical Floor Plan
                url = block.getFloorPlan();
                height = (int) (height * 2.1);
                break;
            case 5: // Layout Ideas
                url = block.getLayoutIdeas();
                height = (int) (height * 2.1);
                break;
            case 6: // General Specifications
                url = block.getSpecs();
                height = (int) (height * 2.1);
                break;
            default:
                break;
        }
        new getImage().execute();
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        return rootView;
    }

    private class getImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            dialog.show();
            bitmap = null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            dialog.dismiss();
            ivImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, getResources().getDisplayMetrics().widthPixels, height, false));
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }
}
