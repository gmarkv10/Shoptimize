package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.robotium.solo.Solo;

/**
 * Created by Humdilla on 3/25/15.
 */
public class MenuActivityTest extends ActivityInstrumentationTestCase2<MenuActivity> {

    private MenuActivity activity;
    private Solo solo;
    private View listButton;
    private View manButton;
    private Instrumentation.ActivityMonitor storeListMonitor;
    //private Instrumentation.ActivityMonitor manualMonitor;


    public MenuActivityTest(){
        super(MenuActivity.class);
    }

    public void setUp()throws Exception{
        storeListMonitor = getInstrumentation().addMonitor(StoreListActivity.class.getName(), null, false);
        //manualMonitor = getInstrumentation().addMonitor(Activity.class.getName(), null, false);
        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        listButton = solo.getView(R.id.button_list);
        manButton = solo.getView(R.id.button_man);
    }

    public void tearDown() throws Exception{
        listButton = null;
        manButton = null;
        solo.finishOpenedActivities();
    }

    public void testPreconditions(){
        assertNotNull("activity is null", activity);
        solo.assertCurrentActivity("Current Activity", MenuActivity.class);
        assertNotNull("listButton is null", listButton);
        assertNotNull("manButton is null", manButton);
    }

    /*

    This test should be reimplemented (and updated) when/if the browser becomes a part of the application. For now,
    the testing suite has no control over the browser. It can't see the status of the browser.

    public void testManButton_functionality(){
        //Pressing the "Manual" button should open the browser
        solo.clickOnView(manButton);
        Activity webActivity = getInstrumentation().waitForMonitorWithTimeout(manualMonitor, 3000);
        assertNotNull("Browser activity is null", webActivity);
        webActivity.finish();
    }
    */

    /**
     * This method tests whether or not the "Shopping Lists" button starts a
     * StoreListActivity. This test gives the app 3 seconds to do so.
     **/
    public void testStoreListButton_functionality(){
        //Pressing the "Shopping Lists" button should start a StoreListActivity
        solo.clickOnView(listButton);
        Activity storeListActivity = getInstrumentation().waitForMonitorWithTimeout(storeListMonitor, 3000);
        assertNotNull("storeListActivity is null", storeListActivity);
        storeListActivity.finish();
    }

    /**
     * This method tests the visibility of UI components.
     **/
    public void testUIVisibility(){
        assertTrue("'Shopping Lists' button is not visible", View.VISIBLE == listButton.getVisibility());
        assertTrue("'Manual' button is not visible", View.VISIBLE == manButton.getVisibility());
        assertTrue("'Shoptimize!' text is not visible", View.VISIBLE == solo.getView(R.id.screen_title).getVisibility());
    }


}
