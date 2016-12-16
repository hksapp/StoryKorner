package com.hkapps.storykorner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoriesFragment extends Fragment {

    private RecyclerView storyRecyclerview;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mDatabaseRef, mProfStoriesRef;
    private DatabaseReference childRef;

    private StoryAdapter mStoryAdapter;

    public StoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_stories, container, false);

        boolean cal = getActivity().getIntent().getBooleanExtra("user_prof", false);

        if (cal)
            Toast.makeText(getActivity(), "Profile Fragment", Toast.LENGTH_SHORT).show();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        storyRecyclerview = (RecyclerView) rootview.findViewById(R.id.story_recycler_view);
        storyRecyclerview.setHasFixedSize(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("Posted_Stories");


        mStoryAdapter = new StoryAdapter(StoryObject.class,R.layout.story_custom_ui,StoryHolder.class ,childRef,getContext());
        storyRecyclerview.setLayoutManager(linearLayoutManager);
        storyRecyclerview.setAdapter(mStoryAdapter);


        return rootview;
    }


}
