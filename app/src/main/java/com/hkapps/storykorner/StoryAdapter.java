package com.hkapps.storykorner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kamal on 11-12-2016.
 */

public class StoryAdapter extends FirebaseRecyclerAdapter <StoryObject, StoryHolder> {

    public static final String MyPREFERENCES = "profile";
    private static final String TAG = StoryAdapter.class.getSimpleName();
    public SharedPreferences sharedPreference;
    private Context context;
    private DatabaseReference mFireRef, mLikeRef;
    private boolean liked;

    public StoryAdapter(Class<StoryObject> modelClass, int modelLayout, Class<StoryHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }


    @Override
    protected void populateViewHolder(final StoryHolder viewHolder, final StoryObject model, final int position) {

        final long tmp = model.getTimestamp();
        Date date = new Date(tmp);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String tym = formatter.format(date);

        viewHolder.timestamp.setText(tym);



        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        String storyuserid = sharedPreference.getString("storyuserid", model.getUserid());

        boolean chk = sharedPreference.getBoolean("profile", false);

        mFireRef = FirebaseDatabase.getInstance().getReference().child("Users");

        String post_key = getRef(position).getKey().toString();
        mLikeRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key).child("likes");

        viewHolder.like.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                //   mLikeRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                liked = true;

                mLikeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (liked) {
                            if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                mLikeRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                // Toast.makeText(context, "HIIII", Toast.LENGTH_SHORT).show();
                                viewHolder.like.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                                liked = false;


                            } else {
                                mLikeRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                viewHolder.like.setImageResource(R.drawable.ic_mood_black_24dp);
                                liked = false;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

        });

        mLikeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                    viewHolder.like.setImageResource(R.drawable.ic_mood_black_24dp);
                } else {
                    viewHolder.like.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        viewHolder.userproflink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("profile_id", model.getUserid());
                edit.commit();

                Fragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });


        if (model.getUserid().equals(storyuserid)) {

            mFireRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(model.getUserid()).child("photolink").exists()) {
                        String photo = dataSnapshot.child(model.getUserid()).child("photolink").getValue().toString();
                        Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.userimage);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        viewHolder.title_ui.setText(model.getTitle());
            viewHolder.story_ui.setText(model.getStory());
            viewHolder.username.setText(model.getUsername());


            viewHolder.story_ui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, StoryDescription.class);
                    i.putExtra("intent_title", model.getTitle());
                    i.putExtra("intent_story", model.getStory());
                    context.startActivity(i);
                }
            });


            viewHolder.title_ui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, StoryDescription.class);
                    i.putExtra("intent_title", model.getTitle());
                    i.putExtra("intent_story", model.getStory());
                    context.startActivity(i);
                }
            });
        }
        if (chk) {


            mFireRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(model.getUserid()).child("photolink").exists()) {
                        String photo = dataSnapshot.child(model.getUserid()).child("photolink").getValue().toString();
                        Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.userimage);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            viewHolder.title_ui.setText(model.getTitle());
            viewHolder.story_ui.setText(model.getStory());
            viewHolder.username.setText(model.getUsername());


            viewHolder.story_ui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, StoryDescription.class);
                    i.putExtra("intent_title", model.getTitle());
                    i.putExtra("intent_story", model.getStory());
                    context.startActivity(i);
                }
            });


            viewHolder.title_ui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, StoryDescription.class);
                    i.putExtra("intent_title", model.getTitle());
                    i.putExtra("intent_story", model.getStory());
                    context.startActivity(i);
                }
            });

        }

    }
}
