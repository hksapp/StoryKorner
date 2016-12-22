package com.hkapps.storykorner;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kamal on 20-12-2016.
 */

public class LikeHolder extends RecyclerView.ViewHolder {
    public TextView likes_user;
    public ImageView likes_img;

    public LikeHolder(View itemView) {
        super(itemView);

        likes_img = (ImageView) itemView.findViewById(R.id.likes_img);
        likes_user = (TextView) itemView.findViewById(R.id.likes_user);


    }
}
