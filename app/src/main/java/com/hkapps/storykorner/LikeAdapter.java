package com.hkapps.storykorner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by kamal on 20-12-2016.
 */

public class LikeAdapter extends FirebaseRecyclerAdapter<LikeObject, LikeHolder> {

    private Context context;
    private DatabaseReference mAllLikesRef;


    public LikeAdapter(Class<LikeObject> modelClass, int modelLayout, Class<LikeHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }


    @Override
    protected void populateViewHolder(final LikeHolder viewHolder, LikeObject model, final int position) {


        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        String likes_post_key = sharedPreference.getString("likes_post_key", "null");


        Toast.makeText(context, "Hiiiiiiiiiiiii", Toast.LENGTH_SHORT).show();

        viewHolder.likes_user.setText(LikeFragment.list.get(position));

    }
}

