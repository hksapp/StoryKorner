package com.hkapps.storykorner;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by kamal on 11-12-2016.
 */

public class StoryHolder extends RecyclerView.ViewHolder {

    private static final String TAG = StoryHolder.class.getSimpleName();
    public TextView title_ui;
    public TextView story_ui;
    public TextView username;
    public ImageView userimage;
    public LinearLayout userproflink;
    public TextView timestamp;
    public ImageButton like;
    public TextView likecount;

    public StoryHolder(View itemView) {
        super(itemView);

        title_ui = (TextView) itemView.findViewById(R.id.title_ui);
        story_ui = (TextView) itemView.findViewById(R.id.story_ui);
        username = (TextView) itemView.findViewById(R.id.username);
        userimage = (ImageView) itemView.findViewById(R.id.userimage);
        userproflink = (LinearLayout) itemView.findViewById(R.id.userproflink);
        timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        like = (ImageButton) itemView.findViewById(R.id.like);
        likecount = (TextView) itemView.findViewById(R.id.likecount);



    }
}
