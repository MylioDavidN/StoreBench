package com.dnguyen.storebench;

import com.dnguyen.storebench.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{
	 private Context mContext;
	 
	    // Keep all Images in array
	    public Integer[] mThumbIds = {
	            R.drawable.youtube, R.drawable.angry, R.drawable.maps, R.drawable.pandora,
	            R.drawable.gmail, R.drawable.fb, R.drawable.twitter, R.drawable.temple,
	            R.drawable.cnn, R.drawable.abcnews, R.drawable.simpsons, R.drawable.accu,
	            R.drawable.nightly, R.drawable.gta, R.drawable.nfs, R.drawable.compass,
	            R.drawable.gyro, R.drawable.accel, R.drawable.proximity, R.drawable.barometer
	    };
	 
	    // Constructor
	    public ImageAdapter(Context c){
	        mContext = c;
	    }
	 
	    @Override
	    public int getCount() {
	        return mThumbIds.length;
	    }
	 
	    @Override
	    public Object getItem(int position) {
	        return mThumbIds[position];
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return 0;
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView = new ImageView(mContext);
	        imageView.setImageResource(mThumbIds[position]);
	        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
	        return imageView;
	    }

		
	 
}
