package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Gabe Markarian on 3/23/2015.
 */
public class FloorplanActivity extends Activity {

    final Context context = this;

    FPView fp;
    ImageButton nextBtn;
    ImageButton prevBtn;
    ImageButton couponBtn;
    ImageButton findItemBtn;
    ImageButton doneBtn;
    TextView nameField;
    String storeName;
    int count = 0;
    ArrayList<String>  names;
    ArrayList<Integer> ypoints;
    ArrayList<Integer> xpoints;
    String itemName;
    ArrayList<Item> foundItems;
    ArrayList<String> fnames;

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
        fnames = getIntent().getExtras().getStringArrayList("filenamesDB");
        storeName = getIntent().getExtras().getString("STORENAME");
        fp.setBitMap(storeName);
        fp.getXYCollection(xpoints, ypoints);
        nameField.setText(names.get(count));
        nextBtn = (ImageButton) findViewById(R.id.btn_next);
        prevBtn = (ImageButton) findViewById(R.id.btn_prev);
        couponBtn = (ImageButton) findViewById(R.id.btn_show);
        findItemBtn = (ImageButton) findViewById(R.id.btn_findItem);
        doneBtn = (ImageButton) findViewById(R.id.btn_done);
        nextBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == names.size() -1 ){
                    Toast.makeText(getApplicationContext(), "Last item! Show your coupons!", Toast.LENGTH_SHORT).show();
                    couponBtn.setColorFilter(Color.argb(255, 255, 246, 67 ), PorterDuff.Mode.MULTIPLY);
                }
                else {
                    count++;
                    fp.setCoord(count);
                    nameField.setText(names.get(count));
                }

            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0){
                    Toast.makeText(getApplicationContext(), "You haven't even started!", Toast.LENGTH_SHORT).show();
                }
                else {
                    count--;
                    fp.setCoord(count);
                    nameField.setText(names.get(count));
                }
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
                String[] nomenclature = new String[names.size()];
                nomenclature = names.toArray(nomenclature);

                // Set an EditText view to get user input
                final AutoCompleteTextView input = new AutoCompleteTextView(context);
                ArrayAdapter<String> autoComp = new ArrayAdapter<String>(context,
                        android.R.layout.select_dialog_item,
                        nomenclature);
                input.setThreshold(1);
                input.setAdapter(autoComp);



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
                nameField.setText("Tap where the item is!");
                //doneBtn.setVisibility(View.VISIBLE);
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
                nameField.setText(names.get(count));
                Toast.makeText(getApplicationContext(), "Thank you for crowdsourcing!", Toast.LENGTH_SHORT).show();
                Log.v("END OF PLACE",foundItems.get(foundItems.size()-1).toString());
            }
        });

//        postBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new InventoryListPoster("TraderBruns_InventoryList", foundItems, context).execute();
//            }
//        });
    }

    private void setButtonGroup(boolean placing){
        if(placing){
            //DONE:YES.  Next, coupon, prev, replace:disable.  Text: invisible
            doneBtn.setVisibility(View.VISIBLE);
            findItemBtn.setVisibility(View.INVISIBLE);
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(false);
            couponBtn.setEnabled(false);
            nameField.setVisibility(View.INVISIBLE);
            doneBtn.setVisibility(View.VISIBLE);

        }
        else{
            //DONE: invisible.  Next, coupon, prev: enable, Text:visible
            findItemBtn.setVisibility(View.VISIBLE);
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
        new InventoryListPoster("TraderBruns_InventoryList", foundItems, context).execute();
        super.onDestroy();
    }

}
