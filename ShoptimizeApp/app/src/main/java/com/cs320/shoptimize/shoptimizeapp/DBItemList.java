package com.cs320.shoptimize.shoptimizeapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabe Markarian on 3/8/2015.
 */
public class DBItemList {

    List<Item> items = new ArrayList<Item>();

    public DBItemList(){
        populateSL();
    }

    public List<Item> getItems(){return items;}

    public void addItem(String name, boolean coupon){
        items.add(new Item(name, coupon));
    }

    public void populateSL(){
        addItem("Peanuts", false);
        addItem("Butter", false);
        addItem("Salt", false);
        addItem("Sardines", false);
    }

    private String[] locs = {"Isle 1", "Isle 2", "Isle 13", "Isle 5", "Isle 7"};

    public void populateLocations(){
        int index = 0;
        for(Item i : items){
            i.setLocation(locs[index++]);
        }
    }
}
