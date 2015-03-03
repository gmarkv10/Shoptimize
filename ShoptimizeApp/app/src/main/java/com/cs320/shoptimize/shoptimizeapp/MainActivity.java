package com.cs320.shoptimize.shoptimizeapp;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    List<Item> items = new ArrayList<Item>();

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lv = (ListView) findViewById(R.id.listView);
        populateSL();

        ArrayAdapter<Item> adapter = new ItemListAdapter(this, R.layout.listview_item, items );
        lv.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void populateSL(){
        addItem("Peanuts", true);
        addItem("Butter", false);
        addItem("Salt", false);
        addItem("Sardines", false);
        addItem("Condoms", false);
        addItem("Hatchet", false);
    }

    private void addItem(String name, boolean coupon){
        items.add(new Item(name, coupon));
    }

    public class ItemListAdapter extends ArrayAdapter<Item> {


        public ItemListAdapter(Context context, int viewResource, List<Item> list){
            super(context, viewResource, list);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            View row = inflater.inflate(R.layout.listview_item, parent, false);
            /*if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent , false);*/

            Item currItem = items.get(position);
            TextView name = (TextView) row.findViewById(R.id.list_item);
            name.setText(currItem.getName());
            TextView coupText = (TextView) row.findViewById(R.id.coupon);
            coupText.setText(currItem.getCouponAsStr());
            CheckBox coupCheck = (CheckBox) row.findViewById(R.id.couponCheck);
            coupCheck.setChecked(currItem.getCoupon());

            return row;
        }

    }




}
