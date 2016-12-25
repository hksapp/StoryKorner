package com.hkapps.storykorner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by kamal on 25-12-2016.
 */

public class FollowAdapter extends FirebaseRecyclerAdapter<FollowObject, FollowHolder> {
    private static final String TAG = FollowAdapter.class.getSimpleName();

    private Context context;
    private SharedPreferences sharedPreferences;
    private boolean follow_check;
    private DatabaseReference mPhotoRef;

    public FollowAdapter(Class<FollowObject> modelClass, int modelLayout, Class<FollowHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);


        this.context = context;
    }


    @Override
    protected void populateViewHolder(final FollowHolder viewHolder, final FollowObject model, int position) {

        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        follow_check = sharedPreference.getBoolean("follow_check", false);


        if (follow_check) {

            viewHolder.follow_list.setOnClickListener(new View.OnClickListener() {
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


            viewHolder.follow_list.setOnClickListener(new View.OnClickListener() {
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


    }


}