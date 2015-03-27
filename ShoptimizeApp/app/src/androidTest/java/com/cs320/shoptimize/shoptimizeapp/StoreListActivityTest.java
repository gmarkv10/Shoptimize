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
    private View addStoreButton;
    private EditText addStoreField;
    private Instrumentation.ActivityMonitor mainActivityMonitor;


    public StoreListActivityTest(){
        super(StoreListActivity.class);
    }

    public void setUp()throws Exception{
        mainActivityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        addStoreButton = solo.getView(R.id.but_addStore);
        addStoreField = (EditText)solo.getView(R.id.txt_addStore);
    }

    public void tearDown() throws Exception{
        mainActivityMonitor = null;
        addStoreButton = null;
        addStoreField = null;
        solo.finishOpenedActivities();
    }

    public void testPreconditions(){
        assertNotNull("activity is null", activity);
        solo.assertCurrentActivity("Current Activity is not StoreListActivity", StoreListActivity.class);
        assertNotNull("activity.storeList is null", activity.storeList);
        assertNotNull("addStoreField is null", addStoreField);
    }

    public void testAddStoreButton_functionality(){
        //addStoreButton should be disabled if addStoreField is empty.
        assertTrue("'Add' button is enabled", !addStoreButton.isEnabled());
        //addStoreButton should be enabled if addStoreField is not empty.
        final String test_string = "Test Store";
        final int oldSize = activity.storeList.size();
        solo.enterText(addStoreField, test_string);
        getInstrumentation().waitForIdleSync();
        assertTrue("'Add' button is disabled", addStoreButton.isEnabled());
        //storeList should have 1 more item in it when addItemButton is clicked.
        solo.clickOnView(addStoreButton);
        getInstrumentation().waitForIdleSync();
        assertEquals("activity.storeList did not increase in size", oldSize+1, activity.storeList.size());
        //The latest item in DBItemList should be the one just added
        assertEquals("The latest item in activity.storeList is not one that was previously added", test_string, activity.storeList.get(activity.storeList.size() - 1).getName());
        //The new Item should be visible on the screen.
        assertTrue("Test Store is not visible on the screen", solo.searchText(test_string, true));
    }

    public void testAddStoreDuplication(){
        //storeList should not allow duplicates
        final String test_string = "Test Store";
        final int oldSize = activity.storeList.size();
        solo.enterText(addStoreField, test_string);
        getInstrumentation().waitForIdleSync();
        solo.clickOnView(addStoreButton);
        getInstrumentation().waitForIdleSync();
        solo.enterText(addStoreField, test_string);
        getInstrumentation().waitForIdleSync();
        solo.clickOnView(addStoreButton);
        getInstrumentation().waitForIdleSync();
        assertEquals("activity.storeList has registered a duplicate", oldSize + 1, activity.storeList.size());
    }

    public void testStoreSelection(){
        //Pressing one of the Stores should start a MainActivity
        solo.clickOnText("Trader Brun's");
        getInstrumentation().waitForIdleSync();
        Activity mainActivity = getInstrumentation().waitForMonitorWithTimeout(mainActivityMonitor, 3000);
        assertNotNull("mainActivity is null", mainActivity);
        mainActivity.finish();
    }

    public void testUIVisibility(){
        assertTrue("'Add' button is not visible", View.VISIBLE == addStoreButton.getVisibility());
        assertTrue("'Add a new store' input field is not visible", View.VISIBLE == addStoreField.getVisibility());
        assertTrue("'Store List' text is not visible", View.VISIBLE == solo.getView(R.id.textView3).getVisibility());
        assertTrue("Store list view is not visible", View.VISIBLE == solo.getView(R.id.listView2).getVisibility());
    }


}
