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
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ysapps.tools.videoslocker.R;
import com.ysapps.tools.videoslocker.system.ContractListFragment;

/**
 * Created by yshahak on 12/08/2015.
 */
public class VideosFragment extends
            ContractListFragment<VideosFragment.Contract> implements
            LoaderManager.LoaderCallbacks<Cursor>,
            SimpleCursorAdapter.ViewBinder {
    private ImageLoader imageLoader;

    @Override
    public void onAttach(Activity host) {
        super.onAttach(host);

        ImageLoaderConfiguration ilConfig=
                    new ImageLoaderConfiguration.Builder(getActivity()).build();

        imageLoader=ImageLoader.getInstance();
        imageLoader.init(ilConfig);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        String[] from=
                    { MediaStore.Video.Media.TITLE, MediaStore.Video.Media._ID };
        int[] to= { android.R.id.text1, R.id.thumbnail };
        SimpleCursorAdapter adapter=
                    new SimpleCursorAdapter(getActivity(), R.layout.row, null,
                                from, to, 0);

        adapter.setViewBinder(this);
        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CursorAdapter adapter=(CursorAdapter)getListAdapter();
        Cursor c=(Cursor)adapter.getItem(position);
        int uriColumn=c.getColumnIndex(MediaStore.Video.Media.DATA);
        int mimeTypeColumn=
                    c.getColumnIndex(MediaStore.Video.Media.MIME_TYPE);

        getContract().onVideoSelected(c.getString(uriColumn),
                    c.getString(mimeTypeColumn));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return(new CursorLoader(
                    getActivity(),
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null, null, null,
                    MediaStore.Video.Media.TITLE));
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
            Uri video=
                        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    c.getInt(column));
            DisplayImageOptions opts = new DisplayImageOptions.Builder()
                        .showImageOnLoading(android.R.drawable.presence_video_online)
                        .build();

            imageLoader.displayImage(video.toString(), (ImageView)v, opts);

            return(true);
        }

        return(false);
    }

    interface Contract {
        void onVideoSelected(String uri, String mimeType);
    }
}