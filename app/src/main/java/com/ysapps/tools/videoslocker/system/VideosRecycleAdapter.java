package com.ysapps.tools.videoslocker.system;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.ysapps.tools.videoslocker.R;

import java.io.File;
import java.util.HashMap;

/**
 * Created by B.E.L on 13/08/2015.
 */
public class VideosRecycleAdapter extends RecyclerView.Adapter<VideosRecycleAdapter.ViewHolder>
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Cursor videosCursor;
    private String videoUri=null;
    private String videoMimeType = null;
    private int titleIndex, thumbnailIndex, idIndex, mimeTypeIndex;
    private Context context;
    private final int rowDpHeight = 64;
    private SparseBooleanArray hideArray = new SparseBooleanArray();
    public MenuItem lockMenu;
    public HashMap<Uri, String> videosToLock = new HashMap<>();

    public VideosRecycleAdapter(Cursor c, Context context, MenuItem lockMenu) {
        this.videosCursor = c;
        this.lockMenu = lockMenu;
        this.videosCursor.moveToFirst();
        this.idIndex = videosCursor.getColumnIndex(MediaStore.Video.Media._ID);
        this.titleIndex = videosCursor.getColumnIndex(MediaStore.Video.Media.TITLE);
        this.thumbnailIndex = videosCursor.getColumnIndex(MediaStore.Video.Media.DATA);
        this.mimeTypeIndex = videosCursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE);

        this.context = context;

    }

    @Override
    public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
        ViewHolder viewHolder = (ViewHolder) checkBox.getTag();
        Uri key = viewHolder.videoPath;
        if (isChecked){
            videosToLock.put(key, viewHolder.title.getText().toString());
        } else {
            if (videosToLock.containsKey(key))
                videosToLock.remove(key);
        }
        lockMenu.setVisible(videosToLock.size() > 0);
        /*boolean visible = videosToLock.size() > 0;
        if (visible != lockMenu.isVisible()) {
            ((Activity)context).invalidateOptionsMenu();
            lockMenu.setVisible(visible);
        }*/
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup container;
        private TextView title;
        private ImageView thumbnail;
        private CheckBox checkBox;
        public int id;
        public Uri videoPath;
        //public String mimeType;

        public ViewHolder(ViewGroup container) {
            super(container);
            this.container = container;
            this.title = (TextView)container.findViewById(R.id.text);
            this.thumbnail = (ImageView)container.findViewById(R.id.thumbnail);
            this.checkBox = (CheckBox)container.findViewById(R.id.checkBox);
            this.checkBox.setOnCheckedChangeListener(VideosRecycleAdapter.this);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup container = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(container);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if (hideArray.get(i)){
            hideContainer(viewHolder);
            return;
        }
        else
            showContainer(viewHolder);
        videosCursor.moveToPosition(i);
        final int id = videosCursor.getInt(idIndex);
        viewHolder.title.setText(videosCursor.getString(titleIndex));
        final Uri video = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
        viewHolder.videoPath = video;
        //viewHolder.mimeType = videosCursor.getString(mimeTypeIndex);

        viewHolder.id = id;
        viewHolder.checkBox.setTag(viewHolder);
        Picasso
                .with(context)
                .load(video)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .fit()
                .centerInside()
                .into(viewHolder.thumbnail, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        hideArray.put(i, true);
                        hideContainer(viewHolder);
                    }
                });
    }


    private void hideContainer(ViewHolder viewHolder) {
        ViewGroup.LayoutParams params = viewHolder.container.getLayoutParams();
        params.height = 0;
        viewHolder.container.setLayoutParams(params);
    }

    private void showContainer(ViewHolder viewHolder) {
        ViewGroup.LayoutParams params = viewHolder.container.getLayoutParams();
        if (params.height != 0)
            return;
        params.height = Utils.getDpInPixels(rowDpHeight, context);
        Logger.logD(params.height);
        viewHolder.container.setLayoutParams(params);
    }


    @Override
    public int getItemCount() {
        return videosCursor.getCount();
    }

    @Override
    public void onClick(View v) {
        Uri video=Uri.fromFile(new File(videoUri));
        Intent i=new Intent(Intent.ACTION_VIEW);

        i.setDataAndType(video, videoMimeType);
    }




}