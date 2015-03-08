package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabe Markarian on 3/8/2015.
 */
public class StoreListActivity extends Activity {

    List<Store> storeList = new ArrayList<Store>();
    ArrayAdapter<Store> adapter;
    ListView listView;

    public void onCreate(Bundle icicle){
        super.onCreate(icicle);
        setContentView(R.layout.shop_list);

        listView = (ListView) findViewById(R.id.listView2);
        populateStoreList();

        adapter = new ItemListAdapter(this, R.layout.shop_item,storeList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(selectItem);

    }

    private AdapterView.OnItemClickListener selectItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int i, long l) {
            Intent listScreen = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(listScreen);

        }
    };

    private void populateStoreList(){
        addStore("Target",0001);
        addStore("BigY",0002);
        addStore("Dick's Sporting Goods",0003);
        addStore("Walmart",0004);
    }

    private void addStore(String name, int store_ID){
        storeList.add(new Store(name, store_ID));
    }

    class ItemListAdapter extends ArrayAdapter<Store> {


        public ItemListAdapter(Context context, int viewResource, List<Store> storeList){
            super(context, viewResource, storeList);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            LayoutInflater inflater = StoreListActivity.this.getLayoutInflater();
            View row = inflater.inflate(R.layout.shop_item, parent, false);

            final Store currStore = storeList.get(position);
            TextView name = (TextView) row.findViewById(R.id.store);
            name.setText(currStore.getName());

            return row;
        }

    }
}
