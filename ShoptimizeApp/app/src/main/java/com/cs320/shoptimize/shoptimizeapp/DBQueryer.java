package com.cs320.shoptimize.shoptimizeapp;

import android.os.AsyncTask;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Vincent Tse on 3/8/2015.
 */
public class DBQueryer extends AsyncTask<Item, Void, ScanResult> {


    @Override
    protected ScanResult doInBackground(Item... params) {
        ScanResult result = null;
        for(Item i : params) {
            result = MainActivity.db.getInventoryListItem("TraderBruns_InventoryList", i.getName());
        }
        return result;
    }
}
