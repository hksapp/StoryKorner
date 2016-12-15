package com.hkapps.storykorner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final int GALLERY_INTENT = 2;
    private TextView uname;
    private ImageButton prof_image;
    private StorageReference mStorageRef;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        uname = (TextView) rootview.findViewById(R.id.prof_uname);
        uname.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());





        return rootview;
    }


}
