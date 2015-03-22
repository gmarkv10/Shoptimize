package com.cs320.shoptimize.shoptimizeapp;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * This class is used to get clients to the various AWS services. Before
 * accessing a client the credentials should be checked to ensure validity.
 */
public class AmazonClientManager {

    private static AmazonClientManager instance = null;
    private static AmazonDynamoDBClient ddb = null;
    private static Context context;

    private AmazonClientManager() {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static AmazonClientManager getInstance() {
        if(instance == null) {
            instance = new AmazonClientManager();
        }
        return instance;
    }

    public AmazonDynamoDBClient ddb() {
        if(ddb == null) {
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    context,
                    "us-east-1:29c88855-504d-436a-b962-e91d1dc68092", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );
            ddb = new AmazonDynamoDBClient(credentialsProvider);
            Region usEast1 = Region.getRegion(Regions.US_EAST_1);
            ddb.setRegion(usEast1);
        }
        return ddb;
    }
}