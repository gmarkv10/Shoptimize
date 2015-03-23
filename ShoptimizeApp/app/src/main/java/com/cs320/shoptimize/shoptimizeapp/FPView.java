package com.cs320.shoptimize.shoptimizeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Gabe Markarian on 3/23/2015.
 */
public class FPView extends SurfaceView implements SurfaceHolder.Callback {
    Bitmap bm;
    boolean draw = false;
    private ViewThread viewThread;
    private ArrayList<Integer> xs, ys;
    private int currentCoord = 0;

    public FPView(Context context){
        super(context);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.tbs);
        getHolder().addCallback(this);
        viewThread = new ViewThread(this);
    }

    public FPView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.tbs);
        getHolder().addCallback(this);
        viewThread = new ViewThread(this);
    }

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected void doDraw(Canvas canvas) {
        //super.onDraw(canvas);


        //canvas.drawCircle(200, 200, 10, paint);
        canvas.drawBitmap(bm, 0, 0, null);

        for(int i = 0; i < xs.size(); i++){
            paint.setColor(Color.RED);
            if(i == currentCoord) paint.setColor(Color.BLUE);
            canvas.drawCircle(xs.get(i), ys.get(i), 15, paint);
        }
    }

    protected void toggleDraw(){
        draw = !draw;
    }

    protected void getXYCollection(ArrayList<Integer> XS, ArrayList<Integer> YS){
        if(XS.size() == YS.size()) {
            this.xs = XS;
            this.ys = YS;
        }
        else{
            Log.v("FPVIEWERR", "Array list sizes do not match");
        }
    }

    //METHODS ASSOCIATED WITH BUTTONS IN FLOORPLAN ACTIVITY

    protected void nextCoord(){
        currentCoord++;//%xs.size();
    }

    protected void prevCoord(){
        currentCoord--;//%xs.size();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
        if (!viewThread.isAlive()) {
            viewThread = new ViewThread(this);
            viewThread.setRunning(true);
            viewThread.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (viewThread.isAlive()) {
            viewThread.setRunning(false);
        }
    }

}
