package com.hkapps.storykorner;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

    public static final String MyPREFERENCES = "profile";
    private static final int GALLERY_INTENT = 2;
    public SharedPreferences sharedPreferences;
    String checkingid = "";
    private TextView uname;
    private ImageButton prof_image;
    private StorageReference mStorageRef;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseRef;
    private LinearLayout prof_stories;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        checkingid = sharedPreference.getString("profile_id", userid);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(checkingid);

        prof_stories = (LinearLayout) rootview.findViewById(R.id.prof_stories);
        prof_image = (ImageButton) rootview.findViewById(R.id.prof_image);

        uname = (TextView) rootview.findViewById(R.id.prof_uname);

        mProgressDialog = new ProgressDialog(getActivity());


        if (checkingid.equals(userid)) {

            prof_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT);

                }
            });
        }

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                uname.setText(dataSnapshot.child("uname").getValue().toString());

                if (dataSnapshot.child("photolink").exists()) {
                    String photo = dataSnapshot.child("photolink").getValue().toString();
                    Picasso.with(getActivity()).load(photo).fit().centerCrop().into(prof_image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        prof_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("storyuserid", checkingid);
                edit.commit();

                Fragment fragment = new StoriesFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("profile", false);
        edit.commit();




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
