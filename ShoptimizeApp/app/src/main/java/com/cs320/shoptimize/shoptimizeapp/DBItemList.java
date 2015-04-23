package com.cs320.shoptimize.shoptimizeapp;


import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.dynamodbv2.model.ScanResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabe Markarian on 3/8/2015.
 */
public class DBItemList {


    List<Item> items = new ArrayList<Item>();
    ScanResult result = null;

    ArrayList<Integer>   xs          = new ArrayList<Integer>();
    ArrayList<Integer>   ys          = new ArrayList<Integer>();
    ArrayList<String>    names       = new ArrayList<String>();
    ArrayList<String[]>  preprocXY   = new ArrayList<String[]>();
    ArrayList<String>    preprocNAME = new ArrayList<String>();

    public DBItemList() throws Exception {
        // populateSL();
    }

    public List<Item> getItems(){return items;}

    public void addItem(String name, boolean coupon){
        items.add(new Item(name, coupon));
    }

    public void populateSL(){
        addItem("Peanuts", false);
        addItem("Butter", false);
        addItem("Milk", false);
        addItem("Peanut Butter", false);
    }

    public boolean contains(String s){
        for(Item i : items){
            if(i.getName().toLowerCase().equals(s.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public void populateLocations(){

        int index = 0;
        //attempted to load coords into ArrayLists here, but it got sticky.  Moved to helper
        for(Item i : items){
            new InventoryListQueryer("TraderBruns_InventoryList", i).execute();
        }
    }


    public ArrayList<Integer> getXs()    { return xs;}

    public ArrayList<Integer> getYs()    { return ys; }

    public ArrayList<String>  getNames() { return names; }
    //LOC OF FIRST﹕ {S: 50,50,}



    protected void addFPPointsforIntent(){
        for(Item i : items) {
            String s = i.getLocation();
            String name = i.getName();
            if(s.charAt(0) != 'I') {  //check for validity
                //s = s.substring(s.indexOf(' '), s.lastIndexOf(','));
                //s = s.trim();
                String[] xy = s.split(",");
                preprocXY.add(xy);
                preprocNAME.add(name);
                //Integer x = Integer.parseInt(xy[0]);
                //Integer y = Integer.parseInt(xy[1]);

            }
        }
        routingAlgorithm();
        //xs.add(x);  ys.add(y);
    }

    private void routingAlgorithm(){
        xs.clear();
        ys.clear();
        names.clear();
        while(preprocXY.size() > 0){
            int min = 100000; //max int
            int minIdx = 0;
            for(int i = 0; i < preprocXY.size(); i++){
                int comp = Integer.parseInt(preprocXY.get(i)[0]);
                if(comp < min){
                    min = comp;
                    minIdx = i;
                }
            }
            xs.add(Integer.parseInt(preprocXY.get(minIdx)[0]));
            ys.add(Integer.parseInt(preprocXY.get(minIdx)[1]));
            names.add(preprocNAME.get(minIdx));
            preprocNAME.remove(minIdx);
            preprocXY.remove(minIdx);
        }
    }


    private class InventoryListQueryer extends AsyncTask<Void, Void, ScanResult> {

        String InventoryListName;
        Item item;

        public InventoryListQueryer (String InventoryListName, Item item) {
            this.InventoryListName = InventoryListName;
            this.item = item;
        }

        @Override
        protected ScanResult doInBackground(Void... params) {
            return ShoptimizeDB.getInventoryListItem(InventoryListName, item.getName());
        }

        @Override
        protected void onPostExecute(ScanResult scanResult) {
            if(scanResult.getCount() == 0) {
                item.setLocation("Item is not in database");
            }
            else {
                String loc = scanResult.getItems().get(0).get("ItemLocation").toString();
                String trimmed = loc.substring(4, loc.length() - 2);
                item.setLocation(trimmed);
            }
        }


    private class InventoryListPoster extends AsyncTask<Void, Void, String> {

        String InventoryListName;
        Item item;

        public InventoryListPoster (String InventoryListName, Item item) {
            this.InventoryListName = InventoryListName;
            this.item = item;
        }

        @Override
        protected String doInBackground(Void... params) {
            ShoptimizeDB.addInventoryListItem(InventoryListName, item.getName(), item.getLocation(), "False", "");
            return "Add item " + item.toString();
        }


        protected void onPostExecute(String... params) {
            Log.v("RETURN", params.toString());
        }
    }

    }
}
