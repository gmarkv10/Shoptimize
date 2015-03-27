package com.cs320.shoptimize.shoptimizeapp;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by Humdilla on 3/18/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity activity;
    private Solo solo;
    private EditText addItemField;
    private View addItemButton;
    private View showLocationsButton;
    private DBItemList mockList;

    public MainActivityTest(){
        super(MainActivity.class);
    }

    public void setUp()throws Exception{
        try{
            mockList = new DBItemList();
        }catch(Exception e){}
        //Set up intent
        Intent intent = new Intent(getInstrumentation().getContext(), MainActivity.class);
        intent.putExtra("storeNAME", "Trader Brun's");
        setActivityIntent(intent);
        //Set up activity
        activity = getActivity();
        activity.items = mockList;
        solo = new Solo(getInstrumentation(), activity);
        //Set up views
        addItemField = (EditText)solo.getView(R.id.add_item_field);
        addItemButton = solo.getView(R.id.add_item_button);
        showLocationsButton = solo.getView(R.id.button_addLocs);
    }

    public void tearDown() throws Exception{
        addItemField = null;
        addItemButton = null;
        showLocationsButton = null;
        mockList = null;
        solo.finishOpenedActivities();
    }

    public void testPreconditions(){
        assertNotNull("activity is null", activity);
        solo.assertCurrentActivity("Current Activity", MainActivity.class);
        assertNotNull("activity.items is null", activity.items);
        assertNotNull("activity.items.items is null", activity.items.getItems());
        assertTrue("Number of items in DBList is 0", (activity.items.getItems().size() != 0));
        assertNotNull("addItemField is null", addItemField);
        assertNotNull("addItemButton is null", addItemButton);
        assertNotNull("showLocationsButton is null", showLocationsButton);
    }

    /**
     * This method tests whether or not the addItemField works. It should be cleared after
     * pressing the "Add Item" button.
     **/
    public void testAddItemField_contents(){
        //The contents of addItemField should not change without user interaction
        final String test_string = "Test Item";
        solo.enterText(addItemField, test_string);
        getInstrumentation().waitForIdleSync();
        assertEquals("addItemField's input text has changed", test_string, addItemField.getText().toString());
        //Clicking on the "Add Item" button should clear addItemField's input text
        solo.clickOnView(addItemButton);
        getInstrumentation().waitForIdleSync();
        assertEquals("addItemField's input text is not empty", "", addItemField.getText().toString());
    }

    /**
     * This method tests whether or not the addItemButton works. It should be increasing the
     * size of the item list by 1. The last item in the item list should be the one we just added.
     **/
    public void testAddItemButton_functionality(){
        //addItemButton should be disabled if addItemField is empty.
        assertTrue("'Add Item' button is enabled", !addItemButton.isEnabled());
        //addItemButton should be enabled if addItemField is not empty.
        final String test_string = "Test Item";
        final int oldSize = activity.items.getItems().size();
        solo.enterText(addItemField, test_string);
        getInstrumentation().waitForIdleSync();
        assertTrue("'Add Item' button is disabled", addItemButton.isEnabled());
        //DBItemList should have 1 more item in it when addItemButton is clicked.
        solo.clickOnView(addItemButton);
        getInstrumentation().waitForIdleSync();
        assertEquals("activity.items.items did not increase in size", oldSize+1, activity.items.getItems().size());
        //The latest item in DBItemList should be the one just added
        assertEquals("The latest item in activity.item.items is not one that was previously added", test_string, activity.items.getItems().get(activity.items.getItems().size()-1).getName());
    }

    /**
     * This method tests whether or not the item list accepts duplicates. It shouldn't.
     **/
    public void testAddItemDuplication(){
        //DBItemList should not allow duplicates
        final String test_string = "Test Item";
        final int oldSize = activity.items.getItems().size();
        solo.enterText(addItemField, test_string);
        getInstrumentation().waitForIdleSync();
        solo.clickOnView(addItemButton);
        getInstrumentation().waitForIdleSync();
        solo.enterText(addItemField, test_string);
        getInstrumentation().waitForIdleSync();
        solo.clickOnView(addItemButton);
        getInstrumentation().waitForIdleSync();
        assertEquals("activity.items.items has registered a duplicate", oldSize+1, activity.items.getItems().size());
    }

    /**
     * This method tests whether or not the "Show Locations" button pulls item locations from
     * the database..
     **/
    public void testShowLocationsButton_functionality(){
        //locations should be null before pressing showLocationsButton
        assertNull("Item location is not null", activity.items.items.get(0).getLocation());
        //locations should be filled in after pressing showLocationsButton
        solo.clickOnView(showLocationsButton);
        getInstrumentation().waitForIdleSync();
        try {
            Thread.sleep(3000);
        }catch(InterruptedException e){}
        assertNotNull("Item location is null", activity.items.items.get(0).getLocation());
    }

    /**
     * This method tests the lifecycle of the activity. This test is focused on the
     * addItemField. addItemField should retain its input value after the activity is destroyed.
     **/
    public void testAddItemField_lifecycle(){
        //Entered text should be saved before activity destruction
        final String test_string = "Test Item";
        final int oldSize = activity.items.getItems().size();
        solo.enterText(addItemField, test_string);
        getInstrumentation().waitForIdleSync();
        activity.finish();
        activity = getActivity();
        assertEquals("State was not saved before activity destruction: addItemField input string differences", test_string, addItemField.getText().toString());
    }

    /**
     * This method tests the lifecycle of the activity. This test is focused on the
     * item list. The item list should retain its values after the activity is destroyed.
     **/
    public void testItemList_lifecycle(){
        //Store items should be saved before activity destruction
        final String test_string = "Test Item";
        final int oldSize = activity.items.getItems().size();
        solo.enterText(addItemField, test_string);
        getInstrumentation().waitForIdleSync();
        solo.clickOnView(addItemButton);
        getInstrumentation().waitForIdleSync();
        activity.finish();
        activity = getActivity();
        assertEquals("State was not saved before activity destruction: item list size differences", oldSize+1, activity.items.getItems().size());
    }
}
