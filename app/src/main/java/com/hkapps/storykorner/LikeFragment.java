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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class LikeFragment extends Fragment {

    public static ArrayList<String> list = new ArrayList<String>();
    private RecyclerView likeRecyclerView;
    private LinearLayoutManager lm;
    private DatabaseReference mLikeDb, mAllLikesRef;
    private LikeAdapter mLikeAdapter;
    private int i = 0;


    public LikeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_like, container, false);


        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String likes_post_key = sharedPreference.getString("likes_post_key", "null");


        mAllLikesRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").child(likes_post_key).child("likes");

        // String[] arr = {"Hello","hi","whats app","Heya","What","hell","Oh boy"};
        mAllLikesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //    Toast.makeText(context, getRef(position).toString(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(context, dataSnapshot.getRef().toString(), Toast.LENGTH_SHORT).show();
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                int length = (int) dataSnapshot.getChildrenCount();


                while (i < length) {
                    list.add(iterator.next().getKey().toString());
                    Toast.makeText(getActivity(), list.get(i), Toast.LENGTH_SHORT).show();

                    i++;
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        lm = new LinearLayoutManager(getActivity());
        likeRecyclerView = (RecyclerView) rootview.findViewById(R.id.like_recycler_view);
        likeRecyclerView.setHasFixedSize(true);
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);
        mLikeDb = FirebaseDatabase.getInstance().getReference().child("Posted_Stories");
        likeRecyclerView.setLayoutManager(lm);
        mLikeAdapter = new LikeAdapter(LikeObject.class, R.layout.like_custom_ui, LikeHolder.class, mLikeDb, getContext());

        likeRecyclerView.setAdapter(mLikeAdapter);


        return rootview;
    }

}
