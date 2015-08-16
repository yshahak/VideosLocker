package com.ysapps.tools.videoslocker.system;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	private boolean enabled;

    
	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = true;

	}

	public MyViewPager(Context context) {
		super(context);
		this.enabled = true;
	}

	@Override
	public void setCurrentItem (int item, boolean smoothScroll){
		Log.d("CurrentItem smooth", Integer.toString(item));
		if (this.enabled)
			super.setCurrentItem(item, smoothScroll);
	}

 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Log.d("onTouchEvent", "yes");

		if (this.enabled) {
			try {
				return super.onTouchEvent(event);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public void setCurrentItem(int item) {
		Log.d("CurrentItem", Integer.toString(item));
		super.setCurrentItem(item);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		//Log.d("onInterceptTouchEvent", Boolean.toString(setEnable));
		//if (this.enabled ) {
			return super.onInterceptTouchEvent(event);
		//}
		//return false;
	}
	

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/*public void setSetEnabled(boolean enabled) {
		this.setEnable = enabled;
	}
	
	public boolean getSetEnabled() {
		return this.setEnable ;
	}*/
}
