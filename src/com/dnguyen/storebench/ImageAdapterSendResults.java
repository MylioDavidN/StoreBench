package com.dnguyen.storebench;

import com.dnguyen.storebench.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapterSendResults extends BaseAdapter {
	private Context mContext;
	 
    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.gmail, R.drawable.gplus, R.drawable.twitter, R.drawable.fb 
    };
 
    // Constructor
    public ImageAdapterSendResults(Context c){
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
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
        return imageView;
    }

	
 
}
