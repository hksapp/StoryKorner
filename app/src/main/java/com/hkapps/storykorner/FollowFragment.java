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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class FollowFragment extends Fragment {


    private static final String TAG = FollowFragment.class.getSimpleName();
    private RecyclerView followRecyclerview;
    private LinearLayoutManager linearLayoutManager;
    private FollowAdapter mFollowAdapter;
    private DatabaseReference mDatabaseRef, mChildRef;
    private boolean follow_check;

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
        follow_check = sharedPreference.getBoolean("follow_check", false);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        followRecyclerview = (RecyclerView) rootview.findViewById(R.id.follow_recycleview);
        followRecyclerview.setHasFixedSize(true);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(prof_id);
        if (follow_check) {
            mChildRef = mDatabaseRef.child("followers");
        } else {
            mChildRef = mDatabaseRef.child("following");
        }


        mFollowAdapter = new FollowAdapter(FollowObject.class, R.layout.follow_custom_ui, FollowHolder.class, mChildRef, getContext());

        followRecyclerview.setLayoutManager(linearLayoutManager);


        followRecyclerview.setAdapter(mFollowAdapter);

        return rootview;
    }

}
