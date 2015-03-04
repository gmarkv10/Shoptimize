package com.cs320.shoptimize.shoptimizeapp;

/**
 * Created by Gabe Markarian on 3/2/2015.
 */
public class Item {

    private String name;
    private boolean coupon;




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

    public boolean getCoupon(){
        return coupon;
    }

    public String getCouponAsStr() {
        if (coupon) return "coupon found!";
        else return "no coupon.";
    }

}
