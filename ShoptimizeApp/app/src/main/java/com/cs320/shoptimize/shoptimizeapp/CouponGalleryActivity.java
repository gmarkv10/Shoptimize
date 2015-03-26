package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 3/24/2015.
 */
public class CouponGalleryActivity extends Activity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_gallery);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        CouponPageAdapter adapter = new CouponPageAdapter();
        viewPager.setAdapter(adapter);
    }

    private List<Bitmap> getCouponBitmaps(){
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        String currentStore = getIntent().getExtras().getString("storeNAME").replaceAll("\\W+", "");
        //String storeDirPath = getApplicationContext().getFilesDir().getPath() + "/" + currentStore;
        File storeDir = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + currentStore);
        if(storeDir.exists()){
            File[] items = storeDir.listFiles();
            for(File file : items){
                String pathName = file.getAbsolutePath();
                Bitmap bitmap = BitmapFactory.decodeFile(pathName);
                bitmaps.add(bitmap);
            }
        } else {
            Log.v("dir", "could not find " + storeDir.getAbsolutePath());
        }
        return bitmaps;

    }

    private class CouponPageAdapter extends PagerAdapter{

        private int[] mImages = new int[] {
                R.drawable.first,
                R.drawable.second,
                R.drawable.third,
                R.drawable.fourth,
                R.drawable.fifth,
                R.drawable.sixth
        };

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }



        public Object instantiateItem(ViewGroup container, int position){
            Log.v("intItem", "instantiate item called");
            Context context = getApplicationContext();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.cgallery_item, container, false);
            ImageView imageview = (ImageView) itemView.findViewById(R.id.imageView);
            //imageview.setImageResource(mImages[position]);
            List<Bitmap> coupons = getCouponBitmaps();
            imageview.setImageBitmap(coupons.get(position));
            Log.v("intItem", "instantiate item finished");
            container.addView(itemView);
            return itemView;

        }
        public void destroyItem(ViewGroup container, int position, Object object){
            container.removeView((LinearLayout) object);
            Log.v("intItem", "destroy item finished");
        }


    }

}
