package com.cs320.shoptimize.shoptimizeapp;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Gabe Markarian on 3/8/2015.
 */
public class DBItemList {


    List<Item> items = new ArrayList<Item>();
    ScanResult result = null;

    public DBItemList() throws Exception {
        populateSL();
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

        for(Item i : items){
            new InventoryListQueryer("TraderBruns_InventoryList", i).execute();
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
                item.setLocation(scanResult.getItems().get(0).get("ItemLocation").toString());
            }
        }


    }
}
