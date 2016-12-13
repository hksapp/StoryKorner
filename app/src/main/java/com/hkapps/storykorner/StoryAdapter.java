package com.hkapps.storykorner;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kamal on 11-12-2016.
 */

public class StoryAdapter extends FirebaseRecyclerAdapter <StoryObject, StoryHolder> {


    private static final String TAG = StoryAdapter.class.getSimpleName();
    private DatabaseReference postRef;
    private Context context;
    private boolean mProcessLike;

    public StoryAdapter(Class<StoryObject> modelClass, int modelLayout, Class<StoryHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }


    @Override
    protected void populateViewHolder(final StoryHolder viewHolder, final StoryObject model, final int position) {




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

                final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String uname = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String post_key = getRef(position).getKey().toString();
                postRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key).child("Likes");


                mProcessLike = true;


                postRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (mProcessLike) {

                            if (dataSnapshot.hasChild(userid)) {

                                postRef.child(userid).removeValue();


                                mProcessLike = false;

                            } else {

                                postRef.child(userid).setValue(uname);


                                mProcessLike = false;


                            }
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });










            }
        });




    }
}
