package com.cs320.shoptimize.shoptimizeapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.support.v4.view.GestureDetectorCompat;

import com.amazonaws.com.google.gson.Gson;
import com.amazonaws.services.dynamodbv2.model.ScanResult;


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
    EditText addField;
    TextView storename;
    ListView lv;
    ArrayAdapter<Item> adapter;
    TabHost tabs;
    private String current_Store; //tells which store's list to access by index
    static AmazonClientManager clientManager = null;



    public MainActivity() throws Exception {
        shoppingLists = new HashMap<String, DBItemList>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        addField = (EditText) findViewById(R.id.add_item_field);

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
    protected void onResume(){
        super.onResume();
        final Button tripBtn = (Button) findViewById(R.id.button_floorplan);
        final Button addBtn = (Button) findViewById(R.id.add_item_button);
        final Button locBtn = (Button) findViewById(R.id.button_addLocs);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                items.addItem(addField.getText().toString(), false);
                Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
                addField.setText("");

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
                items.addFPPointsforInent();
                Intent floorplan = new Intent(getApplicationContext(), FloorplanActivity.class);
                floorplan.putExtra("XPOINTS", items.getXs());
                floorplan.putExtra("YPOINTS", items.getXs());
                startActivity(floorplan);

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
        current_Store = getIntent().getExtras().getString("storeNAME");
        final Item currItem = items.getItems().get(position);
        //replaceAll methods sanitize names to be suitable directory names
        String storeDirName = current_Store.replaceAll("\\W+", "");
        String itemDirName = currItem.getName().replaceAll("\\W+", "");
        File storeDir = new File(getApplicationContext().getFilesDir() + "/" + storeDirName + "/" + itemDirName);
        Log.v("directory", getApplicationContext().getFilesDir() + "/" + storeDirName + "/" + itemDirName);
        if(!storeDir.exists()){
            storeDir.mkdirs();
        }
        return storeDir;
    }
    private File createImageFile(int position) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(
        //        Environment.DIRECTORY_PICTURES);
        File storageDir = getCurrentDirectory(position);
        if(storageDir == null){
            Log.v("bad", "storageDir was null");
        }
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
                Log.v("lol", "photoFile failed.");

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            } else {
                Log.v("tag", "photoFile was equal to null.");
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
            coupCheck.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currItem.toggleCoupon();
                    coupText.setText(currItem.getCouponAsStr());

                }
            });
            TextView loc =  (TextView) row.findViewById(R.id.location);
            loc.setText("Location: " + currItem.getLocation());

            final ImageButton addCoupBtn = (ImageButton) row.findViewById(R.id.add_coupon_button);
            addCoupBtn.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  dispatchTakePictureIntent(finalPosition);
                                              }
                                          }

            );
            return row;
        }
    }




    private boolean tmp_populateShoppingLists(){
        DBItemList one;
        DBItemList two;
        try {
            one = new DBItemList();
            two = new DBItemList();
            shoppingLists.put("Trader Brun's", one);
            shoppingLists.put("Stop & Shop", two);
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
