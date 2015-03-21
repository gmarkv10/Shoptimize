package com.cs320.shoptimize.shoptimizeapp;

import java.util.List;

/**
 * Created by Gabe Markarian on 3/2/2015.
 */
public class Item {

    private String name;
    private boolean coupon;
    private String location;




    public Item(String name, boolean coupon){
        this.name = name;
        this.coupon = coupon;
    }

    public boolean toggleCoupon(){
        this.coupon = !coupon;
        return coupon;
    }

    public String getName(){
        return name;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean getCoupon(){
        return coupon;
    }

    public String getCouponAsStr() {
        if (coupon) return "coupon found!";
        else return "no coupon.";
    }

    @Override
    public String toString(){
        return "Name is " + name + " with " + coupon + " coupon";
    }

    public static void getLocations(String store, List<Item> sl){

    }

}
