package com.hkapps.storykorner;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kamal on 11-12-2016.
 */

public class StoryAdapter extends FirebaseRecyclerAdapter <StoryObject, StoryHolder> {


    private static final String TAG = StoryAdapter.class.getSimpleName();
    private DatabaseReference postRef;
    private Context context;

    public StoryAdapter(Class<StoryObject> modelClass, int modelLayout, Class<StoryHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }


    @Override
    protected void populateViewHolder(StoryHolder viewHolder, final StoryObject model, final int position) {


        viewHolder.title_ui.setText(model.getTitle());
        viewHolder.story_ui.setText(model.getStory());
        viewHolder.username.setText(model.getUsername());

        long tmp = model.getTimestamp();
        Date date = new Date(tmp);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String tym = formatter.format(date);

        viewHolder.timestamp.setText(tym);

        viewHolder.story_ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context,StoryDescription.class);
                i.putExtra("intent_title",model.getTitle());
                i.putExtra("intent_story",model.getStory());
                context.startActivity(i);
            }
        });


        viewHolder.title_ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context,StoryDescription.class);
                i.putExtra("intent_title",model.getTitle());
                i.putExtra("intent_story",model.getStory());
                context.startActivity(i);
            }
        });

        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String post_key = getRef(position).getKey().toString();
                postRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key);


                Map liked_user = new HashMap();
                liked_user.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                Map likes = new HashMap();
                likes.put("Likes", liked_user);

                postRef.updateChildren(likes);


            }
        });




    }
}
