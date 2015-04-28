package com.cs320.shoptimize.shoptimizeapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Vincent Tse on 4/17/2015.
 */

public class InventoryListPoster extends AsyncTask<Void, Void, Void> {

    String InventoryListName;
    ArrayList<Item> items;
    Context context;

    public InventoryListPoster (String InventoryListName, ArrayList<Item> items, Context context) {
        this.InventoryListName = InventoryListName;
        this.items = items;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {

        while(!isNetworkAvailable()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(Item item : items) {
            ShoptimizeDB.addInventoryListItem(InventoryListName, item.getName(), item.getLocation(), "False", "Screw Yourself");
            Log.v("RESULT", "Added " + item.getName() + "with location " + item.getLocation() + " to the database");
        }
        return null;

    }

    protected void onPostExecute(Void... params) {

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}