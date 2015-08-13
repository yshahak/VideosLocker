package com.ysapps.tools.videoslocker.fragments;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ysapps.tools.videoslocker.R;
import com.ysapps.tools.videoslocker.system.ContractListFragment;

/**
 * Created by yshahak on 12/08/2015.
 */
public class FragmentVideos extends ContractListFragment<VideosFragment.Contract>
            implements LoaderManager.LoaderCallbacks<Cursor>, SimpleCursorAdapter.ViewBinder {

    private final int ID_LOADER_VIDEO = 0;
    ImageLoader imageLoader;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] from = { MediaStore.Video.Media.TITLE, MediaStore.Video.Media._ID };
        int[] to= { android.R.id.text1, R.id.thumbnail };
        SimpleCursorAdapter adapter=
                    new SimpleCursorAdapter(
                                getActivity(),
                                R.layout.row, // the layout inflate
                                null,         //cursor
                                from,         //the columns we want from the content provider
                                to,           //this ids inside the layout
                                0); //flags
        adapter.setViewBinder(this); //for bind the thumbnail and the results from cursor
        setListAdapter(adapter);
        getLoaderManager().initLoader(ID_LOADER_VIDEO, null, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ImageLoaderConfiguration ilConfig = new ImageLoaderConfiguration.Builder(activity).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ilConfig);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == ID_LOADER_VIDEO){
            String[] projection = {
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.TITLE
            };
            return(new CursorLoader(
                        getActivity(),
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        projection, //will return all available columns, not the most efficient approach, but it is convenient
                        null,
                        null,
                        MediaStore.Video.Media.TITLE)); //the sort ordering is alphabeta
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        ((CursorAdapter)getListAdapter()).swapCursor(c);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter)getListAdapter()).swapCursor(null);
    }

    @Override
    public boolean setViewValue(View v, Cursor c, int column) {
        if (column == c.getColumnIndex(MediaStore.Video.Media._ID)) {
            Uri video = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, c.getInt(column));
            DisplayImageOptions opts = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.ic_launcher)
                        .build();
            imageLoader.displayImage(video.toString(), (ImageView)v, opts);
            return(true);
        }
        return false;
    }


}
