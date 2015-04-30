package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by Humdilla on 3/25/15.
 */
public class StoreListActivityTest extends ActivityInstrumentationTestCase2<StoreListActivity> {

    private StoreListActivity activity;
    private Solo solo;
    private Instrumentation.ActivityMonitor mainActivityMonitor;


    public StoreListActivityTest(){
        super(StoreListActivity.class);
    }

    public void setUp()throws Exception{
        mainActivityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
    }

    public void tearDown() throws Exception{
        mainActivityMonitor = null;
        solo.finishOpenedActivities();
    }

    public void testPreconditions(){
        assertNotNull("activity is null", activity);
        solo.assertCurrentActivity("Current Activity is not StoreListActivity", StoreListActivity.class);
        assertNotNull("activity.storeList is null", activity.storeList);
    }

    /**
     * This method tests whether or not clicking a store in the view list starts a MainActivity.
     **/
    public void testStoreSelection(){
        //Pressing one of the Stores should start a MainActivity
        solo.clickOnText("Trader Brun's");
        getInstrumentation().waitForIdleSync();
        Activity mainActivity = getInstrumentation().waitForMonitorWithTimeout(mainActivityMonitor, 3000);
        assertNotNull("mainActivity is null", mainActivity);
        mainActivity.finish();
    }

    /**
     * This method tests the visibility of UI components.
     **/
    public void testUIVisibility(){
        assertTrue("'Store List' text is not visible", View.VISIBLE == solo.getView(R.id.textView3).getVisibility());
        assertTrue("Store list view is not visible", View.VISIBLE == solo.getView(R.id.listView2).getVisibility());
    }
}
