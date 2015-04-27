package com.cs320.shoptimize.shoptimizeapp;

import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Gabe Markarian on 3/2/2015.
 */
public class Item {

    private String name;
    private boolean coupon;
    private String location;
    private String fname;




    public Item(String name, boolean coupon){
        this.name = name;
        this.coupon = coupon;
        this.fname = "none";
    }

    public Item(String name, String loc){
        this.name = name;
        this.location = loc;
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

    public String getFname(){
        return fname;
    }


    public void setFilename(String newFilename){
        this.fname = newFilename;
    }

    public String getCouponAsStr() {
        if (coupon) return "coupon found!";
        else return "no coupon.";
    }

    @Override
    public String toString(){
        return "Name is " + name + " with " + location + " location";
    }

    public static void getLocations(String store, List<Item> sl){

    }

}
