package com.cs320.shoptimize.shoptimizeapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
            imageview.setImageResource(mImages[position]);
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
