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

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;

import static android.view.View.VISIBLE;
import static com.btofindr.R.id.relLayout1;
import static com.btofindr.R.id.tv_nohistory;
import static com.btofindr.fragment.FavouriteFragment.selectedCheckboxPosition;
import static com.btofindr.fragment.HistoryFragment.mMenuItem2;
import static com.btofindr.fragment.HistoryFragment.editModeHistory;
import static com.btofindr.fragment.HistoryFragment.selectedHistoryCheckbox;
import static com.btofindr.fragment.HistoryFragment.selectedHistoryCheckboxPosition;

/**
 * Created by Shi Qi on 10/12/2016.
 */

/**
 * Adapter Class for the List view used in the History page
 */
public class HistoryAdapter extends BaseAdapter   {
    private Context context;
    private ArrayList<Integer> history;
    private ArrayList<BlockItem> blockItems;
    private Gson gson;
    private ImageView icon;
    private String url;
    private Bitmap bitmap;
    private FloatingActionButton deleteBtn;
    View rootView;
    ListView lvBlocks;
    TextView tv_nohistory;
    TextView tv_norv;

    /**
     * Constructor class
     * @param context the context of the fragment
     * @param blockItems an ArrayList of BlockItem used to pass through when each unit is clicked
     * @param history list of history items
     * @param rootView a view of the parent page, CompareUnitsFragment
     */
    public HistoryAdapter(Context context, ArrayList<BlockItem> blockItems, ArrayList<Integer> history, View rootView){
        this.context = context;
        this.blockItems = blockItems;
        this.history = history;
        this.rootView = rootView;
        selectedCheckboxPosition = new ArrayList<Integer>();

    }

    /**
     * Inflating view, link to XML.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_list_item, null);
        }

        RelativeLayout l1 = (RelativeLayout) convertView.findViewById(relLayout1);
        icon = (ImageView) convertView.findViewById(R.id.iv_icon);
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView address = (TextView) convertView.findViewById(R.id.tv_address);
        TextView tvUnitTypes = (TextView) convertView.findViewById(R.id.tv_unit_types);
        TextView priceRange = (TextView) convertView.findViewById(R.id.tv_price_range);
        tv_nohistory= (TextView)rootView.findViewById(R.id.tv_nohistory);
        tv_norv = (TextView) rootView.findViewById(R.id.tv_norv);

        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkboxBlock);

        if(editModeHistory == ListView.CHOICE_MODE_MULTIPLE){
            cb.setVisibility(VISIBLE);
            l1.animate().translationX(120);
        }
        else{
            cb.setVisibility(View.INVISIBLE);
            l1.animate().translationX(0);
        }

        url = blockItems.get(position).getIcon();
        new getImage().execute();

        title.setText(blockItems.get(position).getProjectName()+"");
        address.setText(blockItems.get(position).getBlockNo() + " " + blockItems.get(position).getStreet());
        priceRange.setText("Price: $" + Utility.formatPrice(blockItems.get(position).getMinPrice()) + " - $" + Utility.formatPrice(blockItems.get(position).getMaxPrice()));

        String unitTypeNames = "";
        for (int i = 0; i < blockItems.get(position).getUnitTypes().size(); i++) {
            unitTypeNames += blockItems.get(position).getUnitTypes().get(i).getUnitTypeName();

            if(i != blockItems.get(position).getUnitTypes().size() - 1) {
                unitTypeNames += ", ";
            }
        }

        tvUnitTypes.setText(unitTypeNames);
        cb.setTag(position);

        cb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                CheckBox cb = (CheckBox) v;

                int position = (Integer) v.getTag();
                int blockId = blockItems.get((Integer) v.getTag()).getBlockId();

                if(selectedHistoryCheckbox.contains(blockId)){
                    selectedHistoryCheckbox.remove((Integer) blockId);
                    selectedHistoryCheckboxPosition.remove((Integer) position);

                }
                else{
                    selectedHistoryCheckbox.add(blockId);
                    selectedHistoryCheckboxPosition.add(position);
                }
            }
        });

        deleteBtn = (FloatingActionButton) rootView.findViewById(R.id.FAB);
        lvBlocks = (ListView)rootView.findViewById(R.id.lv_blocks);

        if(deleteBtn!=null){
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gson = new Gson();
                for(int i=0; i<selectedHistoryCheckbox.size();i++){
                    history.remove(selectedHistoryCheckbox.get(i));

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Utility.writeToFile("history", gson.toJson(history), context)){

                            for(int i=0; i<selectedHistoryCheckbox.size(); i++){
                                remove(selectedHistoryCheckbox.get(i),1);
                            }

                            notifyDataSetChanged();
                            deleteBtn.setVisibility(View.INVISIBLE);

                            Toast.makeText(context, "Removed from History", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(context, "Error removing from History. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", null).show();
                lvBlocks.setChoiceMode(ListView.CHOICE_MODE_NONE);
                editModeHistory = lvBlocks.CHOICE_MODE_NONE;

            }
        });
        }
        return convertView;
    }

    /**
     * Get size of blockItems
     * @return
     */
    @Override
    public int getCount() {
        return blockItems.size();
    }

    /**
     * remove from list
     * @param position
     * @param choice
     */
    public void remove(int position, int choice) {

        for(int i=0;i<blockItems.size();i++){
            if(blockItems.get(i).getBlockId()==position){
                blockItems.remove(i);
                continue;
            }
        }

        if(choice==1){
            if(blockItems.isEmpty()){
                if(deleteBtn!=null){
                    deleteBtn.setVisibility(View.INVISIBLE);
                    mMenuItem2.setVisible(false);
                    tv_nohistory.setVisibility(View.VISIBLE);
                }
                else{
                    tv_norv.setVisibility(View.VISIBLE);
                }
            }
        }else{
            if(blockItems.isEmpty()) {
                if (deleteBtn != null) {
                    deleteBtn.setVisibility(View.INVISIBLE);
                    //mMenuItem2.setVisible(false);
                    tv_nohistory.setVisibility(View.VISIBLE);
                } else {
                    tv_norv.setVisibility(View.VISIBLE);
                }
            }
        }


        notifyDataSetChanged();

    }

    /**
     * Method to get the number of list items by calling the previous getCount() method
     *
     * @return an integer containing the number of items in the list view
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
     * Get block item from position
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return blockItems.get(position);
    }

    /**
     * Get itemid from position
     * @param position
     * @return
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
}
