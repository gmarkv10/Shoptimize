package com.cs320.shoptimize.shoptimizeapp;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Vincent Tse on 4/17/2015.
 */

public class InventoryListPoster extends AsyncTask<Void, Void, String> {

    String InventoryListName;
    Item item;

    public InventoryListPoster (String InventoryListName, Item item) {
        this.InventoryListName = InventoryListName;
        this.item = item;
    }

    @Override
    protected String doInBackground(Void... params) {
        ShoptimizeDB.addInventoryListItem(InventoryListName, item.getName(), item.getLocation(), "False", "Screw Yourself");
        return "Add item " + item.toString();
    }

    protected void onPostExecute(String... params) {
        Log.v("RETURN", params.toString());
    }
}