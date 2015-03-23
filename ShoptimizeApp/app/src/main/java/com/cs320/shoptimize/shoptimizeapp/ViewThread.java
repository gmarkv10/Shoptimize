package com.cs320.shoptimize.shoptimizeapp;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Gabe Markarian on 3/23/2015.
 */
public class ViewThread extends Thread {
    private FPView pview;
    private SurfaceHolder holder;
    private boolean run = false;

    public ViewThread(FPView paintView){
        pview = paintView;
        holder = pview.getHolder();
    }

    public void setRunning(boolean run){
        this.run = run;
    }
    @Override
    public void run() {
        Canvas canvas = null;
        while (run) {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                pview.doDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
