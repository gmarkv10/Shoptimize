package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Gabe Markarian on 3/23/2015.
 */
public class FloorplanActivity extends Activity {

    FPView fp;
    Button nextBtn;
    Button prevBtn;
    Button couponBtn;


    @Override
    protected void onCreate(Bundle icicle){
        super.onCreate(icicle);
        setContentView(R.layout.floorplan_screen);

        fp = (FPView) findViewById(R.id.floorplan_view);

        ArrayList<Integer> xpoints = getIntent().getExtras().getIntegerArrayList("XPOINTS");
        ArrayList<Integer> ypoints = getIntent().getExtras().getIntegerArrayList("YPOINTS");
        fp.getXYCollection(xpoints, ypoints);

        nextBtn = (Button) findViewById(R.id.btn_next);
        prevBtn = (Button) findViewById(R.id.btn_prev);
        couponBtn = (Button) findViewById(R.id.btn_show);

        nextBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fp.nextCoord();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fp.prevCoord();
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
