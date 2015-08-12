package com.ysapps.tools.videoslocker.system;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * Created by yshahak on 12/08/2015.
 */
public class Utils {

    public static Cursor getAllVideos(Context context){
        String[] projection = {
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME
        };
        Cursor cursor = new CursorLoader(context,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null, // Return all rows
                    null,
                    null).loadInBackground();
        return cursor;
    }
}
