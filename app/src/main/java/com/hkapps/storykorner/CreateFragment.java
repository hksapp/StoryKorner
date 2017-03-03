package com.hkapps.storykorner;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment {

    DatabaseReference post_stories;
    Spinner spnr;
    String cat;
    int position = 0;
    String[] celebrities = {
            "None",
            "Love & Romance",
            "Science Fiction",
            "Horror",
            "Humor",
            "Drama",
            "Satire",
            "Adventure & Action",
            "Mystery",
            "Biography",
            "Short Story"


    };
    private boolean cFrag = true;
    private EditText create_story;
    private EditText create_title;


    public CreateFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_create, container, false);

        create_story = (EditText) rootview.findViewById(R.id.create_story);
        create_title = (EditText) rootview.findViewById(R.id.create_title);






        ///  Button save_story = (Button) rootview.findViewById(R.id.save_story);
        Button publish_story = (Button) rootview.findViewById(R.id.publish_story);


        post_stories = FirebaseDatabase.getInstance().getReference("Posted_Stories");


        spnr = (Spinner) rootview.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.customspinner, celebrities);

        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        position = spnr.getSelectedItemPosition();
                        cat = celebrities[position];
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                }
        );


        publish_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cFrag = false;


                if (create_title.getText().toString().trim().length() != 0 && create_story.getText().toString().trim().length() != 0) {

                    Map postdata = new HashMap();
                    postdata.put("title", create_title.getText().toString());
                    postdata.put("story", create_story.getText().toString());
                    postdata.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    postdata.put("username", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    postdata.put("timestamp", ServerValue.TIMESTAMP);
                    postdata.put("category", cat);
                    postdata.put("title_lower", create_title.getText().toString().toLowerCase());

                    final String pushid = post_stories.push().getKey();
                    postdata.put("post_id", pushid);
                    post_stories.child(pushid).setValue(postdata);

                    DatabaseReference mOwnRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("newsfeed");
                    mOwnRef.child(pushid).child("story_id").setValue(pushid);

                    DatabaseReference mFollowerRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("followers");

                    mFollowerRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                String follower_id = ds.getKey().toString();

                                DatabaseReference mNewsFeedRef = FirebaseDatabase.getInstance().getReference().child("Users").child(follower_id).child("newsfeed");

                                mNewsFeedRef.child(pushid).child("story_id").setValue(pushid);


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("storiesfragment", 4);
                    edit.commit();


                    if (!isNetworkConnected()) {


                        edit.putInt("error", 7);
                        edit.commit();


                        Fragment fragment = new EmptyScreenFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment);
                        fragmentTransaction.commit();

                    } else {


                        getFragmentManager().beginTransaction().replace(R.id.main_container, new StoriesFragment()).commit();

                    }


                } else {

                    Toast.makeText(getActivity(), "Nothing to Post...!", Toast.LENGTH_SHORT).show();

                }


            }


        });


        return rootview;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (cFrag) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("cTitle", create_title.getText().toString());
            edit.putString("cStory", create_story.getText().toString());
            edit.putInt("cCatPos", position);

            edit.commit();
        } else {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor edit = sp.edit();
            edit.remove("cStory");
            edit.remove("cTitle");
            edit.remove("cCatPos");
            edit.commit();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        create_story.setText(sharedPreference.getString("cStory", ""));
        create_title.setText(sharedPreference.getString("cTitle", ""));

        spnr.setSelection(sharedPreference.getInt("cCatPos", 0));


    }
}
