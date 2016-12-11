package com.hkapps.storykorner;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kamal on 11-12-2016.
 */

public class StoryHolder extends RecyclerView.ViewHolder {

    private static final String TAG = StoryHolder.class.getSimpleName();
    public TextView title_ui;
    public TextView story_ui;

    public StoryHolder(View itemView) {
        super(itemView);

        title_ui = (TextView) itemView.findViewById(R.id.title_ui);
        story_ui = (TextView) itemView.findViewById(R.id.story_ui);


    }
}
