package com.hkapps.storykorner;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_stories, container, false);

        try {


            if (!isNetworkConnected()) {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("error", 6);
                edit.commit();


                Fragment fragment = new EmptyScreenFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment);

                fragmentTransaction.commit();

            }


            sharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String storyuserid = sharedPreference.getString("storyuserid", null);
            boolean chk = sharedPreference.getBoolean("profile", false);


            String storysearch = sharedPreference.getString("storysearch", "None").toLowerCase();
            boolean storysearch_boolean = sharedPreference.getBoolean("storysearch_boolean", false);

            String Category = sharedPreference.getString("Category", "Humor");
            boolean CategoryBoolean = sharedPreference.getBoolean("CategoryBoolean", false);
            String story_id = sharedPreference.getString("story_id", "-KapV7mFEkt15bS8yhHD");
            int storiesfragment = sharedPreference.getInt("storiesfragment", 404);


            linearLayoutManager = new LinearLayoutManager(getActivity());
            storyRecyclerview = (RecyclerView) rootview.findViewById(R.id.story_recycler_view);
            storyRecyclerview.setHasFixedSize(true);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            mDatabaseRef = FirebaseDatabase.getInstance().getReference();
            childRef = mDatabaseRef.child("Posted_Stories");
            childRef.keepSynced(true);


            switch (storiesfragment) {

                case 1:

                    Query profRef = childRef.orderByChild("userid").equalTo(storyuserid);
                    profRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {

                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putInt("error", 1);
                                edit.commit();


                                Fragment fragment = new EmptyScreenFragment();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.main_container, fragment);

                                fragmentTransaction.commit();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    profRef.keepSynced(true);
                    mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, profRef, getContext());
                    break;

                case 2:

                    Query storyRef = childRef.orderByChild("title_lower").startAt(storysearch).endAt(storysearch + "\uf8ff");
                    storyRef.keepSynced(true);
                    mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, storyRef, getContext());
                    break;

                case 3:

                    Query catRef = childRef.orderByChild("category").equalTo(Category);
                    catRef.keepSynced(true);
                    mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, catRef, getContext());
                    break;

                case 4:


                    DatabaseReference noRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("newsfeed");

                    noRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {


                                try {


                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor edit = sp.edit();
                                    edit.putString("storyuserid", "mJQpeKMk4BUPZpfK4qYgw91nBdD3");
                                    edit.putInt("storiesfragment", 1);
//                                edit.putInt("error", 1);

                                edit.commit();

                                    Fragment fragment = new StoriesFragment();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.main_container, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                                } catch (Exception e) {
                                }

//                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                                SharedPreferences.Editor edit = sp.edit();
//                                edit.putInt("error", 5);
//                                edit.commit();

                /*    try {
//                                Fragment fragment = new EmptyScreenFragment();
                        Fragment fragment = new SearchFragment();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment);

                        fragmentTransaction.commit();
                    }catch (Exception e){}*/
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    Query newsfeedRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("newsfeed");
                    newsfeedRef.keepSynced(true);
                    mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, newsfeedRef, getContext());

                    break;

                case 5:

                    Query savedRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("saved");


                    savedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {

                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putInt("error", 4);
                                edit.commit();


                                Fragment fragment = new EmptyScreenFragment();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.main_container, fragment);
                                fragmentTransaction.commit();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    savedRef.keepSynced(true);
                    mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, savedRef, getContext());
                    break;


                case 8:

                    Query nStory = childRef.orderByChild("post_id").equalTo(story_id);
                    nStory.keepSynced(true);
                    mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, nStory, getContext());
                    break;

            }



      /*  if (chk) {

            Query profRef = childRef.orderByChild("userid").equalTo(storyuserid);
            mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, profRef, getContext());

        } else if (storysearch_boolean) {

            Query profRef = childRef.orderByChild("title").startAt(storysearch).endAt(storysearch + "\uf8ff");
            mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, profRef, getContext());
        } else if (CategoryBoolean) {

            Query profRef = childRef.orderByChild("category").equalTo(Category);
            mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, profRef, getContext());
        } else {


            mStoryAdapter = new StoryAdapter(StoryObject.class, R.layout.story_custom_ui, StoryHolder.class, childRef, getContext());

        }*/

            storyRecyclerview.setLayoutManager(linearLayoutManager);
            storyRecyclerview.setAdapter(mStoryAdapter);

        } catch (Exception e) {

        }
        return rootview;
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
