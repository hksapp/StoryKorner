package com.hkapps.storykorner;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


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


        prof_image = (ImageButton) rootview.findViewById(R.id.prof_image);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        prof_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });




        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_INTENT && requestCode == RESULT_OK) {

            Toast.makeText(getContext(), "Uploading started", Toast.LENGTH_SHORT).show();

            Uri uri = data.getData();

            StorageReference filepath = mStorageRef.child("User_photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getContext(), "Done Uploading...", Toast.LENGTH_SHORT).show();
                }
            });

            filepath.putFile(uri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to Upload...", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
