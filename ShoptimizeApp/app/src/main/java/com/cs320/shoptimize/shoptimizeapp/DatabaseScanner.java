package com.cs320.shoptimize.shoptimizeapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vincent Tse on 4/23/2015.
 */
public class DatabaseScanner extends AsyncTask<Void, Void, ScanResult> {

    String inventoryListName;
    AutoCompleteTextView addField;
    ArrayAdapter<String> autoCompleteAdapter;

    public DatabaseScanner (String inventoryListName, AutoCompleteTextView addField, ArrayAdapter<String> autoCompleteAdapter) {
        this.inventoryListName = inventoryListName;
        this.addField = addField;
        this.autoCompleteAdapter = autoCompleteAdapter;
    }

    @Override
    protected ScanResult doInBackground(Void... params) {

        if(inventoryListName.contains("Trader")) {
            inventoryListName = "TraderBruns_InventoryList";
        }

        if(inventoryListName.contains("Big")) {
            inventoryListName = "BigY_InventoryList";
        }

        if(inventoryListName.contains("Stop") || inventoryListName.contains("Other")) {
            inventoryListName = "StopAndShop_InventoryList";
        }

        Log.v("LISTSELECT", inventoryListName + " selected");

        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();

        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.NOT_NULL.toString());
        scanFilter.put("ItemName", condition);
        ScanRequest scanRequest = new ScanRequest(inventoryListName).withScanFilter(scanFilter);
        ScanResult scanResult = ddb.scan(scanRequest);
        return scanResult;
    }

    protected void onPostExecute(ScanResult results) {

        for (int i = 0; i < results.getCount(); i++) {
            String name = results.getItems().get(i).get("ItemName").toString();
            String trimmed = name.substring(4, name.length() - 2);
            autoCompleteAdapter.add(trimmed);
        }

        addField.setThreshold(1);
        addField.setAdapter(autoCompleteAdapter);
    }
}
