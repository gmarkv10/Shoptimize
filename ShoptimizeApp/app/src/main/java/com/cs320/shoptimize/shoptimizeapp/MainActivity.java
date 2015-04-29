package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.support.v4.view.GestureDetectorCompat;

import com.amazonaws.com.google.gson.Gson;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;


public class MainActivity extends ActionBarActivity{

    private GestureDetectorCompat gestureDetector;

    final Context context = this;

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
    ArrayList<String> inventory = new ArrayList<String>();



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
            shoppingLists.put("Other (no locations)", new DBItemList());
            shoppingLists.put("Big Y Amherst", new DBItemList());
        }catch(Exception e){
            e.printStackTrace();
        }

        //Setup the shopping list with the right labels
        current_Store = getIntent().getExtras().getString("storeNAME");
        Log.v("PLS LOOK", current_Store);//intent from store list screen
        storename = (TextView) findViewById(R.id.storename);
        items = shoppingLists.get(current_Store);
        items.setContext(this);
        storename.setText(current_Store);
        addField = (AutoCompleteTextView) findViewById(R.id.add_item_field);

        //AUTOCOMPLETE functionality
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item,
                inventory);

        //AWS CONNECTION
        clientManager = AmazonClientManager.getInstance();
        clientManager.setContext(this);

        //Populate the "inventory" arrayList for autocomplete
        new DatabaseScanner(current_Store, addField, autoCompleteAdapter, this).execute();

        //Populate the shoppinglist  TODO: items needs to be populated from memory
        lv = (ListView) findViewById(R.id.listView);
        adapter = new ItemListAdapter(this, R.layout.listview_item, items.getItems() );
        lv.setAdapter(adapter);
        if(!current_Store.equals("Other (no locations)" ))
            items.populateLocations(current_Store); //start populating passively

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

        final ImageButton tripBtn = (ImageButton) findViewById(R.id.button_floorplan);
        final ImageButton addBtn = (ImageButton) findViewById(R.id.add_item_button);
        final Button locBtn = (Button) findViewById(R.id.button_addLocs);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(addField.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter an item, please.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String s = addField.getText().toString();

                    if (!items.contains(s)) {
                        items.addItem(addField.getText().toString(), false);
                        Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
                        addField.setText("");
                        items.populateLocations(current_Store);
                        adapter.notifyDataSetChanged();
                        //Log.v("COORD", items.getItems().get(0).getLocation());
                    } else {
                        Toast.makeText(getApplicationContext(), "This item has already been added", Toast.LENGTH_SHORT).show();
                        addField.setText("");
                    }
                }
            }
        });
        locBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                items.populateLocations(current_Store);

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
                if(!isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Could not connect to the database", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!current_Store.equals("Other (no locations)")) {
                        if (items.getItems().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "You have no items in your list!", Toast.LENGTH_SHORT).show();
                        } else {
                            final Intent floorplan = new Intent(getApplicationContext(), FloorplanActivity.class);
                            Handler handler = new Handler();
                            Toast.makeText(getApplicationContext(), "Loading Locations...", Toast.LENGTH_SHORT).show();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    items.addFPPointsforIntent();
                                    if (items.getXs().isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "We don't know where any of your items are!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        floorplan.putExtra("STORENAME", current_Store);
                                        floorplan.putExtra("XPOINTS", items.getXs());
                                        floorplan.putExtra("YPOINTS", items.getYs());
                                        floorplan.putExtra("NAMES", items.getNames());
                                        floorplan.putExtra("storeNAME", getIntent().getExtras().getString("storeNAME"));
                                        startActivity(floorplan);
                                    }
                                }
                            }, 500);

                        }


                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("This is your 'Other' list"); //Set Alert dialog title here
                        alert.setMessage("Use it for lists for stores we don't currently keep data for!"); //Message here
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();

                    }


                }
            }
        });

        lv = (ListView) findViewById(R.id.listView);
        adapter = new ItemListAdapter(this, R.layout.listview_item, items.getItems() );
        lv.setAdapter(adapter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    private File createImageFile(int position) throws IOException {
        // Create an image file name
        String storeDirName = current_Store.replaceAll("\\W+", "");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "SHOPT_" + storeDirName+ "_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Item currItem = items.getItems().get(position);
        currItem.setFilename(image.getName());
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
                Log.v("PIctIntent", "Failed to create image file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            } else {
                Log.v("what","what did you do");
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
            coupCheck.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    if(currItem.getCoupon()){
                        coupCheck.setEnabled(true);
                        coupCheck.setChecked(true);
                        File storageDir = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES);
                        File[] files = storageDir.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                return Pattern.matches("\\b"+ currItem.getFname() + "\\b", filename);
                            }
                        });
                        if(files.length == 1){
                            files[0].delete();
                            Log.v("couponDel","coupon for " + currItem.getName()+ " deleted");
                            currItem.setFilename("none");
                            coupCheck.setChecked(false);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.v("delCoupon", "something went wrong here");
                        }

                    } else {
                        Log.v("delCoupon", "no coupon to delete");
                    }
                }
            });
            //currItem.toggleCoupon();
            //coupText.setText(currItem.getCouponAsStr());
            //TextView loc =  (TextView) row.findViewById(R.id.location);
            //loc.setText("Location: " + currItem.getLocation());
            final ImageButton addCoupBtn = (ImageButton) row.findViewById(R.id.add_coupon_button);
            addCoupBtn.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  dispatchTakePictureIntent(finalPosition);
                                                  //            coupCheck.setChecked(currItem.toggleCoupon());
                                                  //  adapter.notifyDataSetChanged();
                                                  //             coupText.setText(currItem.getCouponAsStr());
                                              }
                                          }
            );
            final ImageButton delItemBtn = (ImageButton) row.findViewById(R.id.btn_delete);
            delItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File storageDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);
                    File[] files = storageDir.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return Pattern.matches("\\b"+ currItem.getFname() + "\\b", filename);
                        }
                    });
                    if(files.length == 1) {
                        Log.v("coupon","coupon deleted");
                        files[0].delete();
                    } else {
                        Log.v("delItem","coupon not deleted");
                    }
                    items.getItems().remove(currItem);
                    Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            });
            return row;
        }
    }
}
