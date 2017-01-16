package com.hkapps.storykorner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by kamal on 25-12-2016.
 */

public class FollowAdapter extends FirebaseRecyclerAdapter<FollowObject, FollowHolder> {
    private static final String TAG = FollowAdapter.class.getSimpleName();

    private Context context;
    private SharedPreferences sharedPreferences;
    private boolean follow_check, usersearch_boolean;
    private int followfragment;
    private DatabaseReference mPhotoRef;
    private String photolink = "Ssup";
    private String uname = "hi";

    public FollowAdapter(Class<FollowObject> modelClass, int modelLayout, Class<FollowHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);


        this.context = context;
    }


    @Override
    protected void populateViewHolder(final FollowHolder viewHolder, final FollowObject model, final int position) {

        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        follow_check = sharedPreference.getBoolean("follow_check", false);
        String comment_id = sharedPreference.getString("comment_id", "null");
        final String comment_post_key = sharedPreference.getString("comment_post_key", "");

        usersearch_boolean = sharedPreference.getBoolean("usersearch_boolean", false);
        followfragment = sharedPreference.getInt("followfragment", 404);


        switch (followfragment) {


            case 1:


                DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getLiked_id());
                Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        viewHolder.follow_list.setText(dataSnapshot.child("uname").getValue().toString());
                        Picasso.with(context).load(dataSnapshot.child("photolink").getValue().toString()).fit().centerCrop().into(viewHolder.follow_imgview);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });


                viewHolder.follow_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("profile_id", model.getLiked_id());
                        edit.commit();


                        Fragment fragment = new ProfileFragment();
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

                break;


            case 2:

                Picasso.with(context).load(model.getPhotolink()).fit().centerCrop().into(viewHolder.follow_imgview);

                viewHolder.follow_list.setText(model.getUname());
                viewHolder.usermail.setVisibility(View.VISIBLE);
                viewHolder.usermail.setText(model.getUser_email());


                viewHolder.follow_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("profile_id", model.getUserid());
                        edit.commit();

                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);

                        Fragment fragment = new ProfileFragment();
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

                break;


            case 3:

                viewHolder.follow_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("profile_id", model.getFollower_id());
                        edit.commit();

                        Fragment fragment = new ProfileFragment();
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

                viewHolder.follow_list.setText(model.getFollower_name());

                mPhotoRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getFollower_id().toString());

                mPhotoRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("photolink").exists()) {
                            String photo = dataSnapshot.child("photolink").getValue().toString();
                            Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.follow_imgview);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                break;


            case 4:

                viewHolder.follow_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("profile_id", model.getFollowing_id());
                        edit.commit();

                        Fragment fragment = new ProfileFragment();
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });


                viewHolder.follow_list.setText(model.getFollowing_name());
                mPhotoRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getFollowing_id().toString());

                mPhotoRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("photolink").exists()) {
                            String photo = dataSnapshot.child("photolink").getValue().toString();
                            Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.follow_imgview);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;


            case 10:


                final DatabaseReference cmtRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getCmt_user_id());
                cmtRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        viewHolder.follow_list.setText(dataSnapshot.child("uname").getValue().toString());
                        Picasso.with(context).load(dataSnapshot.child("photolink").getValue().toString()).fit().centerCrop().into(viewHolder.follow_imgview);
                        viewHolder.usermail.setVisibility(View.VISIBLE);
                        viewHolder.usermail.setText("'" + model.getCmt().toString() + "'");


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });


                final DatabaseReference checkComRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(comment_post_key);
                checkComRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        if ((FirebaseAuth.getInstance().getCurrentUser().getUid().toString().equals(dataSnapshot.child("userid").getValue().toString())) || (FirebaseAuth.getInstance().getCurrentUser().getUid().toString().equals(model.getCmt_user_id().toString()))) {


                            viewHolder.follow_profile.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {

                                    checkComRef.child("comments").child(model.getCmt_key().toString()).removeValue();
                                    checkComRef.keepSynced(true);
                                    cmtRef.keepSynced(true);
                                    Toast.makeText(context, "Done Deletion!", Toast.LENGTH_SHORT).show();

                                    DatabaseReference cmtRef = FirebaseDatabase.getInstance().getReference().child("Users").child(dataSnapshot.child("userid").getValue().toString()).child("Notifications");


                                    Query noti = cmtRef.orderByChild("cmt_key_post_id").equalTo(model.getCmt_key().toString() + "_" + comment_post_key);

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

                                    return true;
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });












                viewHolder.follow_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("profile_id", model.getCmt_user_id());
                        edit.commit();


                        Fragment fragment = new ProfileFragment();
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

                break;



            case 404:
                Toast.makeText(context, "Umm...Error Occured", Toast.LENGTH_SHORT).show();


        }







      /*  if (usersearch_boolean) {

            Picasso.with(context).load(model.getPhotolink()).fit().centerCrop().into(viewHolder.follow_imgview);

            viewHolder.follow_list.setText(model.getUname());
            viewHolder.usermail.setVisibility(View.VISIBLE);
            viewHolder.usermail.setText(model.getUser_email());


            viewHolder.follow_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("profile_id", model.getUserid());
                    edit.commit();

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    Fragment fragment = new ProfileFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });


        } else {

            if (follow_check) {

                viewHolder.follow_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("profile_id", model.getFollower_id());
                        edit.commit();

                        Fragment fragment = new ProfileFragment();
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

                viewHolder.follow_list.setText(model.getFollower_name());

                mPhotoRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getFollower_id().toString());

                mPhotoRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("photolink").exists()) {
                            String photo = dataSnapshot.child("photolink").getValue().toString();
                            Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.follow_imgview);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            } else {


                viewHolder.follow_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("profile_id", model.getFollowing_id());
                        edit.commit();

                        Fragment fragment = new ProfileFragment();
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });


                viewHolder.follow_list.setText(model.getFollowing_name());
                mPhotoRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getFollowing_id().toString());

                mPhotoRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("photolink").exists()) {
                            String photo = dataSnapshot.child("photolink").getValue().toString();
                            Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.follow_imgview);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


        }*/

    }


}