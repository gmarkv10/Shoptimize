package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
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
        Intent intent = getIntent();
        Bundle bundle;
        String string1;
        String currentStore;
        if(intent != null){
            bundle = intent.getExtras();
            if(bundle != null){
                string1 = bundle.getString("storeNAME");
                if(string1 != null){
                    currentStore = string1.replaceAll("\\W+", "");
                } else {
                    Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                    return bitmaps;
                }
            }else{
                Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                return bitmaps;
            }
        } else {
            Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
            return bitmaps;
        }
        Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_SHORT).show();
        File storeDir = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + currentStore);
        if(storeDir.exists()){
            File[] items = storeDir.listFiles();

            for(File file : items){
                Toast.makeText(getApplicationContext(), "found some files", Toast.LENGTH_SHORT).show();
                if(file.isFile()) {
                    String pathName = file.getAbsolutePath();
                    //Bitmap bitmap = BitmapFactory.decodeFile(pathName);
                    Bitmap bitmap = loadImage(pathName);
                    bitmaps.add(bitmap);

                }
            }
        } else {
            Log.v("dir", "could not find " + storeDir.getAbsolutePath());
        }
       // Toast.makeText(getApplicationContext(), "I made " + bitmaps.size() + " bitmaps.", Toast.LENGTH_SHORT).show();
        return bitmaps;

    }

    private Bitmap loadImage(String imgPath) {
        BitmapFactory.Options options;
        try {
            options = new BitmapFactory.Options();
            options.inSampleSize = 2;
           // Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
            FileInputStream fis = new FileInputStream(new File(imgPath));
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            return bitmap;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
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
            Context context = getApplicationContext();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.cgallery_item, container, false);
            ImageView imageview = (ImageView) itemView.findViewById(R.id.imageView);
            //imageview.setImageResource(mImages[position]);
            List<Bitmap> coupons = getCouponBitmaps();
            if(coupons.size() == 0){
                Log.v("bitmaps", "size was 0");
                Toast.makeText(getApplicationContext(), "no coupons", Toast.LENGTH_SHORT).show();
            } else {


                if(coupons.get(position) != null) {
                    imageview.setImageBitmap(coupons.get(position));
                    Toast.makeText(getApplicationContext(), "Displaying coupon " + position + " of size " + coupons.get(position).getHeight() + "x" + coupons.get(position).getWidth() + ".", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "coupon.get(position) returned null",Toast.LENGTH_SHORT).show();
                }
            }

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
