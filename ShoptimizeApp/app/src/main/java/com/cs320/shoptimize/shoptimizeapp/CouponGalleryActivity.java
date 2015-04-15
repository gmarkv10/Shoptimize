package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 3/24/2015.
 */
public class CouponGalleryActivity extends Activity {

    List<Bitmap> coupons = null;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_gallery);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        CouponPageAdapter adapter = new CouponPageAdapter();
        viewPager.setAdapter(adapter);
        coupons = this.getCouponBitmaps();
    }

    private List<File> getCouponFiles(){
        List<File> files = new ArrayList<File>();
        String string1 = getIntent().getExtras().getString("storeNAME");
        String currentStore = string1.replaceAll("\\W+", "");
        File storeDir = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + currentStore);
        File[] items = storeDir.listFiles();
        for(File file : items){
            if(file.isFile()) {
                files.add(file);
            }
        }
        if (items == null || items.length == 0){
            Toast.makeText(getApplicationContext(), "couldn't get files", Toast.LENGTH_SHORT).show();
        }
        return files;
    }

    private List<Bitmap> getCouponBitmaps(){
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        String string1 = getIntent().getExtras().getString("storeNAME");
        String currentStore = string1.replaceAll("\\W+", "");
        File storeDir = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + currentStore);
        if(storeDir.exists()){
            File[] items = storeDir.listFiles();
            for(File file : items){
                if(file.isFile()) {
                    String pathName = file.getPath();
                    String canonicalPathName = null;
                    String absolutePathName = file.getAbsolutePath();
                    try {
                        canonicalPathName = file.getCanonicalPath();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "IOEXCEPTION", Toast.LENGTH_SHORT).show();
                    }
                    Bitmap bitmap = null;
                    bitmap = decodeSampledBitmapFromFile(pathName, 720, 720);
                    if(bitmap != null){
                        Toast.makeText(getApplicationContext(), "1NOT NULL", Toast.LENGTH_SHORT).show();
                    } else {
                        bitmap = decodeSampledBitmapFromFile(canonicalPathName, 720, 720);
                        if(bitmap != null){
                            Toast.makeText(getApplicationContext(), "2NOT NULL", Toast.LENGTH_SHORT).show();
                        } else {
                            bitmap = decodeSampledBitmapFromFile(absolutePathName, 720, 720);
                            if(bitmap != null){
                                Toast.makeText(getApplicationContext(), "3NOT NULL", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "4NULL " + file.getName(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    bitmaps.add(bitmap);
                }
            }
        } else {
            Log.v("dir", "could not find " + storeDir.getAbsolutePath());
        }
       // Toast.makeText(getApplicationContext(), "I made " + bitmaps.size() + " bitmaps.", Toast.LENGTH_SHORT).show();
        return bitmaps;

    }

     /* private Bitmap loadImage(String imgPath) {
        BitmapFactory.Options options;
        try {
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;

            Toast.makeText(getApplicationContext(), "decode finished",Toast.LENGTH_SHORT).show();
            return bitmap;
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "exception",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "i returned null",Toast.LENGTH_SHORT).show();
        return null;
    } */

    public Bitmap decodeSampledBitmapFromFile(String filePath,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

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

            List<File> files = getCouponFiles();
            if(files == null){
                Toast.makeText(getApplicationContext(), "files are null",Toast.LENGTH_SHORT).show();
                return itemView;
            }
            if (files.size() == 0){
                Toast.makeText(getApplicationContext(), "size is 0",Toast.LENGTH_SHORT).show();
                return itemView;
            }
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
