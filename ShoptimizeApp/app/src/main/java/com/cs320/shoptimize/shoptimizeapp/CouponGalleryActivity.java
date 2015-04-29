package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Peter on 3/24/2015.
 */
public class CouponGalleryActivity extends Activity {

    List<Bitmap> coupons = new ArrayList<Bitmap>();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_gallery);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        CouponPageAdapter adapter = new CouponPageAdapter();
        viewPager.setAdapter(adapter);
        coupons = this.getCouponBitmaps();
        adapter.notifyDataSetChanged();
    }

    private List<Bitmap> getCouponBitmaps(){
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        String string1 = getIntent().getExtras().getString("storeNAME");
        final String currentStore = string1.replaceAll("\\W+", "");
        File storeDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        //File storeDir = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + currentStore);
            File[] files = storeDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return Pattern.matches("\\bSHOPT_" + currentStore + "_.*", filename);
                }
            });
            for(File file : files){
                if(file.isFile()) {
                    String pathName = file.getPath();
                    Bitmap bitmap = null;
                    bitmap = decodeSampledBitmapFromFile(pathName, 720, 720);
                    if(bitmap == null){
                        Log.v("BMFactory","was null");
                        continue;
                    }
                    bitmaps.add(bitmap);
                }
            }
            if(bitmaps.isEmpty()) {
                Log.v("dir", "i didn't find nothing ");
            }

        // Toast.makeText(getApplicationContext(), "I made " + bitmaps.size() + " bitmaps.", Toast.LENGTH_SHORT).show();
        return bitmaps;

    }

    public Bitmap decodeSampledBitmapFromFile(String filePath,
                                              int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        Log.d("CGA", "help! does this log even work?");

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        //Toast.makeText(getApplicationContext(), "inSampleSize " + inSampleSize +" " + height + "x" + width , Toast.LENGTH_SHORT).show();
        return inSampleSize;
    }


    private class CouponPageAdapter extends PagerAdapter{

        private int[] mImages = new int[] {

                R.drawable.second,

        };

        @Override
        public int getCount() {
            int size = coupons.size();
            Log.v("getcount()","I found " + size + "items.");
            return size;
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
/*
            List<File> files = getCouponFiles();
            if(files == null){
                Toast.makeText(getApplicationContext(), "files are null",Toast.LENGTH_SHORT).show();
                return itemView;
            }
            if (files.size() == 0){
                Toast.makeText(getApplicationContext(), "size is 0",Toast.LENGTH_SHORT).show();
                return itemView;
            }
            */
            //Picasso.with(getApplicationContext()).load(files.get(position)).resize(720, 720).into(imageview);

            //imageview.setImageResource(mImages[position]);
            //Picasso.with(getApplicationContext())load
            //List<Bitmap> coupons = getCouponBitmaps();
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

            container.addView(itemView);
            return itemView;

        }
        public void destroyItem(ViewGroup container, int position, Object object){
            container.removeView((LinearLayout) object);
            Log.v("intItem", "destroy item finished");
        }


    }

}