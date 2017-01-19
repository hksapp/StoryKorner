package com.hkapps.storykorner;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kamal on 10-01-2017.
 */
public class NotifyHolder extends RecyclerView.ViewHolder {

    public TextView liker_name, tag, cmttag;
    public ImageView notify_imgview;
    public TextView notify_time;
    public ImageView imgview;
    public ImageView notifStory;

    public NotifyHolder(View itemView) {
        super(itemView);

        liker_name = (TextView) itemView.findViewById(R.id.liker_name);
        notify_imgview = (ImageView) itemView.findViewById(R.id.notify_imgview);
        notify_time = (TextView) itemView.findViewById(R.id.notify_time);
        tag = (TextView) itemView.findViewById(R.id.tag);
        cmttag = (TextView) itemView.findViewById(R.id.cmttag);
        notifStory = (ImageView) itemView.findViewById(R.id.notifStory);
    }
}
