package com.hkapps.storykorner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment {


    DatabaseReference post_stories;

    public CreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_create, container, false);

        final EditText create_story = (EditText) rootview.findViewById(R.id.create_story);
        final EditText create_title = (EditText) rootview.findViewById(R.id.create_title);

        Button save_story = (Button) rootview.findViewById(R.id.save_story);
        Button publish_story = (Button) rootview.findViewById(R.id.publish_story);


        post_stories = FirebaseDatabase.getInstance().getReference("Posted_Stories");



        publish_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map postdata = new HashMap();
                postdata.put("title",create_title.getText().toString());
                postdata.put("story",create_story.getText().toString());
                postdata.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                postdata.put("username", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                post_stories.push().setValue(postdata);

                getFragmentManager().beginTransaction().replace(R.id.main_container, new StoriesFragment()).commit();




            }
        });


        return rootview;
    }

}
