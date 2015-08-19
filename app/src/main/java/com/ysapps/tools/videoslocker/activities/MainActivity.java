package com.ysapps.tools.videoslocker.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.flurry.android.FlurryAgent;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.interstitial.RevMobFullscreen;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.viewpagerindicator.TitlePageIndicator;
import com.ysapps.tools.videoslocker.R;
import com.ysapps.tools.videoslocker.fragments.FragmentLocksVideos;
import com.ysapps.tools.videoslocker.fragments.FragmentVideos;
import com.ysapps.tools.videoslocker.system.MyViewPager;
import com.ysapps.tools.videoslocker.system.Utils;
import com.ysapps.tools.videoslocker.system.VideoData;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdDisplayListener {
    private static final String KEY_LOCK_VISIBLE = "lockVisible";
    String[] titles = {"Videos", "Locked"};
    private final String StartApp_id = "207259796";
    private StartAppAd startAppAd = new StartAppAd(this);;
    private RevMob revmob;
    private RevMobFullscreen fullscreen;
    private final int CODE_SET_FIRST_PASS = 1;
    private ArrayList<VideoData> videoData;
    public MyViewPager viewPager;
    private ViewGroup rootView;
    private StringBuilder build = new StringBuilder();
    private SharedPreferences pref;
    private final int TRIES_HITS = 3;
    private int tries;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_SET_FIRST_PASS && resultCode == RESULT_OK){
            startAppAd.showAd(this);
            startAppAd.loadAd();
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar); // define the toolBar as the actionBar of that Activity
            viewPager = (MyViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
            titleIndicator.setViewPager(viewPager);
            videoData = Utils.load(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_LOCK_VISIBLE, rootView != null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, StartApp_id, true);
        revmob = RevMob.startWithListener(this, revmobListener);
        if (savedInstanceState != null)
            loadFullscreen();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        if ("0".equals(pref.getString(ChangePasswordActivity.KEY_LOCK_SCREEN_PASSWORD, "0"))) {
            startActivityForResult(new Intent(this, ChangePasswordActivity.class), CODE_SET_FIRST_PASS);
        } else {
            if (savedInstanceState == null || savedInstanceState.getBoolean(KEY_LOCK_VISIBLE))
                showLockerWindow();
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar); // define the toolBar as the actionBar of that Activity
            getSupportActionBar().setTitle(null);

            viewPager = (MyViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
            titleIndicator.setViewPager(viewPager);
            videoData = Utils.load(this);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        FlurryAgent.onStartSession(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        startAppAd.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        startAppAd.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
        Utils.save(this, videoData);

    }

    @Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
        super.onBackPressed();
    }

    RevMobAdsListener revmobListener = new RevMobAdsListener() {
        @Override
        public void onRevMobSessionIsStarted() {
            loadFullscreen(); // pre-load it without showing it
        }

        @Override
        public void onRevMobAdNotReceived(String message) {
            super.onRevMobAdNotReceived(message);
        }

        @Override
        public void onRevMobAdReceived() {
            super.onRevMobAdReceived();
        }
    };

    public void loadFullscreen() {
        fullscreen = revmob.createFullscreen(this, null);
    }

    /*@Override
    public void onReceiveAd(Ad ad) {
//            if (interstitial){
//                startAppAd.showAd(); // show the ad
//                startAppAd.loadAd(); // load the next ad
//            } else{
//                fullscreen.show();
//            }
//        } else {
//            startAppAd.showAd(EntryActivity.this); // show the ad
//            startAppAd.loadAd(); // load the next ad
//        }
    }

    @Override
    public void onFailedToReceiveAd(Ad ad) {

    }*/

    @Override
    public void adHidden(Ad ad) {
        if (fullscreen != null)
            fullscreen.show();
    }

    @Override
    public void adDisplayed(Ad ad) {
    }
    @Override
    public void adClicked(Ad ad) {
    }

    @Override
    public void adNotDisplayed(Ad ad) {
        if (fullscreen != null)
            fullscreen.show();
    }



    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return FragmentVideos.newInstance();
                default:
                    return FragmentLocksVideos.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }


    }

    public Fragment getFragmentTag(int index) {
        String tag = "android:switcher:" + viewPager.getId() + ":" + index;
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public ArrayList<VideoData> getVideoData() {
        return videoData;
    }

    private void showLockerWindow() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN ,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager =  getWindowManager();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = (ViewGroup) inflater.inflate(R.layout.locker_screen, rootView);
        windowManager.addView(rootView, params);
    }

    private void removeLockWindow(){
        ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(rootView);
        rootView = null;
        startAppAd.showAd(this);
        startAppAd.loadAd();
    }

    private void resetScreen(View view){
        ((ImageView) view.findViewById(R.id.circle1)).setImageResource(R.drawable.blue_circle);
        ((ImageView) view.findViewById(R.id.circle2)).setImageResource(R.drawable.blue_circle);
        ((ImageView) view.findViewById(R.id.circle3)).setImageResource(R.drawable.blue_circle);
        ((ImageView) view.findViewById(R.id.circle4)).setImageResource(R.drawable.blue_circle);
        build.delete(0, build.length());
    }

    public void getInputPass(final View v){
        build.append(v.getTag());
        int size  = build.length();
        final View view = updateCircle(size);
        if (size == 4) {
            view.postDelayed(new Runnable(){
                @Override
                public void run() {
                    if (Utils.checkCode(pref, build)) {
                        removeLockWindow();
                    }else{
                        if (tries == TRIES_HITS){
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            removeLockWindow();
                        } else {
                            view.findViewById(R.id.circels).startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.shake_circles));
                            Utils.vibrate(v.getContext(), 500);
                            tries++;
                        }
                    }
                    resetScreen(view);
                }
            }, 100);
        }
    }

    public View updateCircle(int index) {
        ImageView view;
        switch (index) {
            case 1:
                view = (ImageView) rootView.findViewById(R.id.circle1);
                view.setImageResource(R.drawable.blue_circle_full);
                break;
            case 2:
                view = (ImageView) rootView.findViewById(R.id.circle2);
                view.setImageResource(R.drawable.blue_circle_full);
                break;
            case 3:
                view = (ImageView) rootView.findViewById(R.id.circle3);
                view.setImageResource(R.drawable.blue_circle_full);
                break;
            case 4:
                view = (ImageView) rootView.findViewById(R.id.circle4);
                view.setImageResource(R.drawable.blue_circle_full);
                break;
        }
        return rootView;
    }
}
