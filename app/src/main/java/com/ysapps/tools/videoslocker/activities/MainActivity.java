package com.ysapps.tools.videoslocker.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ysapps.tools.videoslocker.R;
import com.ysapps.tools.videoslocker.system.Utils;
import com.ysapps.tools.videoslocker.system.VideosRecycleAdapter;

import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor>{
    private RecyclerView rv;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar); // define the toolBar as the actionBar of that Activity

        getLoaderManager().initLoader(0, null, this);
        rv = (RecyclerView) findViewById(R.id.my_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_lock_videos){
            startVideoRemoveSession((VideosRecycleAdapter)rv.getAdapter());
        }
        return super.onOptionsItemSelected(item);
    }

    private void startVideoRemoveSession(VideosRecycleAdapter adapter) {
        Set<Map.Entry<Uri, String>> set =  adapter.videosToLock.entrySet();
        for (Object aSet : set) {
            Map.Entry pair = (Map.Entry) aSet;
            Utils.removeFileToNewPlace(MainActivity.this, (Uri) pair.getKey(), (String) pair.getValue());
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.MIME_TYPE
        };
        return(new CursorLoader(this,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Video.Media.TITLE));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        rv.setAdapter(new VideosRecycleAdapter(c, this));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //((VideoAdapter)getAdapter()).setVideos(null);
    }




}
