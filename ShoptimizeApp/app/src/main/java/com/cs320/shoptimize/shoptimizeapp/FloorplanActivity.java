package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gabe Markarian on 3/23/2015.
 */
public class FloorplanActivity extends Activity {

    final Context context = this;

    FPView fp;
    Button nextBtn;
    Button prevBtn;
    Button couponBtn;
    Button findItemBtn;
    Button doneBtn;
    Button postBtn;
    TextView nameField;
    String storeName;
    int count = 0;
    ArrayList<String>  names;
    ArrayList<Integer> ypoints;
    ArrayList<Integer> xpoints;
    String itemName;
    ArrayList<Item> foundItems;

    @Override
    protected void onCreate(Bundle icicle){
        super.onCreate(icicle);
        setContentView(R.layout.floorplan_screen);

        fp = (FPView) findViewById(R.id.floorplan_view);
        nameField = (TextView) findViewById(R.id.name_item);
        foundItems = new ArrayList<Item>();
        xpoints = getIntent().getExtras().getIntegerArrayList("XPOINTS");
        ypoints = getIntent().getExtras().getIntegerArrayList("YPOINTS");
        names   = getIntent().getExtras().getStringArrayList("NAMES");
        for(String s :names){
            Log.v("IN FPA", s);
        }
        storeName = getIntent().getExtras().getString("STORENAME");
        fp.getXYCollection(xpoints, ypoints);
        nameField.setText(names.get(count));
        nextBtn = (Button) findViewById(R.id.btn_next);
        prevBtn = (Button) findViewById(R.id.btn_prev);
        couponBtn = (Button) findViewById(R.id.btn_show);
        findItemBtn = (Button) findViewById(R.id.btn_findItem);
        postBtn     = (Button) findViewById(R.id.btn_post);
        doneBtn = (Button) findViewById(R.id.btn_done);
        nextBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                fp.setCoord(count);
                nameField.setText(names.get(count));

            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                fp.setCoord(count);
                nameField.setText(names.get(count));
                //todo: uncomment this code an it will show how the store trip keeps adding ot itself!
                /*for(String s : names){
                    Log.v("Item ", s);
                }*/
            }
        });

        couponBtn.setOnClickListener( new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent couponGallery = new Intent(getApplicationContext(), CouponGalleryActivity.class);
                                              couponGallery.putExtra("storeNAME", getIntent().getExtras().getString("storeNAME"));
                                              startActivity(couponGallery);
                                          }
                                      }


        );

        //CROWDSOURCING:  Ask for item name and a place on the floorplan
        //TODO: make a method that enables/disables buttons for the placing/not placing states
        findItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Update an Item Location"); //Set Alert dialog title here
                alert.setMessage("Type the name of the item you found"); //Message here

                // Set an EditText view to get user input
                final EditText input = new EditText(context);
                alert.setView(input);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //You will get as string input data in this variable.
                        // here we convert the input to a string and show in a toast.
                        itemName = input.getEditableText().toString();
                        //Toast.makeText(context, srt, Toast.LENGTH_LONG).show();
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                doneBtn.setVisibility(View.VISIBLE);
                setButtonGroup(true);
                fp.setPlacing(true);


            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foundItems.add(new Item(itemName, fp.sendTouchEvent()));
                doneBtn.setVisibility(View.INVISIBLE);
                setButtonGroup(false);
                fp.setPlacing(false);
                Log.v("END OF PLACE",foundItems.get(foundItems.size()-1).toString());
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Item i: foundItems){
                    //new InventoryListPoster(storeName, i).execute();
                    new InventoryListPoster("TraderBruns_InventoryList", i).execute();
                }
            }
        });
    }

    private void setButtonGroup(boolean placing){
        if(placing){
            //DONE:YES.  Next, coupon, prev, replace:disable.  Text: invisible
            doneBtn.setVisibility(View.VISIBLE);
            findItemBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(false);
            couponBtn.setEnabled(false);
            nameField.setVisibility(View.INVISIBLE);

        }
        else{
            //DONE: invisible.  Next, coupon, prev: enable, Text:visible
            doneBtn.setVisibility(View.INVISIBLE);
            nextBtn.setEnabled(true);
            findItemBtn.setEnabled(true);
            prevBtn.setEnabled(true);
            couponBtn.setEnabled(true);
            nameField.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();


    }

}
