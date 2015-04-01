package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gabe Markarian on 3/23/2015.
 */
public class FloorplanActivity extends Activity {

    FPView fp;
    Button nextBtn;
    Button prevBtn;
    Button couponBtn;
    TextView nameField;
    int count = 0;
    ArrayList<String>  names;
    ArrayList<Integer> ypoints;
    ArrayList<Integer> xpoints;

    @Override
    protected void onCreate(Bundle icicle){
        super.onCreate(icicle);
        setContentView(R.layout.floorplan_screen);

        fp = (FPView) findViewById(R.id.floorplan_view);
        nameField = (TextView) findViewById(R.id.name_item);

        xpoints = getIntent().getExtras().getIntegerArrayList("XPOINTS");
        ypoints = getIntent().getExtras().getIntegerArrayList("YPOINTS");
        names   = getIntent().getExtras().getStringArrayList("NAMES");
        fp.getXYCollection(xpoints, ypoints);
        nameField.setText(names.get(count));
        nextBtn = (Button) findViewById(R.id.btn_next);
        prevBtn = (Button) findViewById(R.id.btn_prev);
        couponBtn = (Button) findViewById(R.id.btn_show);

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
                                              startActivity(couponGallery);
                                          }
                                      }


        );
    }

}
