package com.ysapps.tools.videoslocker.fragments;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

/**
 * Created by yshahak on 12/08/2015.
 */
public class FragmentVideos extends Fragment implements LoaderManager.LoaderCallbacks {

    private final int ID_LOADER_VIDEO = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_LOADER_VIDEO, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == ID_LOADER_VIDEO){
            return(new CursorLoader(
                        getActivity(),
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        null, //will return all available columns, not the most efficient approach, but it is convenient
                        null,
                        null,
                        MediaStore.Video.Media.TITLE)); //the sort ordering is alphabeta
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
