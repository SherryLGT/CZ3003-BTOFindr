package com.btofindr.adapter;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.btofindr.R;
import com.btofindr.controller.Utility;
import com.btofindr.model.BlockItem;
import com.btofindr.model.UnitItem;
import com.google.gson.Gson;
import java.io.InputStream;
import java.util.ArrayList;

import static android.view.View.VISIBLE;
import static com.btofindr.R.id.relLayout1;
import static com.btofindr.fragment.FavouriteFragment.editMode;
import static com.btofindr.fragment.FavouriteFragment.selectedCheckbox;
import static com.btofindr.fragment.FavouriteFragment.selectedCheckboxPosition;
import static com.btofindr.fragment.FavouriteFragment.mMenuItem;
/**
 * Created by Shi Qi on 10/12/2016.
 */

/**
 * Adapter Class for the List view used in the Favourites page
 */
public class FavouriteAdapter extends BaseAdapter   {
    private Context context;
    private ArrayList<UnitItem> unitItems;
    private ArrayList<BlockItem> blockItems;
    private Gson gson;
    private ImageView icon;
    private String url;
    private Bitmap bitmap;
    private FloatingActionButton deleteBtn;
    View rootView;
    ListView lvUnits;
    TextView tv_nofave;


    private ArrayList<Integer> favourites;

    /**
     * Constructor
     * @param context
     * @param unitItems
     * @param blockItems
     * @param favourites
     * @param rootView
     */
    public FavouriteAdapter(Context context, ArrayList<UnitItem> unitItems,
        ArrayList<BlockItem> blockItems, ArrayList<Integer> favourites, View rootView){
        this.context = context;
        this.unitItems = unitItems;
        this.blockItems = blockItems;
        this.favourites = favourites;
        this.rootView = rootView;
        selectedCheckboxPosition = new ArrayList<Integer>();

    }

    /**
     * Inflate view, linking to XML
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.favourite_list_item, null);
        }

        RelativeLayout l1 = (RelativeLayout) convertView.findViewById(relLayout1);
        icon = (ImageView) convertView.findViewById(R.id.iv_icon);//project image
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView address = (TextView) convertView.findViewById(R.id.tv_address);
        TextView unitNumber = (TextView) convertView.findViewById(R.id.tv_unit_no);
        TextView unitType = (TextView) convertView.findViewById(R.id.tv_unit_types);
        TextView price = (TextView) convertView.findViewById(R.id.tv_price);
        tv_nofave = (TextView)rootView.findViewById(R.id.tv_nofave);

        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkboxUnit);

        if(editMode == ListView.CHOICE_MODE_MULTIPLE){
            cb.setVisibility(VISIBLE);
            l1.animate().translationX(120);
        }
        else{
            cb.setVisibility(View.INVISIBLE);
            l1.animate().translationX(0);
        }

        url = blockItems.get(position).getIcon();
        new getImage().execute();

        title.setText(blockItems.get(position).getProjectName());
        unitNumber.setText(unitItems.get(position).getUnitNo()+" ");
        address.setText(blockItems.get(position).getBlockNo() + " " + blockItems.get(position).getStreet());
        price.setText("Price: $" + Utility.formatPrice(unitItems.get(position).getPrice()));
        unitType.setText("("+unitItems.get(position).getUnitType().getUnitTypeName()+")"); //room type
        cb.setTag(position);

        cb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                CheckBox cb = (CheckBox) v;

                int position = (Integer) v.getTag();
                int unitId = unitItems.get((Integer) v.getTag()).getUnitId();

                if(selectedCheckbox.contains(unitId)){
                    selectedCheckbox.remove((Integer) unitId);
                    selectedCheckboxPosition.remove((Integer) position);

                }
                else{
                    selectedCheckbox.add(unitId);
                    selectedCheckboxPosition.add(position);
                }
            }
        });

        deleteBtn = (FloatingActionButton) rootView.findViewById(R.id.FAB);
        lvUnits = (ListView)rootView.findViewById(R.id.lv_units);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gson = new Gson();
                for(int i=0; i<selectedCheckbox.size();i++){
                    favourites.remove(selectedCheckbox.get(i));

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Utility.writeToFile("favourites", gson.toJson(favourites), context)){

                            for(int i=0; i<selectedCheckboxPosition.size(); i++){
                                remove(selectedCheckbox.get(i),1);//pass in unitID to remove instead
                                new removeFavouriteCount().execute(selectedCheckbox.get(i));//remove using unitID
                            }

                            notifyDataSetChanged();
                            deleteBtn.setVisibility(View.INVISIBLE);

                            Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(context, "Error adding to Favourites. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", null).show();
                lvUnits.setChoiceMode(ListView.CHOICE_MODE_NONE);
                editMode = lvUnits.CHOICE_MODE_NONE;






            }
        });
        return convertView;
    }

    /**
     * Get size of unitItems
     * @return
     */
    @Override
    public int getCount() {
        return unitItems.size();
    }

    /**
     * Remove item from array
     * @param position
     * @param choice
     */
    public void remove(int position, int choice) {

        for(int i=0;i<unitItems.size();i++){
            if(unitItems.get(i).getUnitId()==position){//i am removing by unitID
                unitItems.remove(i);
                blockItems.remove(i);
                continue;
            }
        }

        if(choice==1){
            notifyDataSetChanged();
            if(unitItems.isEmpty()){
                deleteBtn.setVisibility(View.INVISIBLE);
                tv_nofave.setVisibility(View.VISIBLE);

                mMenuItem.setVisible(false);
            }
        }else{
            notifyDataSetChanged();
            if(unitItems.isEmpty()){
                deleteBtn.setVisibility(View.INVISIBLE);
                tv_nofave.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     *
     * Method to get the number of list items by calling the previous getCount() method
     * @return
     */
    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    /**
     * Method to get the position of the item that is in the list view
     *
     * @param position
     * @return an integer containing the position of the item in the list view
     */
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    /**
     * Method to get the Object that is in the list view
     *
     * @param position the integer indicates which item it is in the ArrayList
     * @return an Object of the specific list view item
     */
    @Override
    public Object getItem(int position) {
        return unitItems.get(position);
    }

    /**
     * Method to get the list view item's id
     *
     * @param position
     * @return a long number containing the position of the item in the list view
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * AsyncTask Class to load the image for each unit/block
     */
    private class getImage extends AsyncTask<String, Void, Bitmap> {
        /**
         * Method to initialize the bitmap variable to null before executing the loading of an image
         */
        @Override
        protected void onPreExecute() {
            bitmap = null;
        }

        /**
         * Method to set the image to the icon of each list view item after image is being loaded
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            icon.setImageBitmap(bitmap);
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
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    /**
     * Update favourite count in database
     */
    private class removeFavouriteCount extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            Utility.getRequest("Unit/RemoveFaveUnit?unitId="+params[0].toString());
            return null;
        }
    }
}
