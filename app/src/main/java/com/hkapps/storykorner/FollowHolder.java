package com.hkapps.storykorner;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by kamal on 25-12-2016.
 */

public class FollowHolder extends RecyclerView.ViewHolder {


    public TextView follow_list;
    public ImageView follow_imgview;
    public LinearLayout follow_profile;

    public FollowHolder(View itemView) {
        super(itemView);


        follow_list = (TextView) itemView.findViewById(R.id.follow_list);
        follow_imgview = (ImageView) itemView.findViewById(R.id.follow_imgview);

        follow_profile = (LinearLayout) itemView.findViewById(R.id.follow_profile);


    }
}
