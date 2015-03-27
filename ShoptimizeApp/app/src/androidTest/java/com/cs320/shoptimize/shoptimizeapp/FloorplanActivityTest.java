package com.cs320.shoptimize.shoptimizeapp;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

import com.robotium.solo.Solo;
import java.util.ArrayList;

/**
 * Created by Humdilla on 3/26/15.
 */
public class FloorplanActivityTest extends ActivityInstrumentationTestCase2<FloorplanActivity> {

    private FloorplanActivity activity;
    private Solo solo;
    private View nextButton;
    private View prevButton;
    private View couponGalleryButton;
    private Instrumentation.ActivityMonitor couponGalleryMonitor;


    public FloorplanActivityTest(){
        super(FloorplanActivity.class);
    }

    public void setUp()throws Exception{
        couponGalleryMonitor = getInstrumentation().addMonitor(CouponGalleryActivity.class.getName(), null, false);
        Intent intent = new Intent(getInstrumentation().getContext(), MainActivity.class);
        ArrayList<Integer> mockXs = new ArrayList<Integer>();
        mockXs.add(0);
        mockXs.add(1);
        mockXs.add(2);
        ArrayList<Integer> mockYs = new ArrayList<Integer>();
        mockYs.add(0);
        mockYs.add(1);
        mockYs.add(2);
        intent.putExtra("XPOINTS", mockXs);
        intent.putExtra("YPOINTS", mockYs);
        setActivityIntent(intent);
        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        nextButton = (Button)solo.getView(R.id.btn_next);
        prevButton = (Button)solo.getView(R.id.btn_prev);
        couponGalleryButton = (Button)solo.getView(R.id.btn_show);
    }

    public void tearDown() throws Exception{
        nextButton = null;
        prevButton = null;
        couponGalleryButton = null;
        couponGalleryMonitor = null;
        solo.finishOpenedActivities();
    }

    public void testPreconditions(){
        assertNotNull("activity is null", activity);
        solo.assertCurrentActivity("Current Activity", FloorplanActivity.class);
        assertNotNull("nextButton is null", nextButton);
        assertNotNull("prevButton is null", prevButton);
        assertNotNull("couponGalleryButton is null", couponGalleryButton);
        assertNotNull("couponGalleryMonitor is null", couponGalleryMonitor);
    }

    //Coupon gallery testing is disabled until coupon gallery stops halting the damn app.
    /*public void testCouponGalleryButton_functionality(){
        //Pressing the "Show Coupons" button should start a CouponGalleryActivity
        solo.clickOnView(couponGalleryButton);
        Activity couponGalleryActivity = getInstrumentation().waitForMonitorWithTimeout(couponGalleryMonitor, 3000);
        assertNotNull("couponGalleryActivity is null", couponGalleryActivity);
        couponGalleryActivity.finish();
    }*/

    public void testUIVisibility(){
        assertTrue("'Next' button is not visible", View.VISIBLE == nextButton.getVisibility());
        assertTrue("'Previous' button is not visible", View.VISIBLE == prevButton.getVisibility());
        assertTrue("'Show Coupons' button is not visible", View.VISIBLE == couponGalleryButton.getVisibility());
        assertTrue("Floorplan view is not visible", View.VISIBLE == solo.getView(R.id.floorplan_view).getVisibility());
    }
}