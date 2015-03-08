package com.cs320.shoptimize.shoptimizeapp;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;

/**
* This sample demonstrates how to perform a few simple operations with the
* Amazon DynamoDB service.
*/

/*
* WANRNING:
*      To avoid accidental leakage of your credentials, DO NOT keep
*      the credentials file in your source directory.
*/

/**
* The only information needed to create a client are security credentials
* consisting of the AWS Access Key ID and Secret Access Key. All other
* configuration, such as the service endpoints, are performed
* automatically. Client parameters, such as proxies, can be specified in an
* optional ClientConfiguration object when constructing a client.
*
* @see com.amazonaws.auth.BasicAWSCredentials
* @see com.amazonaws.ClientConfiguration
*/

public class ShoptimizeDB {

	//Instance of the DB Client. Make all queries to this client.
	static AmazonDynamoDBClient dynamoDB;

    public ShoptimizeDB (Context context) throws Exception {
        init(context);
    }

	private void init(Context context) throws Exception {
		/*
		 * The ProfileCredentialsProvider will return your [Shoptimize]
		 * credential profile by reading from the credentials file located at
		 * (C:\\Users\\Vincent Tse\\.aws\\credentials.csv).
		 */

		CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
			    context, // Context
			    "us-east-1:29c88855-504d-436a-b962-e91d1dc68092", // Identity Pool ID
			    Regions.US_EAST_1 // Region
			);

//		try {
//			credentials = new ProfileCredentialsProvider("C:\\Users\\Vincent Tse\\.aws\\credentials.csv", "Shoptimize").getCredentials();
//		} catch (Exception e) {
//			throw new AmazonClientException(
//					"Cannot load the credentials from the credential profiles file. " +
//							"Please make sure that your credentials file is at the correct " +
//							"location (C:\\Users\\Vincent Tse\\.aws\\credentials.csv), and is in valid format.",
//							e);
//		}

		dynamoDB = new AmazonDynamoDBClient(credentialsProvider);
		Region usEast1 = Region.getRegion(Regions.US_EAST_1);
		dynamoDB.setRegion(usEast1);
	}

	public void addStoreItem(String storeName, String inventoryListName, String floorPlanName) {
		Map<String, AttributeValue> storeItem = newStoreItem(storeName, inventoryListName, floorPlanName);
		PutItemRequest putItemRequest = new PutItemRequest("Stores", storeItem);
		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
	}

	public void addInventoryListItem(String inventoryListName, String itemName, String itemLocation, String hasCoupon, String...notes) {
		Map<String, AttributeValue> inventoryListItem = newInventoryListItem(itemName, itemLocation, hasCoupon, notes);
		PutItemRequest putItemRequest = new PutItemRequest(inventoryListName, inventoryListItem);
		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
	}

	public ScanResult getStoreItem(String storeName) {
        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition condition = new Condition()
            .withComparisonOperator(ComparisonOperator.EQ.toString())
            .withAttributeValueList(new AttributeValue(storeName));
        scanFilter.put("StoreName", condition);
        ScanRequest scanRequest = new ScanRequest("Stores").withScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);
        return scanResult;
	}

	public ScanResult getInventoryListItem(String inventoryListName, String itemName) {
        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition condition = new Condition()
            .withComparisonOperator(ComparisonOperator.EQ.toString())
            .withAttributeValueList(new AttributeValue(itemName));
        scanFilter.put("ItemName", condition);
        ScanRequest scanRequest = new ScanRequest(inventoryListName).withScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);
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