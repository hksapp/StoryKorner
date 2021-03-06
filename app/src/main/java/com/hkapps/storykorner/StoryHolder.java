package com.hkapps.storykorner;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
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
    public ImageView userimage, delete;
    public LinearLayout userproflink, write_comment, main_layout;
    public TextView timestamp;
    public ImageButton like;
    public TextView likecount;
    public EditText type_comment;
    public ImageButton send_comment;
    public ImageButton comment, save, share;
    public TextView user_cmt;
    public TextView category;
    public LinearLayout backgroundColor;


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
        type_comment = (EditText) itemView.findViewById(R.id.type_comment);
        send_comment = (ImageButton) itemView.findViewById(R.id.send_comment);
        comment = (ImageButton) itemView.findViewById(R.id.comment);
        user_cmt = (TextView) itemView.findViewById(R.id.user_cmt);
        write_comment = (LinearLayout) itemView.findViewById(R.id.write_comment);
        backgroundColor = (LinearLayout) itemView.findViewById(R.id.backgroundColor);
        main_layout = (LinearLayout) itemView.findViewById(R.id.main_layout);

        category = (TextView) itemView.findViewById(R.id.categoryname);


        delete = (ImageView) itemView.findViewById(R.id.delete);

        save = (ImageButton) itemView.findViewById(R.id.save);
        share = (ImageButton) itemView.findViewById(R.id.share);

    }
}

