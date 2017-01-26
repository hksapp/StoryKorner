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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    public static NotifyAdapter mNotifyAdapter;
    private RecyclerView notifyRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mDatabaseRef, mProfStoriesRef;
    private DatabaseReference childRef, mUserRef;
    private SharedPreferences sharedPreference;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_notification, container, false);


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


        linearLayoutManager = new LinearLayoutManager(getActivity());
        notifyRecyclerView = (RecyclerView) rootview.findViewById(R.id.notify_recycler_view);
        notifyRecyclerView.setHasFixedSize(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Notifications");

        mNotifyAdapter = new NotifyAdapter(NotifyObject.class, R.layout.notify_custom_ui, NotifyHolder.class, childRef, getContext());
        notifyRecyclerView.setLayoutManager(linearLayoutManager);
        notifyRecyclerView.setAdapter(mNotifyAdapter);

        return rootview;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}

