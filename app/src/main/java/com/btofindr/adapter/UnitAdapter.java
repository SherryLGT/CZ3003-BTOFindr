package com.btofindr.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.btofindr.R;
import com.btofindr.activity.MainActivity;
import com.btofindr.controller.Utility;
import com.btofindr.fragment.PayablesFragment;
import com.btofindr.model.UnitItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sherry on 06/10/2016.
 */

public class UnitAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UnitItem> unitItems;

    private TextView unitNo, price;
    private ImageView calculate, favourite;
    private Gson gson;
    public static int selectedUnitId;

    public UnitAdapter(Context context, ArrayList<UnitItem> unitItems){
        this.context = context;
        this.unitItems = unitItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.unit_list_item, null);
        }

        unitNo = (TextView) convertView.findViewById(R.id.tv_unit_no);
        price = (TextView) convertView.findViewById(R.id.tv_price);
        calculate = (ImageView) convertView.findViewById(R.id.iv_calculate);
        favourite = (ImageView) convertView.findViewById(R.id.iv_favourite);

        unitNo.setText("FLOOR " + unitItems.get(position).getUnitNo());
        price.setText("$" + Utility.formatPrice(unitItems.get(position).getPrice()));
        favourite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favourite));
        favourite.setTag(R.drawable.ic_favourite);

        gson = new Gson();
        ArrayList<Integer> favourites = gson.fromJson(Utility.readFromFile("favourites", context), new TypeToken<List<Integer>>() {
        }.getType());
        if(favourites != null) {
            for(Integer id : favourites) {
                if(id == unitItems.get(position).getUnitId()) {
                    favourite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favourited));
                    favourite.setTag(R.drawable.ic_favourited);
                }
            }
        }

        convertView.setTag(unitItems.get(position).getUnitId());
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUnitId = unitItems.get(position).getUnitId();
                Fragment f = PayablesFragment.newInstance(selectedUnitId);

                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, f ).addToBackStack("CalculatePayablesFragment").commit();
                ((MainActivity) context).setActionBarTitle(context.getResources().getString(R.string.title_calculate_payables));
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gson = new Gson();
                ArrayList<Integer> favourites = gson.fromJson(Utility.readFromFile("favourites", context), new TypeToken<List<Integer>>() {
                }.getType());
                int selected = (Integer)(((LinearLayout) v.getParent()).getTag());

                if((Integer) ((ImageView) v).getTag() == R.drawable.ic_favourite) {
                    if (favourites == null) {
                        favourites = new ArrayList<Integer>();
                    }

                    favourites.add(selected);
                    if(Utility.writeToFile("favourites", gson.toJson(favourites), context))
                    {
                        new addFavouriteCount().execute(selected);
                        Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                        ((ImageView) v).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favourited));
                        ((ImageView) v).setTag(R.drawable.ic_favourited);
                    }
                    else
                    {
                        Toast.makeText(context, "Error adding to Favourites. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    for(int i = 0; i < favourites.size(); i++) {
                        if(selected == favourites.get(i)) {
                            favourites.remove(i);
                        }
                    }

                    if(Utility.writeToFile("favourites", gson.toJson(favourites), context))
                    {
                        new removeFavouriteCount().execute(selected);
                        Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                        ((ImageView) v).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favourite));
                        ((ImageView) v).setTag(R.drawable.ic_favourite);
                    }
                    else
                    {
                        Toast.makeText(context, "Error removing from Favourites. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return unitItems.size();
    }

    @Override
    public Object getItem(int position) {
        return unitItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class addFavouriteCount extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            Utility.getRequest("Unit/AddFaveUnit?unitId="+params[0].toString());
            return null;
        }
    }

    private class removeFavouriteCount extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            Utility.getRequest("Unit/RemoveFaveUnit?unitId="+params[0].toString());
            return null;
        }
    }
}
