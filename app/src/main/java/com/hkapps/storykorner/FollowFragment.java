package com.hkapps.storykorner;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class FollowFragment extends Fragment {


    private static final String TAG = FollowFragment.class.getSimpleName();
    private RecyclerView followRecyclerview;
    private LinearLayoutManager linearLayoutManager;
    private FollowAdapter mFollowAdapter;
    private DatabaseReference mDatabaseRef, mChildRef;
    private boolean follow_check, usersearch_boolean;

    public FollowFragment() {
        // Required empty public constructor
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFollowAdapter != null) {
            mFollowAdapter.cleanup();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_follow, container, false);


        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prof_id = sharedPreference.getString("prof_followers_id", "null");
        String usersearch = sharedPreference.getString("usersearch", "");
        String likes_post_key = sharedPreference.getString("likes_post_key", "");
        String comment_post_key = sharedPreference.getString("comment_post_key", "");

        int followfragment = sharedPreference.getInt("followfragment", 404);

        //String comment_post_key = sharedPreference.getString("comment_post_key", "");
        //int followfragment_cmt = sharedPreference.getInt("followfragment_cmt", 100);



        linearLayoutManager = new LinearLayoutManager(getActivity());

        followRecyclerview = (RecyclerView) rootview.findViewById(R.id.follow_recycleview);
        followRecyclerview.setHasFixedSize(true);


        switch (followfragment) {

            case 1:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(likes_post_key).child("likes");
                mFollowAdapter = new FollowAdapter(FollowObject.class, R.layout.follow_custom_ui, FollowHolder.class, mDatabaseRef, getContext());
                break;

            case 2:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
                Query searchUserRef = mDatabaseRef.orderByChild("uname").startAt(usersearch).endAt(usersearch + "\uf8ff");
                mFollowAdapter = new FollowAdapter(FollowObject.class, R.layout.follow_custom_ui, FollowHolder.class, searchUserRef, getContext());
                break;

            case 3:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(prof_id);
                mChildRef = mDatabaseRef.child("followers");
                mFollowAdapter = new FollowAdapter(FollowObject.class, R.layout.follow_custom_ui, FollowHolder.class, mChildRef, getContext());
                break;

            case 4:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(prof_id);
                mChildRef = mDatabaseRef.child("following");
                mFollowAdapter = new FollowAdapter(FollowObject.class, R.layout.follow_custom_ui, FollowHolder.class, mChildRef, getContext());
                break;

            case 10:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(comment_post_key).child("comments");
                mFollowAdapter = new FollowAdapter(FollowObject.class, R.layout.follow_custom_ui, FollowHolder.class, mDatabaseRef, getContext());
                break;



            case 404:
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();


        }


       /* if (followfragment == 2) {

            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
            Query searchUserRef = mDatabaseRef.orderByChild("uname").startAt(usersearch).endAt(usersearch + "\uf8ff");

            mFollowAdapter = new FollowAdapter(FollowObject.class, R.layout.follow_custom_ui, FollowHolder.class, searchUserRef, getContext());


        } else if (followfragment == 1) {


            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(likes_post_key).child("likes");


            mFollowAdapter = new FollowAdapter(FollowObject.class, R.layout.follow_custom_ui, FollowHolder.class, mDatabaseRef, getContext());


        } else if (followfragment == 3) {

            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(prof_id);


            mChildRef = mDatabaseRef.child("followers");
            mFollowAdapter = new FollowAdapter(FollowObject.class, R.layout.follow_custom_ui, FollowHolder.class, mChildRef, getContext());

        } else if (followfragment == 4) {

            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(prof_id);


            mChildRef = mDatabaseRef.child("following");
            mFollowAdapter = new FollowAdapter(FollowObject.class, R.layout.follow_custom_ui, FollowHolder.class, mChildRef, getContext());

        }*/


        followRecyclerview.setLayoutManager(linearLayoutManager);

        followRecyclerview.setAdapter(mFollowAdapter);

        return rootview;
    }

}
