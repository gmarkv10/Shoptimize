package com.cs320.shoptimize.shoptimizeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.support.v4.view.GestureDetectorCompat;

import com.amazonaws.com.google.gson.Gson;


public class MainActivity extends ActionBarActivity {

    private GestureDetectorCompat gestureDetector;

    //List<DBItemList> shoppingLists;// =  new ArrayList<DBItemList>();
    DBItemList items;
    //DBItemList items = new DBItemList();
    //DELETE AFTER LOCAL MEMORY IS IMPLEMENTED

    // Parameters for Coupon photo taking and storage:
    String mCurrentPhotoPath = null;
    static final int REQUEST_TAKE_PHOTO = 1;

    HashMap<String, DBItemList> shoppingLists;
    //List<Item> items = new ArrayList<Item>();
    AutoCompleteTextView addField;
    TextView storename;
    ListView lv;
    ArrayAdapter<Item> adapter;
    TabHost tabs;
    private String current_Store; //tells which store's list to access by index
    static AmazonClientManager clientManager = null;
    SharedPreferences itemListData; //How I am storing the data
    //TODO: populate inventory with db items
    String[] inventory = {"Ice Cream", "Cream", "Carrots", "Mango", "Herring"};



    public MainActivity() throws Exception {
        shoppingLists = new HashMap<String, DBItemList>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemListData = getApplicationContext().getSharedPreferences("itemListData", Context.MODE_PRIVATE);
        //TODO:  replace with load from internal memory
        try {
            shoppingLists.put("Trader Brun's", new DBItemList());
            shoppingLists.put("Stop & Shop", new DBItemList());
        }catch(Exception e){
            e.printStackTrace();
        }

        //Setup the shopping list with the right labels
        current_Store = getIntent().getExtras().getString("storeNAME");  //intent from store list screen
        storename = (TextView) findViewById(R.id.storename);
        items = shoppingLists.get(current_Store);
        storename.setText(current_Store);
        addField = (AutoCompleteTextView) findViewById(R.id.add_item_field);

        //AUTOCOMPLETE functionality
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(this,
                                                                            android.R.layout.select_dialog_item,
                                                                            inventory);
        addField.setThreshold(1);
        addField.setAdapter(autoCompleteAdapter);

        //AWS CONNECTION
        clientManager = AmazonClientManager.getInstance();
        clientManager.setContext(this);



        //TODO: get points from database!
        //xs.add(50); xs.add(299); xs.add(517); xs.add(643); //xs.add(150); xs.add(40);
        //ys.add(50); ys.add(451); ys.add(495); ys.add(302); //ys.add(150); ys.add(200);

        //Populate the shoppinglist  TODO: items needs to be populated from memory
        lv = (ListView) findViewById(R.id.listView);
        adapter = new ItemListAdapter(this, R.layout.listview_item, items.getItems() );
        lv.setAdapter(adapter);

    }
    @Override
    public void onPause() //Where we are saving
    {
        super.onPause();

        SharedPreferences.Editor itemListEditor = itemListData.edit();
        Gson gson = new Gson();
        String json = gson.toJson(items);
        itemListEditor.putString(current_Store,json);
        itemListEditor.apply();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(itemListData.contains(current_Store)) { //Right now this part is doing the Retrieving
            Gson gson = new Gson();
            String json = itemListData.getString(current_Store,"");
            items = gson.fromJson(json, DBItemList.class);
        }
        final Button tripBtn = (Button) findViewById(R.id.button_floorplan);
        final Button addBtn = (Button) findViewById(R.id.add_item_button);
        final Button locBtn = (Button) findViewById(R.id.button_addLocs);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String s = addField.getText().toString();
                if(!items.contains(s)){
                    items.addItem(addField.getText().toString(), false);
                    Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
                    addField.setText("");
                    //Log.v("COORD", items.getItems().get(0).getLocation());
                }
                else{
                    Toast.makeText(getApplicationContext(), "This item has already been added", Toast.LENGTH_SHORT).show();
                    addField.setText("");
                }

            }
        });
        locBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                items.populateLocations();

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);


            }
        });
        tripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.populateLocations();
                final Intent floorplan = new Intent(getApplicationContext(), FloorplanActivity.class);

                Handler handler = new Handler();
                Toast.makeText(getApplicationContext(), "Loading Locations...", Toast.LENGTH_LONG).show();
                handler.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        items.addFPPointsforIntent();
                        floorplan.putExtra("STORENAME", current_Store);
                        floorplan.putExtra("XPOINTS", items.getXs());
                        floorplan.putExtra("YPOINTS", items.getYs());
                        floorplan.putExtra("NAMES",   items.getNames());
                        for(Item i: items.getItems() ){
                            Log.v("IN MAIN:", i.getName());
                        }

                        floorplan.putExtra("storeNAME", getIntent().getExtras().getString("storeNAME"));
                        startActivity(floorplan);
                    }
                }, 2000);


            }
        });
        addField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addBtn.setEnabled(!addField.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lv = (ListView) findViewById(R.id.listView);
        adapter = new ItemListAdapter(this, R.layout.listview_item, items.getItems() );
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


    /*
Created by Peter
I needed an organized way to keep track of coupon image files. Within the Shoptimize directory in Android internal storage,
these methods create (if it doesn't exist yet) and return a directory in the form Shoptimize/name_of_store/item_name
to keep track of coupon files that could be updated by the user.
*/
    private File getCurrentDirectory(int position){
        /*
        current_Store = getIntent().getExtras().getString("storeNAME");
        final Item currItem = items.getItems().get(position);
        //replaceAll methods sanitize names to be suitable directory names
        String storeDirName = current_Store.replaceAll("\\W+", "");
        String itemDirName = currItem.getName().replaceAll("\\W+", "");
        File storeDir = new File(getApplicationContext().getFilesDir().getPath() + "/" + storeDirName + "/" + itemDirName);
        Log.v("directory", getApplicationContext().getFilesDir() + "/" + storeDirName + "/" + itemDirName);
        if(!storeDir.exists()){
            storeDir.mkdirs();
        }
        return storeDir;
        */
        current_Store = getIntent().getExtras().getString("storeNAME");
        String storeDirName = current_Store.replaceAll("\\W+", "");
        File storeDir = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + storeDirName);
        if(!storeDir.exists()){
            storeDir.mkdirs();
        }
        return storeDir;
    }

    // File storageDir = Environment.getExternalStoragePublicDirectory(
    //        Environment.DIRECTORY_PICTURES);

    private File createImageFile(int position) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "SHOPT_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        //File storageDir = getCurrentDirectory(position);
        if(storageDir == null){
            Toast.makeText(getApplicationContext(), "storageDir was null", Toast.LENGTH_SHORT).show();
        }
        // new File(Environment.getExternalStorageDirectory(), name + ".jpg");
        File image1 = new File(storageDir, imageFileName + ".jpg");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(int position) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(position);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(), "photo file failed", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            } else {
                Toast.makeText(getApplicationContext(), "photoFile null", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class ItemListAdapter extends ArrayAdapter<Item> {


        public ItemListAdapter(Context context, int viewResource, List<Item> list){
            super(context, viewResource, list);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            final int finalPosition = position;
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            View row = inflater.inflate(R.layout.listview_item, parent, false);

            final Item currItem = items.getItems().get(position);
            TextView name = (TextView) row.findViewById(R.id.list_item);
            name.setText(currItem.getName());
            final TextView coupText = (TextView) row.findViewById(R.id.coupon);
            coupText.setText(currItem.getCouponAsStr());
            final CheckBox coupCheck = (CheckBox) row.findViewById(R.id.couponCheck);
            coupCheck.setChecked(currItem.getCoupon());
            //currItem.toggleCoupon();
            //coupText.setText(currItem.getCouponAsStr());
            //TextView loc =  (TextView) row.findViewById(R.id.location);
            //loc.setText("Location: " + currItem.getLocation());

            final ImageButton addCoupBtn = (ImageButton) row.findViewById(R.id.add_coupon_button);
            addCoupBtn.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  dispatchTakePictureIntent(finalPosition);
                                                  currItem.toggleCoupon();
                                                  coupText.setText(currItem.getCouponAsStr());
                                              }
                                          }

            );
            final ImageButton delItemBtn = (ImageButton) row.findViewById(R.id.btn_delete);
            delItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.getItems().remove(currItem);
                    Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();

                }
            });
            return row;
        }
    }




    private boolean tmp_populateShoppingLists(){
        DBItemList one;
        DBItemList two;
        DBItemList three;
        try {
            one = new DBItemList();
            two = new DBItemList();
            three = new DBItemList();
            shoppingLists.put("Trader Joe's", one);
            shoppingLists.put("Stop & Shop", two);
            shoppingLists.put("Big Y", three);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }



}

//POSSIBLY USEFUL TABBOST CODE, haven't figured it out just yet
    /*        tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setup();
        TabHost.TabSpec tabSpec = tabs.newTabSpec("shoppinglist");
        tabSpec.setContent(R.id.tab_list);
        tabSpec.setIndicator("SHOPPINGLIST");
        tabs.addTab(tabSpec);

        tabSpec = tabs.newTabSpec("floorplan");
        tabSpec.setContent(R.id.tab_list);
        tabSpec.setIndicator("FLOORPLAN");
        tabs.addTab(tabSpec);*/
