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
public class StoriesFragment extends Fragment {

    public static StoryAdapter mStoryAdapter;
    private RecyclerView storyRecyclerview;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mDatabaseRef, mProfStoriesRef;
    private DatabaseReference childRef, mUserRef;
    private SharedPreferences sharedPreference;

    public StoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mStoryAdapter != null) {
            mStoryAdapter.cleanup();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_stories, container, false);


        sharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String storyuserid = sharedPreference.getString("storyuserid", null);
        boolean chk = sharedPreference.getBoolean("profile", false);




        String Category = sharedPreference.getString("Category", "Humor");
        Boolean CategoryBoolean = sharedPreference.getBoolean("CategoryBoolean", false);



        if(CategoryBoolean.equals(true)){

            Toast.makeText(getContext(), Category, Toast.LENGTH_SHORT).show();
        }


        linearLayoutManager = new LinearLayoutManager(getActivity());
        storyRecyclerview = (RecyclerView) rootview.findViewById(R.id.story_recycler_view);
        storyRecyclerview.setHasFixedSize(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("Posted_Stories");

        if (chk) {

            Query profRef = childRef.orderByChild("userid").equalTo(storyuserid);
            mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, profRef, getContext());

        } else {


            mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, childRef, getContext());

        }
        storyRecyclerview.setLayoutManager(linearLayoutManager);
        storyRecyclerview.setAdapter(mStoryAdapter);


        return rootview;
    }


}
