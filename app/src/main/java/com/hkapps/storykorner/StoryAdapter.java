package com.hkapps.storykorner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kamal on 11-12-2016.
 */

public class StoryAdapter extends FirebaseRecyclerAdapter<StoryObject, StoryHolder> {


    public static final String MyPREFERENCES = "profile";
    private static final String TAG = StoryAdapter.class.getSimpleName();
    public SharedPreferences sharedPreference;
    private Context context;
    private DatabaseReference mFireRef, mLikeRef, mCommentRef, mCommentUpdateRef, mFollowingRef, mDelRef, notifying;
    private boolean liked;
    private LinearLayout backgroundColor;

    public StoryAdapter(Class<StoryObject> modelClass, int modelLayout, Class<StoryHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }


    @Override
    protected void populateViewHolder(final StoryHolder viewHolder, final StoryObject model, final int position) {
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        int storiesfragment = sharedPreference.getInt("storiesfragment", 404);


        if (storiesfragment == 5 || storiesfragment == 4) {


            final String postid = model.getStory_id();

            mLikeRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(postid).child("likes");

            mCommentRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(postid).child("comments");
            mCommentUpdateRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(postid);


            mDelRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories");

            mFollowingRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("following");

            //  String storyuserid = sharedPreference.getString("storyuserid", model.getUserid());


            mFireRef = FirebaseDatabase.getInstance().getReference().child("Users");

            DatabaseReference saveRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(postid);

            saveRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot_saved) {



                    viewHolder.title_ui.setText(dataSnapshot_saved.child("title").getValue().toString());
                    viewHolder.story_ui.setText(dataSnapshot_saved.child("story").getValue().toString());
                    viewHolder.username.setText(dataSnapshot_saved.child("username").getValue().toString());


                    long tmp = (long) dataSnapshot_saved.child("timestamp").getValue();
                    Date date = new Date(tmp);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    String tym = formatter.format(date);


                    Long tsLong = System.currentTimeMillis();



                    long ctym = tsLong-tmp;

                    long seconds = ctym / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;

                    if (days == 0) {

                        if (hours != 0) {
                            if (hours == 1)
                                viewHolder.timestamp.setText(String.valueOf(hours) + " Hr");
                            else
                                viewHolder.timestamp.setText(String.valueOf(hours) + " Hrs");
                        } else {
                            if (minutes != 0) {
                                if (minutes == 1)
                                    viewHolder.timestamp.setText(String.valueOf(minutes) + " Min");
                                else
                                    viewHolder.timestamp.setText(String.valueOf(minutes) + " Mins");

                            } else {
                                viewHolder.timestamp.setText(String.valueOf(seconds) + " Secs");

                            }
                        }
                    } else {
                        if (days == 1)
                            viewHolder.timestamp.setText(String.valueOf(days) + " day");
                        else
                            viewHolder.timestamp.setText(String.valueOf(days) + " days");

                    }

                    viewHolder.like.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            final String post_key = getRef(position).getKey().toString();


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
                                            notifying = FirebaseDatabase.getInstance().getReference().child("Users").child(dataSnapshot_saved.child("userid").getValue().toString()).child("Notifications");


                                            final Query noti = notifying.orderByChild("liker_id_post_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid().toString() + "_" + post_key);

                                            noti.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {


                                                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                        appleSnapshot.getRef().removeValue();
                                                    }


                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            viewHolder.like.setImageResource(R.drawable.ic_sentiment_satisfied_white_24dp);
                                            liked = false;


                                        } else {

                                            mLikeRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("liked_id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            notifying = FirebaseDatabase.getInstance().getReference().child("Users").child(dataSnapshot_saved.child("userid").getValue().toString()).child("Notifications");
                                            if (!dataSnapshot_saved.child("userid").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())) {
                                                Map postdata = new HashMap();
                                                postdata.put("liker_id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                                postdata.put("liker_id_post_id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString() + "_" + post_key);
                                                postdata.put("liker_name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString());

                                                postdata.put("post_id", post_key);
                                                postdata.put("timestamp", ServerValue.TIMESTAMP);
                                                postdata.put("seen", true);
                                                notifying.push().setValue(postdata);
                                            }
                                            viewHolder.like.setImageResource(R.drawable.ic_sentiment_very_satisfied_white_24dp);
                                            liked = false;



                                        }
                                    }
                                    if (dataSnapshot.getChildrenCount() == 0) {
                                        viewHolder.likecount.setVisibility(View.GONE);
                                    } else {
                                        viewHolder.likecount.setVisibility(View.VISIBLE);
                                    }


                                    viewHolder.likecount.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Likes");


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }

                    });



                    //COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS




                    viewHolder.userproflink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("profile_id", dataSnapshot_saved.child("userid").getValue().toString());
                            edit.commit();

                            Fragment fragment = new ProfileFragment();
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_container, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }
                    });


                    // if (model.getUserid().equals(storyuserid)) {

                    mFireRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(dataSnapshot_saved.child("userid").getValue().toString()).child("photolink").exists()) {
                                String photo = dataSnapshot.child(dataSnapshot_saved.child("userid").getValue().toString()).child("photolink").getValue().toString();
                                Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.userimage);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                  /*  if (dataSnapshot_saved.child("userid").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())) {

                        viewHolder.delete.setVisibility(View.VISIBLE);

                    }*/





                    viewHolder.category.setText(dataSnapshot_saved.child("category").getValue().toString());


                    viewHolder.category.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putInt("storiesfragment", 3);
                            edit.putString("Category", dataSnapshot_saved.child("category").getValue().toString());
                            edit.commit();

                            Fragment fragment = new StoriesFragment();
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_container, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }
                    });


                    viewHolder.story_ui.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent i = new Intent(context, StoryDescription.class);
                            i.putExtra("intent_title", dataSnapshot_saved.child("title").getValue().toString());
                            i.putExtra("intent_story", dataSnapshot_saved.child("story").getValue().toString());

                            context.startActivity(i);
                        }
                    });


                    viewHolder.title_ui.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent i = new Intent(context, StoryDescription.class);
                            i.putExtra("intent_title", dataSnapshot_saved.child("title").getValue().toString());
                            i.putExtra("intent_story", dataSnapshot_saved.child("story").getValue().toString());
                            // i.putExtra("position",position);

                            context.startActivity(i);
                        }
                    });


                    viewHolder.send_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(),
                                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
                            if (viewHolder.type_comment.getText().toString().trim().length() != 0) {


                                // viewHolder.type_comment.setVisibility(View.INVISIBLE);
                                // viewHolder.send_comment.setVisibility(View.INVISIBLE);
                                viewHolder.write_comment.setVisibility(View.GONE);


                                String post_key = getRef(position).getKey().toString();
                                mCommentRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key).child("comments");

                                String ct = viewHolder.type_comment.getText().toString();

                                String key = mCommentRef.push().getKey();

                                mCommentRef.child(key).child("cmt_user_id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                mCommentRef.child(key).child("cmt").setValue(ct);
                                mCommentRef.child(key).child("cmt_key").setValue(key.toString());

                                if (!dataSnapshot_saved.child("userid").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())) {
                                    DatabaseReference cmtRef = FirebaseDatabase.getInstance().getReference().child("Users").child(dataSnapshot_saved.child("userid").getValue().toString()).child("Notifications");

                                    Map postdata = new HashMap();
                                    postdata.put("cmt_user_id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                    postdata.put("cmt_key_post_id", key.toString() + "_" + post_key);
                                    postdata.put("cmt_name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString());
                                    postdata.put("cmt", ct);
                                    postdata.put("post_id", post_key);
                                    postdata.put("timestamp", ServerValue.TIMESTAMP);
                                    postdata.put("seen", true);
                                    cmtRef.push().setValue(postdata);
                                }

                            }

                        }
                    });






                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mLikeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                        viewHolder.like.setImageResource(R.drawable.ic_sentiment_very_satisfied_white_24dp);
                    } else {
                        viewHolder.like.setImageResource(R.drawable.ic_sentiment_satisfied_white_24dp);
                    }

                    if (dataSnapshot.getChildrenCount() == 0) {
                        viewHolder.likecount.setVisibility(View.GONE);
                    } else {
                        viewHolder.likecount.setVisibility(View.VISIBLE);
                    }
                    viewHolder.likecount.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Likes");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            mCommentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getChildrenCount() == 0) {
                        viewHolder.user_cmt.setVisibility(View.GONE);
                    } else {
                        viewHolder.user_cmt.setVisibility(View.VISIBLE);
                    }


                    viewHolder.user_cmt.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Comments");



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            context);
                    alert.setTitle("Confirm Deletion");
                    alert.setMessage("Delete this Story?");
                    alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do your work here
                            mDelRef.child(postid).removeValue();
                            mDelRef.keepSynced(true);
                            DatabaseReference mOwnRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("newsfeed");
                            mOwnRef.child(postid).removeValue();

                            DatabaseReference saveRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                            saveRef.child("saved").child(postid).removeValue();

                            DatabaseReference nRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Notifications");

                            Query nNotDelRef = nRef.orderByChild("post_id").equalTo(postid);


                            nNotDelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }



                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            nNotDelRef.keepSynced(true);


                            DatabaseReference mFollowerRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("followers");

                            mFollowerRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                        String follower_id = ds.getKey().toString();

                                        DatabaseReference mNewsFeedRef = FirebaseDatabase.getInstance().getReference().child("Users").child(follower_id).child("newsfeed");

                                        mNewsFeedRef.child(postid).removeValue();

                                        DatabaseReference saveRef = FirebaseDatabase.getInstance().getReference().child("Users").child(follower_id);

                                        saveRef.child("saved").child(postid).removeValue();


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            dialog.dismiss();

                        }
                    });
                    alert.setNegativeButton("Don't Delete", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    alert.show();


                }
            });


            viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (viewHolder.type_comment.getVisibility() == View.GONE) {
                        viewHolder.type_comment.setVisibility(View.VISIBLE);
                        viewHolder.send_comment.setVisibility(View.VISIBLE);
                        viewHolder.type_comment.requestFocus();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    } else if (viewHolder.type_comment.getVisibility() == View.VISIBLE) {

                        viewHolder.type_comment.setVisibility(View.GONE);
                        viewHolder.send_comment.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    }


                }
            });



            //COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS


            viewHolder.user_cmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String post_key = getRef(position).getKey().toString();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("comment_post_key", post_key);
                    edit.putInt("followfragment", 10);
                    edit.commit();

                    Fragment fragment = new FollowFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });


            //SAVE SAVE SAVE SAVE   SAVE SAVE SAVE SAVE SAVE

            viewHolder.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference saveRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                    saveRef.child("saved").child(postid).child("story_id").setValue(postid);
                    Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show();

                }
            });


            viewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent sendIntent = new Intent();
                    String msg = "Hey, check this out: " + "https://ft4am.app.goo.gl/?link=https://StoryKorner-dynamic-linksstoryid=(" + postid + ")&apn=com.hkapps.storykorner";
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                    sendIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(sendIntent, "Share story..."));
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
                    edit.putInt("followfragment", 1);
                    edit.commit();


                    Fragment fragment = new FollowFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
            });


        } else {


            if (storiesfragment == 1 && model.getUserid().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())) {

                viewHolder.delete.setVisibility(View.VISIBLE);

            } else {
                viewHolder.delete.setVisibility(View.GONE);

            }


            long tmp = model.getTimestamp();
            Date date = new Date(tmp);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String tym = formatter.format(date);

            viewHolder.timestamp.setText(tym);

            Long tsLong = System.currentTimeMillis();


            long ctym = tsLong - tmp;
            long seconds = ctym / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days == 0) {

                if (hours != 0) {
                    if (hours == 1)
                        viewHolder.timestamp.setText(String.valueOf(hours) + " Hr");
                    else
                        viewHolder.timestamp.setText(String.valueOf(hours) + " Hrs");
                } else {
                    if (minutes != 0) {
                        if (minutes == 1)
                            viewHolder.timestamp.setText(String.valueOf(minutes) + " Min");
                        else
                            viewHolder.timestamp.setText(String.valueOf(minutes) + " Mins");

                    } else {
                        viewHolder.timestamp.setText(String.valueOf(seconds) + " Secs");

                    }
                }
            } else {
                if (days == 1)
                    viewHolder.timestamp.setText(String.valueOf(days) + " day");
                else
                    viewHolder.timestamp.setText(String.valueOf(days) + " days");

            }


            final String post_key = getRef(position).getKey().toString();

            mLikeRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key).child("likes");

            mCommentRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key).child("comments");
            mCommentUpdateRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key);


            mDelRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories");

            mFollowingRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("following");

            String storyuserid = sharedPreference.getString("storyuserid", model.getUserid());


            mFireRef = FirebaseDatabase.getInstance().getReference().child("Users");


            //like button
            viewHolder.like.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    final String post_key = getRef(position).getKey().toString();
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
                                    notifying = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUserid().toString()).child("Notifications");


                                    final Query noti = notifying.orderByChild("liker_id_post_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid().toString() + "_" + post_key);

                                    noti.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                appleSnapshot.getRef().removeValue();
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    viewHolder.like.setImageResource(R.drawable.ic_sentiment_satisfied_white_24dp);
                                    liked = false;


                                } else {
                                    mLikeRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("liked_id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    if (!model.getUserid().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())) {
                                        notifying = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUserid().toString()).child("Notifications");
                                        Map postdata = new HashMap();
                                        postdata.put("liker_id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                        postdata.put("liker_id_post_id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString() + "_" + post_key);
                                        postdata.put("liker_name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString());

                                        postdata.put("post_id", post_key);
                                        postdata.put("timestamp", ServerValue.TIMESTAMP);
                                        postdata.put("seen", true);
                                        notifying.push().setValue(postdata);
                                    }
                                    viewHolder.like.setImageResource(R.drawable.ic_sentiment_very_satisfied_white_24dp);
                                    liked = false;
                                }
                            }

                            if (dataSnapshot.getChildrenCount() == 0) {
                                viewHolder.likecount.setVisibility(View.GONE);
                            } else {
                                viewHolder.likecount.setVisibility(View.VISIBLE);
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

                        viewHolder.like.setImageResource(R.drawable.ic_sentiment_very_satisfied_white_24dp);
                    } else {
                        viewHolder.like.setImageResource(R.drawable.ic_sentiment_satisfied_white_24dp);
                    }


                    if (dataSnapshot.getChildrenCount() == 0) {
                        viewHolder.likecount.setVisibility(View.GONE);
                    } else {
                        viewHolder.likecount.setVisibility(View.VISIBLE);
                    }


                    viewHolder.likecount.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Likes");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            mCommentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() == 0) {
                        viewHolder.user_cmt.setVisibility(View.GONE);
                    } else {
                        viewHolder.user_cmt.setVisibility(View.VISIBLE);
                    }


                    viewHolder.user_cmt.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " Comments");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            //COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS

            viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (viewHolder.type_comment.getVisibility() == View.GONE) {
                        viewHolder.type_comment.setVisibility(View.VISIBLE);
                        viewHolder.send_comment.setVisibility(View.VISIBLE);
                        viewHolder.type_comment.requestFocus();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    } else if (viewHolder.type_comment.getVisibility() == View.VISIBLE) {

                        viewHolder.type_comment.setVisibility(View.GONE);
                        viewHolder.send_comment.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    }


                }
            });


            viewHolder.send_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    if (viewHolder.type_comment.getText().toString().trim().length() != 0) {


                        // viewHolder.type_comment.setVisibility(View.INVISIBLE);
                        // viewHolder.send_comment.setVisibility(View.INVISIBLE);
                        viewHolder.write_comment.setVisibility(View.GONE);


                        String post_key = getRef(position).getKey().toString();
                        mCommentRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(post_key).child("comments");

                        String ct = viewHolder.type_comment.getText().toString();

                        String key = mCommentRef.push().getKey();

                        mCommentRef.child(key).child("cmt_user_id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        mCommentRef.child(key).child("cmt").setValue(ct);
                        mCommentRef.child(key).child("cmt_key").setValue(key.toString());


                        if (!model.getUserid().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())) {
                            DatabaseReference cmtRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUserid().toString()).child("Notifications");
                            Map postdata = new HashMap();
                            postdata.put("cmt_user_id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                            postdata.put("cmt_key_post_id", key.toString() + "_" + post_key);
                            postdata.put("cmt_name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString());
                            postdata.put("cmt", ct);
                            postdata.put("post_id", post_key);
                            postdata.put("timestamp", ServerValue.TIMESTAMP);
                            postdata.put("seen", true);
                            cmtRef.push().setValue(postdata);
                        }



                    }

                }
            });




            //SAVE SAVE SAVE SAVE   SAVE SAVE SAVE SAVE SAVE

            viewHolder.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference saveRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                    saveRef.child("saved").child(post_key).child("story_id").setValue(post_key);

                    Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show();

                }
            });


            viewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent sendIntent = new Intent();
                    String msg = "Hey, check this out: " + "https://ft4am.app.goo.gl/?link=https://StoryKorner-dynamic-linksstoryid=(" + post_key + ")&apn=com.hkapps.storykorner";
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                    sendIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(sendIntent, "Share story..."));
                }
            });

            //COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS COMMENTS


            viewHolder.user_cmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   /* Fragment fragment =
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
*/


                    String post_key = getRef(position).getKey().toString();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("comment_post_key", post_key);
                    edit.putInt("followfragment", 10);
                    edit.commit();


                    Fragment fragment = new FollowFragment();
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
                    edit.putInt("followfragment", 1);
                    edit.commit();


                    Fragment fragment = new FollowFragment();
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


            // if (model.getUserid().equals(storyuserid)) {

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





            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            context);
                    alert.setTitle("Confirm Deletion");
                    alert.setMessage("Delete this Story?");
                    alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do your work here
                            mDelRef.child(post_key).removeValue();

                            DatabaseReference mOwnRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("newsfeed");
                            mOwnRef.child(post_key).removeValue();


                            DatabaseReference nRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Notifications");

                            Query nNotDelRef = nRef.orderByChild("post_id").equalTo(post_key);


                            nNotDelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                            DatabaseReference mFollowerRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("followers");

                            mFollowerRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                        String follower_id = ds.getKey().toString();

                                        DatabaseReference mNewsFeedRef = FirebaseDatabase.getInstance().getReference().child("Users").child(follower_id).child("newsfeed");

                                        mNewsFeedRef.child(post_key).removeValue();

                                        DatabaseReference saveRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                                        saveRef.child("saved").child(post_key).removeValue();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                            dialog.dismiss();

                        }
                    });
                    alert.setNegativeButton("Don't Delete", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    alert.show();


                }
            });


            viewHolder.category.setText(model.getCategory());


            viewHolder.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("storiesfragment", 3);
                    edit.putString("Category", model.getCategory().toString());
                    edit.commit();

                    Fragment fragment = new StoriesFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
            });


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




        /*if (chk) {

            mFollowingRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(model.getUserid().toString())) {

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

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/


        }

    }
}


