package com.hkapps.storykorner;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final int GALLERY_INTENT = 2;
    private TextView uname;
    private ImageButton prof_image;
    private StorageReference mStorageRef;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseRef;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());




        uname = (TextView) rootview.findViewById(R.id.prof_uname);
        uname.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        prof_image = (ImageButton) rootview.findViewById(R.id.prof_image);

        mProgressDialog = new ProgressDialog(getActivity());


        prof_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT);

            }
        });


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("photolink").exists()) {
                    String photo = dataSnapshot.child("photolink").getValue().toString();
                    Picasso.with(getActivity()).load(photo).fit().centerCrop().into(prof_image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });










        return rootview;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            mProgressDialog.setMessage("you fool wait na");
            mProgressDialog.show();
            Uri selectedImageUri = data.getData();

            mStorageRef = FirebaseStorage.getInstance().getReference().child("User_Photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            mStorageRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();


                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


                    Uri downloadUri = taskSnapshot.getDownloadUrl();


                    mDatabaseRef.child("photolink").setValue(downloadUri.toString());


                    Picasso.with(getActivity()).load(downloadUri).fit().centerCrop().into(prof_image);

                    mProgressDialog.dismiss();

                }
            });


        }
    }
}
