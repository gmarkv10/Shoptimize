package com.cs320.shoptimize.shoptimizeapp;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

public class ShoptimizeDB {

    public static void addStoreItem(String storeName, String inventoryListName, String floorPlanName) {

        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();

        Map<String, AttributeValue> storeItem = newStoreItem(storeName, inventoryListName, floorPlanName);
        PutItemRequest putItemRequest = new PutItemRequest("Stores", storeItem);
        PutItemResult putItemResult = ddb.putItem(putItemRequest);

        System.out.println("Result: " + putItemResult);
    }

    public static void addInventoryListItem(String inventoryListName, String itemName, String itemLocation, String hasCoupon, String...notes) {

        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();

        Map<String, AttributeValue> inventoryListItem = newInventoryListItem(itemName, itemLocation, hasCoupon, notes);
        PutItemRequest putItemRequest = new PutItemRequest(inventoryListName, inventoryListItem);
        PutItemResult putItemResult = ddb.putItem(putItemRequest);

        System.out.println("Result: " + putItemResult);
    }

    public static ScanResult getStoreItem(String storeName) {

        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();

        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue(storeName));
        scanFilter.put("StoreName", condition);

        ScanRequest scanRequest = new ScanRequest("Stores").withScanFilter(scanFilter);
        ScanResult scanResult = ddb.scan(scanRequest);
        return scanResult;
    }

    public static ScanResult getInventoryListItem(String inventoryListName, String itemName) {

        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();

        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue(itemName));
        scanFilter.put("ItemName", condition);

        ScanRequest scanRequest = new ScanRequest(inventoryListName).withScanFilter(scanFilter);
        ScanResult scanResult = ddb.scan(scanRequest);

        return scanResult;
    }

    private static Map<String, AttributeValue> newStoreItem(String storeName, String inventoryListName, String floorPlanName) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("StoreName", new AttributeValue(storeName));
        item.put("InventoryListName", new AttributeValue(inventoryListName));
        item.put("FloorPlanName", new AttributeValue(floorPlanName));
        return item;
    }

    private static Map<String, AttributeValue> newInventoryListItem(String itemName, String itemLocation, String hasCoupon, String...notes) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("ItemName", new AttributeValue(itemName));
        item.put("ItemLocation", new AttributeValue(itemLocation));
        item.put("HasCoupon", new AttributeValue(hasCoupon));
        item.put("Notes", new AttributeValue().withSS(notes));

        return item;
    }
}