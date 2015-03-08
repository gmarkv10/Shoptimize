package com.cs320.shoptimize.shoptimizeapp;


/**
 * Created by Vivek K and Gabe M. on 3/6/2015.
 */
public class Store {

    private String name;
    private int store_ID;

    public Store(String name, int store_ID){
        this.name = name;
        this.store_ID = store_ID;
    }

    public String getName(){
        return name;
    }

    public int getID(){
        return store_ID;
    }


}
