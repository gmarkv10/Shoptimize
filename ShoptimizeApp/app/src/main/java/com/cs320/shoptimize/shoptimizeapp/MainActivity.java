package com.cs320.shoptimize.shoptimizeapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.List;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.support.v4.view.GestureDetectorCompat;



public class MainActivity extends ActionBarActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private GestureDetectorCompat gestureDetector;

    List<Item> items = new ArrayList<Item>();
    EditText addField;

    ListView lv;
    ArrayAdapter<Item> adapter;
    TabHost tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addField = (EditText) findViewById(R.id.add_item_field);
        final Button addBtn = (Button) findViewById(R.id.add_item_button);
        lv = (ListView) findViewById(R.id.listView);
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

        populateSL();

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                items.add(new Item(addField.getText().toString(), false));
                Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
                addField.setText("");
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

        adapter = new ItemListAdapter(this, R.layout.listview_item, items );
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


    private void populateSL(){
        addItem("Peanuts", false);
        addItem("Butter", false);
        addItem("Salt", false);
        addItem("Sardines", false);
    }

    private void addItem(String name, boolean coupon){
        items.add(new Item(name, coupon));
    }

    class ItemListAdapter extends ArrayAdapter<Item> {


        public ItemListAdapter(Context context, int viewResource, List<Item> list){
            super(context, viewResource, list);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            View row = inflater.inflate(R.layout.listview_item, parent, false);

            final Item currItem = items.get(position);
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
