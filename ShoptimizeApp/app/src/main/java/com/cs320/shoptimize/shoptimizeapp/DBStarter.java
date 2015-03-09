package com.cs320.shoptimize.shoptimizeapp;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Vincent Tse on 3/8/2015.
 */
public class DBStarter extends AsyncTask<ShoptimizeDB, Void, Void>{
    @Override
    protected Void doInBackground(ShoptimizeDB... params) {
        for(ShoptimizeDB db : params) {
            try {
                Log.v("DBINIT", "Database initializing...");
                db.init();
                Log.v("DBINIT", "Database done initializing...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

