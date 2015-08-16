package com.ysapps.tools.videoslocker.system;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysapps.tools.videoslocker.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by B.E.L on 16/08/2015.
 */
public class LocksRecycleAdapter extends RecyclerView.Adapter<LocksRecycleAdapter.ViewHolder>
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context context;
    //private final int rowDpHeight = 64;
    ArrayList<VideoData> videoData= new ArrayList<>();


    public LocksRecycleAdapter(Context context, ArrayList<VideoData> videoData) {
        this.videoData = videoData;
        this.context = context;

    }

    @Override
    public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
        if (isChecked){
            //ViewHolder viewHolder = (ViewHolder) checkBox.getTag();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup container;
        private TextView title;
        private ImageView thumbnail;
        private CheckBox checkBox;

        public ViewHolder(ViewGroup container) {
            super(container);
            this.container = container;
            container.setOnClickListener(LocksRecycleAdapter.this);
            this.title = (TextView)container.findViewById(R.id.text);
            this.thumbnail = (ImageView)container.findViewById(R.id.thumbnail);
            this.checkBox = (CheckBox)container.findViewById(R.id.checkBox);
            checkBox.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup container = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(container);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        String path = videoData.get(i).newPath;
        String[] array = path.split("/");
        Bitmap bm = getThumb(path);
        if (bm != null)
            viewHolder.thumbnail.setImageBitmap(bm);
        viewHolder.title.setText(array[array.length - 1]);
        viewHolder.container.setTag(path);

    }

    public Bitmap getThumb(String filePath) {
        try {
            Uri location = Uri.parse(filePath);
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            ParcelFileDescriptor parcel = ParcelFileDescriptor.open(new File(location.getPath()), ParcelFileDescriptor.MODE_READ_ONLY);
            media.setDataSource(parcel.getFileDescriptor());
            Bitmap thumb = media.getFrameAtTime(0 , MediaMetadataRetriever.OPTION_CLOSEST );
            return thumb;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return videoData.size();
    }

    @Override
    public void onClick(View v) {
        String path = (String) v.getTag();
        Utils.playVideo(context, path);
    }




}