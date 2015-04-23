package com.cs320.shoptimize.shoptimizeapp;

import android.os.AsyncTask;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.HashMap;

/**
 * Created by Vincent Tse on 4/23/2015.
 */
public class DatabaseScanner extends AsyncTask<Void, Void, ScanResult> {

    String inventoryListName;
    String[] inventory;

    public DatabaseScanner (String inventoryListName, String[] inventory) {
        this.inventoryListName = inventoryListName;
        this.inventory = inventory;
    }

    @Override
    protected ScanResult doInBackground(Void... params) {

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

        String[] newinventory = new String[results.getCount()];

        for (int i = 0; i < results.getCount(); i++) {
            String name = results.getItems().get(i).get("ItemName").toString();
            String trimmed = name.substring(4, name.length() - 2);
            newinventory[i] = trimmed;
        }
        this.inventory = newinventory;
    }

}
