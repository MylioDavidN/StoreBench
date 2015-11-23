package com.dnguyen.storebench;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;



public class MyViewPager extends ViewPager {

	private boolean enabled;
	
	public MyViewPager(Context context) {
        super(context);
        this.enabled = true;
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }
    
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
    	if (this.enabled) {
    		return super.onTouchEvent(event);
    		

        } else {
        	return false;
        }
            	
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	if (this.enabled) {
    		return super.onInterceptTouchEvent(event);
    		

        } else {
        	return false;
        }
        //return super.onInterceptTouchEvent(event);
    }
    
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
}
