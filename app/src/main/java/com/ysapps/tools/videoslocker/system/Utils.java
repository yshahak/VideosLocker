package com.ysapps.tools.videoslocker.system;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ysapps.tools.videoslocker.activities.ChangePasswordActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

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

    public static VideoData removeFileToNewPlace(Context context, Uri uriOfMedia, String title) {
        //String[] array = extension.split("/");
        File dir = new File(context.getFilesDir() + File.separator + "videosFiles");
        //File dirTemp = new File(context.getExternalFilesDir(null) + File.separator + "videosFiles");
        dir.mkdirs();
        //dirTemp.mkdir();
        try {
            File outputFile = new File(dir, title);
            //File outputFileEx = new File(dirTemp, "newVideo1");

            String pathFromUri = getRealPathFromURI(context, uriOfMedia);
            File origin = new File(pathFromUri);
            boolean moved = origin.renameTo(outputFile);
            if (!moved){
                moved = copyFile(origin, outputFile);
            }
            if (moved) {
                //copyFile(origin, outputFileEx);
                //removeFromGallery(context, uriOfMedia);
                //playVideo(context, outputFileEx);
                VideoData videoData = new VideoData(title, pathFromUri, outputFile.getPath());
                return videoData;
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void playVideo(Context context, File myFile) {
        boolean read = myFile.setReadable(true, true);
        Logger.logD(read);
        String videoResource = myFile.getPath();
        Uri intentUri = Uri.fromFile(new File(videoResource));

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(intentUri, "video/*");
        context.startActivity(intent);
    }

    public static void playVideo(Context context, String path) {
        File dirTemp = new File(context.getExternalFilesDir(null) + File.separator + "videosFiles");
        dirTemp.mkdir();
        File outputFileEx = new File(dirTemp, "video");
        File origin = new File(path);
        origin.mkdir();
        copyFile(origin, outputFileEx);
        outputFileEx.setReadable(true, true);
        String videoResource = outputFileEx.getPath();
        Uri intentUri = Uri.fromFile(new File(videoResource));

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(intentUri, "video/*");
        context.startActivity(intent);
    }

    private static boolean copyFile(File src,File dst) {
        InputStream is ;
        try {
            is = new FileInputStream(src);
        OutputStream os=new FileOutputStream(dst);
        byte[] buff=new byte[1024];
        int len;
        while((len=is.read(buff))>0){
            os.write(buff,0,len);
        }
        is.close();
        os.close();
        return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void removeFromGallery(Context context, Uri uri){
        try {

            context.getContentResolver() .delete(uri, null, null);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static boolean checkCode(SharedPreferences pref, StringBuilder build) {
        String pass = build.toString();
        return (pass.equals(pref.getString(ChangePasswordActivity.KEY_LOCK_SCREEN_PASSWORD, "1234")));
    }

    public static void vibrate(Context ctx, int milSec){
        ((Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(milSec);
    }


    /**
     * @param context  context
     * @param videosDataList array to save
     */
    public static void save(final Context context, final ArrayList<VideoData> videosDataList){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (videosDataList != null) {
                    try {
                        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        //Integer count = 0;
                        //for (ArrayList<VideoData> arrayList : VideosDataList){
                            Gson gson = new GsonBuilder().create();
                            String array = gson.toJson(videosDataList);
                            edit.putString("json", array).apply();
                           // count++;
                        //}
                        //edit.putInt("jsonSize", count).apply();
                    } catch (ConcurrentModificationException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public static ArrayList<VideoData> load(Context context){
        ArrayList<VideoData> list;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            //for (Integer i = 0 ; i < size ;i++){
                Gson gson = new Gson();
                String array = preferences.getString("json", null);
                if (array == null)
                    return new ArrayList<>();
                Type type = new TypeToken<ArrayList<VideoData>>() {}.getType();
                list = gson.fromJson(array, type);
                //list.add(cells);
            //}
        } catch (ConcurrentModificationException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
        return list;
    }
}
