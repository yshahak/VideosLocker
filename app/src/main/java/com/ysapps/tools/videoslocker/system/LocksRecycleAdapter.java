package com.ysapps.tools.videoslocker.system;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ysapps.tools.videoslocker.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by B.E.L on 16/08/2015.
 */
public class LocksRecycleAdapter extends RecyclerView.Adapter<LocksRecycleAdapter.ViewHolder>
        implements View.OnClickListener{

    private Context context;
    //private final int rowDpHeight = 64;
    ArrayList<VideoData> videoData= new ArrayList<>();


    public LocksRecycleAdapter(Context context, ArrayList<VideoData> videoData) {
        this.videoData = videoData;
        this.context = context;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup container;
        private TextView title;
        private ImageView thumbnail;
        private ImageView unlock;

        public ViewHolder(ViewGroup container) {
            super(container);
            this.container = container;
            container.setOnClickListener(LocksRecycleAdapter.this);
            this.title = (TextView)container.findViewById(R.id.text);
            this.thumbnail = (ImageView)container.findViewById(R.id.thumbnail);
            this.unlock = (ImageView)container.findViewById(R.id.unlock);
            this.unlock.setOnClickListener(LocksRecycleAdapter.this);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup container = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_restore, parent, false);
        return new ViewHolder(container);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        VideoData video = videoData.get(i);
        String path = video.newPath;
        String[] array = path.split("/");
        Bitmap bm = getThumb(path);
        if (bm != null)
            viewHolder.thumbnail.setImageBitmap(bm);
        viewHolder.title.setText(array[array.length - 1]);
        viewHolder.container.setTag(video);

    }

    public Bitmap getThumb(String filePath) {
        try {
            Uri location = Uri.parse(filePath);
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            ParcelFileDescriptor parcel = ParcelFileDescriptor.open(new File(location.getPath()), ParcelFileDescriptor.MODE_READ_ONLY);
            media.setDataSource(parcel.getFileDescriptor());
            Bitmap thumb = media.getFrameAtTime(0 , MediaMetadataRetriever.OPTION_CLOSEST );
            return thumb;
        } catch (FileNotFoundException|RuntimeException e) {
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
        if (v instanceof ImageView){ //click on the unlock button
            new RestoreAsyncTask().execute((VideoData) ((View) v.getParent()).getTag());
        } else { // click on the container
            String path = ((VideoData) v.getTag()).newPath;
            Utils.playVideo(context, path);
        }

    }

    private class RestoreAsyncTask extends AsyncTask<VideoData, Void, Boolean> {

        private ProgressDialog dialog;
        VideoData video;

        @Override
        protected void onPreExecute() {
            dialog =  createDialog();
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(VideoData... params) {
            video = params[0];
            return Utils.restoreVideoToOldPosition(context, video.newPath, video.originPath);
        }

       /* @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0]);

        }*/

        @Override
        protected void onPostExecute(Boolean restore) {
            dialog.dismiss();
            if (restore){
                int position = videoData.indexOf(video);
                videoData.remove(video);
                notifyItemRemoved(position);
            } else {
                Toast.makeText(context, "The restore action failed", Toast.LENGTH_LONG).show();
            }
        }
    }


    private ProgressDialog createDialog() {
        ProgressDialog dlg = new ProgressDialog(context);
        dlg.setTitle("Processing your videos unlock");
        dlg.setMax(1);
        dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        return(dlg);
    }



}