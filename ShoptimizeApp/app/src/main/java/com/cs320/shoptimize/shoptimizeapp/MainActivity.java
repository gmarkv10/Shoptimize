package com.cs320.shoptimize.shoptimizeapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.support.v4.view.GestureDetectorCompat;

import com.amazonaws.services.dynamodbv2.model.ScanResult;


public class MainActivity extends ActionBarActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private GestureDetectorCompat gestureDetector;

    //List<DBItemList> shoppingLists;// =  new ArrayList<DBItemList>();
    DBItemList items;
    //DBItemList items = new DBItemList();
    //DELETE AFTER LOCAL MEMORY IS IMPLEMENTED


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
        //ADDITION so that main activity can deal with multiple stores, will probably become a hastable in BETA
        setContentView(R.layout.activity_main);
        //TODO:  replace with load from internal memory
        try {
            shoppingLists.put("Trader Brun's", new DBItemList());
            shoppingLists.put("Stop & Shop", new DBItemList());
        }catch(Exception e){
            e.printStackTrace();
        }
        //END TODO
        current_Store = getIntent().getExtras().getString("storeNAME");  //intent from store list screen
        storename = (TextView) findViewById(R.id.storename);
        items = shoppingLists.get(current_Store);
        storename.setText(current_Store);

        clientManager = new AmazonClientManager(this);

        addField = (EditText) findViewById(R.id.add_item_field);
        final Button addBtn = (Button) findViewById(R.id.add_item_button);
        final Button locBtn = (Button) findViewById(R.id.button_addLocs);
        lv = (ListView) findViewById(R.id.listView);



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

        adapter = new ItemListAdapter(this, R.layout.listview_item, items.getItems() );
        lv.setAdapter(adapter);


        this.gestureDetector = new GestureDetectorCompat(this, this);
        gestureDetector.setOnDoubleTapListener(this);

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





    class ItemListAdapter extends ArrayAdapter<Item> {


        public ItemListAdapter(Context context, int viewResource, List<Item> list){
            super(context, viewResource, list);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
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

            return row;
        }

    }

/*    public class ItemListActivity extends ListActivity{
        @Override
        protected void onCreate(Bundle bundle){
            super.onCreate(bundle);

            ItemListAdapter ila = new ItemListAdapter(this, R.layout.listview_item, items);

            setListAdapter(ila);

        }

        @Override
        protected void onListItemClick(ListView l, View v, int position, long id ){
            String item = (String) getListAdapter().getItem(position);
            Toast.makeText(this, item + " selected", Toast.LENGTH_SHORT).show();

        }


    }*/
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





    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        return;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

}
