package com.ysapps.tools.videoslocker.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ysapps.tools.videoslocker.R;
import com.ysapps.tools.videoslocker.activities.MainActivity;
import com.ysapps.tools.videoslocker.system.LocksRecycleAdapter;
import com.ysapps.tools.videoslocker.system.VideoData;

/**
 * Created by yshahak on 12/08/2015.
 */
public class FragmentLocksVideos extends Fragment {

    private RecyclerView rv;
    private SharedPreferences pref;


    public static FragmentLocksVideos newInstance(){
        return new FragmentLocksVideos();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_videos, container, false);
        rv = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        rv.setLayoutManager(mLayoutManager);
        MainActivity mainActivity = (MainActivity) getActivity();
        rv.setAdapter(new LocksRecycleAdapter(mainActivity, mainActivity.getVideoData()));
        return root;
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_lock_videos){
            //startVideoRemoveSession((VideosRecycleAdapter)rv.getAdapter());
        }
        return super.onOptionsItemSelected(item);
    }





    public static void addStringToPath(MainActivity mainActivity, VideoData video){
        //int size = mainActivity.getVideoData().size();
        FragmentLocksVideos fragmentLocksVideos = (FragmentLocksVideos)mainActivity.getFragmentTag(1);
        mainActivity.getVideoData().add(0, video);
        /*if (size == 0)
            fragmentLocksVideos.rv.getAdapter().notifyDataSetChanged();
        else*/
            fragmentLocksVideos.rv.getAdapter().notifyItemInserted(0);
    }


}
