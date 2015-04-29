package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.com.google.gson.Gson;
import com.amazonaws.com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gabe Markarian on 3/8/2015.
 */
public class StoreListActivity extends Activity {
    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    List<Store> storeList = new ArrayList<Store>();
    ArrayAdapter<Store> adapter;
    ListView listView;
    EditText addField;
    SharedPreferences storeListData; //How I am storing the data

    public void onCreate(Bundle icicle){
        super.onCreate(icicle);
        setContentView(R.layout.shop_list);
        storeListData = getApplicationContext().getSharedPreferences("storeListData", Context.MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.listView2);
        populateStoreList();
//        if(storeListData.contains("storeList")) { //Right now this part is doing the Retrieving
//            Gson gson = new Gson();
//            Type type = new TypeToken< List < Store >>() {}.getType();
//            String json = storeListData.getString("storeList","");
//            storeList = gson.fromJson(json, type);
//        }
        //storeList.remove(3);

        adapter = new ItemListAdapter(this, R.layout.shop_item,storeList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(selectItem);

       // addField = (EditText) findViewById(R.id.txt_addStore);
       // final Button addBtn = (Button) findViewById(R.id.but_addStore);

       /* addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String st = addField.getText().toString();
                if(st.length() < 2){
                    Toast.makeText(getApplicationContext(), "Enter a valid Shop name", Toast.LENGTH_SHORT).show();
                } else if(st.length() > 20){
                    Toast.makeText(getApplicationContext(), "Shop name must be 20 or fewer characters", Toast.LENGTH_SHORT).show();
                } else {
                    int match = 0;
                    for (Store s : storeList) {
                        if (s.getName().toLowerCase().equals(st.toLowerCase())) {
                            Toast.makeText(getApplicationContext(), "Shop has already been added", Toast.LENGTH_SHORT).show();
                            match++;
                            addField.setText("");
                            break;
                        }
                    }
                    if (match == 0) {
                        storeList.add(new Store(addField.getText().toString(), 2674));
                        Toast.makeText(getApplicationContext(), "Shop added", Toast.LENGTH_SHORT).show();
                        addField.setText("");
                    }
                }
            }
        }); */
       // SharedPreferences.Editor storeListEditor = storeListData.edit();
       // Gson gson = new Gson();
       // String json = gson.toJson(storeList);
       // storeListEditor.putString("storeList",json);
       // storeListEditor.apply();

    }
    @Override
    public void onPause() //Where we are saving
    {
        super.onPause();

        SharedPreferences.Editor storeListEditor = storeListData.edit();
        Gson gson = new Gson();
        String json = gson.toJson(storeList);
        storeListEditor.putString("storeList",json);
        storeListEditor.apply();
    }
   // @Override
    //public void onResume() //Ultimately this is where it is stored, if we setup everything
    //{
       // super.onResume();

        //if(storeListData.contains("storeList")) {
          //  Gson gson = new Gson();
          //  Type type = new TypeToken< List < Store >>() {}.getType();
          //  String json = storeListData.getString("storeList","");
           // storeList = gson.fromJson(json, type);
       // }

    //}

    private AdapterView.OnItemClickListener selectItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int i, long l) {
            Intent listScreen = new Intent(getApplicationContext(), MainActivity.class);
            listScreen.putExtra("storeNAME", storeList.get(i).getName());
            startActivity(listScreen);

        }
    };

    private void populateStoreList(){
        addStore("Trader Brun's",0001);
        addStore("Big Y Amherst", 0002);
        addStore("Other (no locations)", 0003);
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
