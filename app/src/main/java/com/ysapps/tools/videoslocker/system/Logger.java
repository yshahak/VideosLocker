package com.ysapps.tools.videoslocker.system;

import android.util.Log;

/**
 * Created by B.E.L on 13/08/2015.
 */
public class Logger {
    public static void logD(String log){
        if (MyApplication.logger){
            Log.d("TAG", log);
        }
    }

    public static void logD(String tag, String log){
        if (MyApplication.logger){
            Log.d(tag, log);
        }
    }

    public static void logD(int log){
        if (MyApplication.logger){
            Log.d("TAG", Integer.toString(log));
        }
    }
}
