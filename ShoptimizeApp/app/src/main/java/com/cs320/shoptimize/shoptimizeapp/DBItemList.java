package com.cs320.shoptimize.shoptimizeapp;


import android.app.Activity;
import android.content.Context;
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

    ShoptimizeDB sdb = MainActivity.db;
    List<Item> items = new ArrayList<Item>();
    ShoptimizeDB db = null;

    public DBItemList() throws Exception {
        populateSL();
    }

    public List<Item> getItems(){return items;}

    public void addItem(String name, boolean coupon){
        items.add(new Item(name, coupon));
    }

    public void updateDB() {
        db = MainActivity.db;
    }
    public void populateSL(){
        addItem("Peanuts", false);
        addItem("Butter", false);
        addItem("Salt", false);
        addItem("Sardines", false);
    }

//    private String[] locs = {"Isle 1", "Isle 2", "Isle 13", "Isle 5", "Isle 7"};

    public void populateLocations(){

        int index = 0;
        ScanResult result = null;
        for(Item i : items){
            result = db.getInventoryListItem("TraderBruns_InventoryList", i.getName());
            for(Map<String, AttributeValue> item : result.getItems()) {
                String s = item.get("ItemLocation").getS();
                i.setLocation(s);
            }
        }
    }

    private class DBClient extends Activity{


    }
}
