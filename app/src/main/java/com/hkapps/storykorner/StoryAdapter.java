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
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

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
    public int bgc = 1;
    private Context context;
    private DatabaseReference mFireRef, mLikeRef, mCommentRef, mCommentUpdateRef;
    private boolean liked;
    private LinearLayout backgroundColor;
    private String[] bg = new String[6];
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

        String post_key = getRef(position).getKey().toString();
        mLikeRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key).child("likes");

        mCommentRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key).child("comments");
        mCommentUpdateRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key);


        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        String storyuserid = sharedPreference.getString("storyuserid", model.getUserid());

        boolean chk = sharedPreference.getBoolean("profile", false);

        mFireRef = FirebaseDatabase.getInstance().getReference().child("Users");
        final int[] rainbow = context.getResources().getIntArray(R.array.bg);
        // int postcount = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").get
        //  for (int i = 0; i < l; i++) {

        viewHolder.backgroundColor.setBackgroundColor(rainbow[position % 6]);


        //  }
        //bg ={"#2196f3",};
        //  viewHolder.backgroundColor.setBackgroundColor(Color.parseColor("#E91E63"));


        viewHolder.like.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String post_key = getRef(position).getKey().toString();
                mLikeRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key).child("likes");
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

                        viewHolder.likecount.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Likes");


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

                viewHolder.likecount.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Likes");
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS

        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.type_comment.setVisibility(View.VISIBLE);
                viewHolder.send_comment.setVisibility(View.VISIBLE);
                viewHolder.type_comment.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        });


        viewHolder.send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);


                // viewHolder.type_comment.setVisibility(View.INVISIBLE);
                // viewHolder.send_comment.setVisibility(View.INVISIBLE);
                viewHolder.write_comment.setVisibility(View.GONE);


                String post_key = getRef(position).getKey().toString();
                mCommentRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key).child("comments");

                mCommentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String ct = viewHolder.type_comment.getText().toString();

                        String key = mCommentRef.push().getKey();

                        mCommentRef.child(key).child("cmt_user_id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        mCommentRef.child(key).child("cmt").setValue(ct);


                        viewHolder.user_cmt.setText(String.valueOf(dataSnapshot.child("comments").getChildrenCount()) + " Comments");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        mCommentUpdateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("comments")) {


                    viewHolder.user_cmt.setText(String.valueOf(dataSnapshot.child("comments").getChildrenCount()) + " Comments");

          /*         Map mm = new HashMap();
                for( DataSnapshot ds :   dataSnapshot.child("comments").getChildren()){

               String cmt_user_id =  ds.child("cmt_user_id").getValue().toString();
                 String cmt =   ds.child("cmt").getValue().toString();

                 }

                    */
                    }


            }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        //COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS


        viewHolder.user_cmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment fragment = new CommentFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });


        //LIKE FRAGMENT

        viewHolder.likecount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String post_key = getRef(position).getKey().toString();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("likes_post_key", post_key);
                    edit.commit();


                    Fragment fragment = new LikeFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


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
                    i.putExtra("position", position);
                    context.startActivity(i);
                }
            });


            viewHolder.title_ui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, StoryDescription.class);
                    i.putExtra("intent_title", model.getTitle());
                    i.putExtra("intent_story", model.getStory());
                    // i.putExtra("position",position);
                    i.putExtra("position", position);
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

