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
}
