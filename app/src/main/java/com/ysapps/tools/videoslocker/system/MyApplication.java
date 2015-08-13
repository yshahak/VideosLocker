package com.ysapps.tools.videoslocker.system;

import android.app.Application;


/**
 * Created by yshahak on 07/01/2015.
 */
public class MyApplication extends Application {
    //Tracker tracker;
    String TAG = getClass().getSimpleName();
    final String TRIM = "TRIM_MEMORY";
    final String FLARRY = "";
    public final static boolean logger = true;



    @Override
    public void onCreate()  {
        super.onCreate();
        //FlurryAgent.setLogEnabled(false);
        //FlurryAgent.init(this, FLARRY);

    }



}