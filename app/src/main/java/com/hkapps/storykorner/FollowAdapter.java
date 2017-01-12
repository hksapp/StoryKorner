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

    public FollowAdapter(Class<FollowObject> modelClass, int modelLayout, Class<FollowHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);


        this.context = context;
    }


    @Override
    protected void populateViewHolder(final FollowHolder viewHolder, final FollowObject model, int position) {

        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        follow_check = sharedPreference.getBoolean("follow_check", false);
        usersearch_boolean = sharedPreference.getBoolean("usersearch_boolean", false);
        followfragment = sharedPreference.getInt("followfragment", 404);


        switch (followfragment) {


            case 1:


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
