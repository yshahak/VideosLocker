package com.ysapps.tools.videoslocker.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ysapps.tools.videoslocker.R;
import com.ysapps.tools.videoslocker.system.Utils;
import com.ysapps.tools.videoslocker.system.numberAdapter;


/**
 * Created by B.E.L on 05/11/2014.
 */
public class ChangePasswordActivity extends Activity implements GridView.OnItemClickListener, View.OnClickListener {

    public static final String KEY_LOCK_SCREEN_PASSWORD = "lock_passWord";
    Handler handler;
    GridView grid;
    AbsListView.LayoutParams params;
    StringBuilder build = new StringBuilder();
    String confirm;
    TextView passField;
    SharedPreferences pref;
    boolean first, confirmation;
    String[] passHide = {"_  _  _  _", "*  _  _  _", "*  *  _  _", "*  *  *  _", "*  *  *  *"};
    int gridWidth, gridHeight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Settings.System.putInt(getContentResolver(), "lockscreen_sounds_enabled", 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);*/
        setContentView(R.layout.pass_picker);
        findViewById(R.id.Cancel).setOnClickListener(this);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        grid = (GridView)findViewById(R.id.grid);
        grid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridWidth = grid.getWidth() / 3;
                gridHeight = grid.getHeight() / 4;
                params = new GridView.LayoutParams(gridWidth, gridHeight);
                numberAdapter adapter = new numberAdapter(getBaseContext(), params);
                grid.setAdapter(adapter);
                grid.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        grid.setOnItemClickListener(this);
        passField = (TextView)findViewById(R.id.pass_field);
        handler = new Handler();
        if (pref.getString(KEY_LOCK_SCREEN_PASSWORD, "0").equals("0")){ //setting passCode
            findViewById(R.id.Cancel).setVisibility(View.GONE);
            setPassCode();
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 10)
            position = 0;
        else
            position++;
        if (!(position == 10 || position == 12)) {
            build.append(Integer.toString(position));
            if (build.length() < passHide.length)
                passField.setText(passHide[build.length()]);
            if (build.length() == 4) {
                if (first){
                    confirm = build.toString();
                    resetLayout();
                    setConfirmation();
                }
                else if (confirmation){
                    if (confirm.equals(build.toString())){
                        pref.edit().putString(KEY_LOCK_SCREEN_PASSWORD, build.toString()).apply();
                        toast("passCode successfully set");
                        if (getParent() == null) {
                            setResult(Activity.RESULT_OK);
                        } else {
                            getParent().setResult(Activity.RESULT_OK);
                        }
                        closeActivity();
                    } else {
                        setPassCode();
                        toast("PassCode not match, pick new");
                        Utils.vibrate(this, 500);
                        passField.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_circles));
                        resetLayout();
                    }
                }
                else { //User want to change password, need to confirm the old
                    if (Utils.checkCode(PreferenceManager.getDefaultSharedPreferences(this), build)) {
                        setPassCode();
                        toast("passCode match");
                        resetLayout();
                    } else {
                        toast("wrong passCode, try again");
                        Utils.vibrate(this, 500);
                        passField.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_circles));
                        resetLayout();
                    }
                }
            } else if (build.length() > 4){
                toast("wrong passCode, try again");
                Utils.vibrate(this, 500);
                passField.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_circles));
                resetLayout();
            }
        }
        else if (position == 12 && build.length() > 0) {
            build.deleteCharAt(build.length() - 1);
            passField.setText(passHide[build.length()]);
        }
    }

    void setPassCode(){
        ((TextView)findViewById(R.id.edit)).setText("pick passCode");
        ((TextView)findViewById(R.id.old_PassCode)).setText("pick your desire passCode");
        first = true;
        confirmation = false;
        confirm = null;
    }

    void setConfirmation(){
        ((TextView)findViewById(R.id.edit)).setText("confirm passCode");
        ((TextView)findViewById(R.id.old_PassCode)).setText("confirm your desire passCode");
        toast("Confirm your passCode");
        confirmation = true;
        first = false;
    }

    void resetLayout(){
        build.delete(0, build.length());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                passField.setText(passHide[0]);
            }
        }, 200);
    }

    void toast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    void closeActivity(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 200);
    }

    @Override
    public void onClick(View view) {

        closeActivity();
    }
}
