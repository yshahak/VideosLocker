package com.ysapps.tools.videoslocker.system;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public static int getDpInPixels(int dpValue, Context ctx) {
        if (ctx != null) {
            float d = ctx.getResources().getDisplayMetrics().density;
            return (int) (dpValue * d); // margin in pixels
        } else {
            return dpValue;
        }
    }

    public static void removeFileToNewPlace(Context context, Uri path, String extension) {
        String[] array = extension.split("/");

        File dir = new File(context.getFilesDir() + File.separator + "videosFiles");

        dir.mkdirs();
        //File from = new File(path);
        InputStream in;
        try {
            in = context.getContentResolver().openInputStream(path);
            //in = new FileInputStream(from);
            File outputFile = new File(dir, "newVideo1." + array[array.length - 1]);
            FileOutputStream out = new FileOutputStream(outputFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            //from.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
