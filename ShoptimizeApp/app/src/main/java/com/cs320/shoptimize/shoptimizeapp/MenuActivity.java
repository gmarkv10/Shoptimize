package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.sql.BatchUpdateException;

/**
 * Created by Gabe Markarian on 3/6/2015.
 */
public class MenuActivity extends Activity {

    Button list;
    Button man;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);

        Log.v("TEST", "WORKING");

        list = (Button) findViewById(R.id.button_list);
        man = (Button) findViewById(R.id.button_man);

        list.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent listScreen = new Intent(getApplicationContext(), MainActivity.class);
                Log.v("TEST2", "WORKING");
                startActivity(listScreen);
            }

        });

    }
}
