package com.ysapps.tools.videoslocker.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

/**
 * Created by B.E.L on 13/08/2015.
 */
public class RecycleViewActivity  extends AppCompatActivity {
    private RecyclerView rv = null;

    public void setAdapter(RecyclerView.Adapter adapter) {
        getRecyclerView().setAdapter(adapter);
    }

    public RecyclerView.Adapter getAdapter() {
        return(getRecyclerView().getAdapter());
    }

    public void setLayoutManager(RecyclerView.LayoutManager mgr) {
        getRecyclerView().setLayoutManager(mgr);
    }

    public RecyclerView getRecyclerView() {
        if (rv == null) {
            rv = new RecyclerView(this);
            setContentView(rv);
        }

        return(rv);
    }
}