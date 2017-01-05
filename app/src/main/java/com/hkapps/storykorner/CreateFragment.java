package com.hkapps.storykorner;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment {


    DatabaseReference post_stories;

    Spinner spnr;
    String cat;

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

    public CreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_create, container, false);

        final EditText create_story = (EditText) rootview.findViewById(R.id.create_story);
        final EditText create_title = (EditText) rootview.findViewById(R.id.create_title);

        ///  Button save_story = (Button) rootview.findViewById(R.id.save_story);
        Button publish_story = (Button) rootview.findViewById(R.id.publish_story);


        post_stories = FirebaseDatabase.getInstance().getReference("Posted_Stories");




        spnr = (Spinner) rootview.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, celebrities);

        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spnr.getSelectedItemPosition();
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

                if (create_title.getText().toString().trim().length() != 0 && create_story.getText().toString().trim().length() != 0) {

                    Map postdata = new HashMap();
                    postdata.put("title", create_title.getText().toString());
                    postdata.put("story", create_story.getText().toString());
                    postdata.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    postdata.put("username", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    postdata.put("timestamp", ServerValue.TIMESTAMP);
                    postdata.put("category",cat);

                    post_stories.push().setValue(postdata);


                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putBoolean("profile", false);
                    edit.commit();

                    getFragmentManager().beginTransaction().replace(R.id.main_container, new StoriesFragment()).commit();


                } else {

                    Toast.makeText(getActivity(), "Nothing to Post...!", Toast.LENGTH_SHORT).show();

                }
            }


        });


        return rootview;
    }

}
