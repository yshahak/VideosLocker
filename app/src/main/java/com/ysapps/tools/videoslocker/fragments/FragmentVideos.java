package com.ysapps.tools.videoslocker.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ysapps.tools.videoslocker.R;
import com.ysapps.tools.videoslocker.activities.ChangePasswordActivity;
import com.ysapps.tools.videoslocker.activities.MainActivity;
import com.ysapps.tools.videoslocker.system.Utils;
import com.ysapps.tools.videoslocker.system.VideoData;
import com.ysapps.tools.videoslocker.system.VideosRecycleAdapter;

import java.util.Map;
import java.util.Set;

/**
 * Created by yshahak on 12/08/2015.
 */
public class FragmentVideos extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>  {

    private RecyclerView rv;
    SharedPreferences pref;
    private MenuItem lockMenu;
    private ProgressBar progressBar;

    public static FragmentVideos newInstance(){
        return new FragmentVideos();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_videos, container, false);
        rv = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = (ProgressBar)root.findViewById(R.id.progress);
        return root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.MIME_TYPE
        };
        return(new CursorLoader(getActivity(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Video.Media.TITLE));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        rv.setAdapter(new VideosRecycleAdapter(c, getActivity(), lockMenu));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //rv.setAdapter(new VideosRecycleAdapter(null, getActivity()));
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        lockMenu = menu.findItem(R.id.action_lock_videos);
        if (rv != null) {
            VideosRecycleAdapter adapter = (VideosRecycleAdapter) rv.getAdapter();
            if (adapter != null)
                adapter.lockMenu = lockMenu;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_lock_videos){
            //startVideoRemoveSession((VideosRecycleAdapter)rv.getAdapter());
            new BitmapAsyncTask((VideosRecycleAdapter)rv.getAdapter()).execute();
            item.setVisible(false);
        } else {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class BitmapAsyncTask extends AsyncTask<Void, Integer, Void> {

        VideosRecycleAdapter adapter;
        private ProgressDialog dialog;

        public BitmapAsyncTask(VideosRecycleAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            dialog =  createDialog(adapter.videosToLock.size());
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Set<Map.Entry<Uri, String>> set =  adapter.videosToLock.entrySet();
            int count = 0;
            for (Object aSet : set) {
                Map.Entry pair = (Map.Entry) aSet;
                VideoData newVideo = Utils.removeFileToNewPlace(getActivity(), (Uri) pair.getKey(), (String) pair.getValue());
                count++;
                publishProgress(count);
                if (newVideo != null){
                    FragmentLocksVideos.addStringToPath((MainActivity) getActivity(), newVideo);
                }else{
                    //TODO handle remove file not worked
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();
            adapter.videosToLock.clear();
        }
    }

    private ProgressDialog createDialog(int size) {
        ProgressDialog dlg = new ProgressDialog(getActivity());
        dlg.setTitle("Processing your videos lock");
        dlg.setMax(size);
        dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        return(dlg);
    }


}
