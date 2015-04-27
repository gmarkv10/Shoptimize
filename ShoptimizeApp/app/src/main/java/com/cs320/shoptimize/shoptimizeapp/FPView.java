package com.cs320.shoptimize.shoptimizeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Gabe Markarian on 3/23/2015.
 */
public class FPView extends SurfaceView implements SurfaceHolder.Callback {
    Bitmap bm;
    boolean draw = false;
    boolean placing = false;
    private ViewThread viewThread;
    private ArrayList<Integer> xs, ys;
    private int currentCoord = 0;
    int mX, mY;

    public FPView(Context context){
        super(context);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.bigy);
        getHolder().addCallback(this);
        viewThread = new ViewThread(this);
    }

    public FPView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.bigy);
        getHolder().addCallback(this);
        viewThread = new ViewThread(this);
    }

    Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected void doDraw(Canvas canvas) {
        if(placing){
           canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
           canvas.drawBitmap(bm, 0, 0, null);
           paint1.setColor(Color.MAGENTA);
           canvas.drawCircle(mX, mY, 25, paint1);
        }
        else{
        //super.onDraw(canvas);
        paint1.setColor(Color.CYAN);
        paint2.setColor(Color.RED);
        canvas.drawBitmap(bm, 0, 0, null);
        //Draw points
            for(int i = 0; i < xs.size(); i++) {
                paint1.setColor(Color.LTGRAY);
                if (i == currentCoord) {
                    canvas.drawCircle(xs.get(i), ys.get(i), 16, paint2);
                } else {
                    canvas.drawCircle(xs.get(i), ys.get(i), 12, paint1);
                }
                //DRAW CIRCLES BABY////////////////////////////////

                //CIRCLES UP IN THIS BIZITCH///////////////////////
                //dank circles yo. dank as shit
            }
        }
    }

    protected void toggleDraw(){
        draw = !draw;
    }
    protected void setPlacing(boolean b){
        placing = b;
    }
    protected boolean getPlacing(){ return placing;}


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
        currentCoord++;
        doDraw(new Canvas());//%xs.size();
    }

    protected void prevCoord(){
        currentCoord--;
        doDraw(new Canvas());//%xs.size();
    }

    protected void setCoord( int idx){
        currentCoord = idx;
        doDraw(new Canvas());
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mX = (int) event.getX();// - bm.getWidth() / 2;
        mY = (int) event.getY();// - bm.getHeight() / 2;
        logTouchEvent();
        //setPlacing(true);
        return super.onTouchEvent(event);
    }

    public void logTouchEvent(){
        Log.v("Touch Registered at: ", "{" + mX + "," + mY + "}");
        Log.v("Current coord:", " " + currentCoord);
    }

    public String sendTouchEvent(){
        //Log.v("SendTouchEvent -- ", Integer.toString(mX)+","+Integer.toString(mY));
        return Integer.toString(mX)+","+Integer.toString(mY);
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
