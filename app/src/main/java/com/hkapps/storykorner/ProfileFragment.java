package com.hkapps.storykorner;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static final String MyPREFERENCES = "profile";
    private static final int GALLERY_INTENT = 2;
    public SharedPreferences sharedPreferences;
    String checkingid = "";
    String checking_name = "";
    private TextView uname, user_email, followers_count, following_count, stories_count, saved_count;
    private ImageButton prof_image;
    private StorageReference mStorageRef;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseRef, mFollowRef, mStoryCountRef;
    private LinearLayout prof_stories, prof_followers, prof_following, prof_saved;
    private Button signout, follow;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_profile, container, false);


        if (!isNetworkConnected()) {

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor edit = sp.edit();
            edit.putInt("error", 6);
            edit.commit();


            Fragment fragment = new EmptyScreenFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }


        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        checkingid = sharedPreference.getString("profile_id", userid);

        mFollowRef = FirebaseDatabase.getInstance().getReference().child("Users");


        mStoryCountRef = FirebaseDatabase.getInstance().getReference().child("Posted_Stories");
        Query ref = mStoryCountRef.orderByChild("userid").equalTo(checkingid);



        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(checkingid);

        prof_stories = (LinearLayout) rootview.findViewById(R.id.prof_stories);
        prof_followers = (LinearLayout) rootview.findViewById(R.id.prof_followers);
        prof_following = (LinearLayout) rootview.findViewById(R.id.prof_following);
        prof_saved = (LinearLayout) rootview.findViewById(R.id.prof_saved);






        prof_image = (ImageButton) rootview.findViewById(R.id.prof_image);

        user_email = (TextView) rootview.findViewById(R.id.user_email);
        signout = (Button) rootview.findViewById(R.id.signout);

        uname = (TextView) rootview.findViewById(R.id.prof_uname);

        follow = (Button) rootview.findViewById(R.id.follow);

        followers_count = (TextView) rootview.findViewById(R.id.followers_count);
        following_count = (TextView) rootview.findViewById(R.id.following_count);
        stories_count = (TextView) rootview.findViewById(R.id.stories_count);
        saved_count = (TextView) rootview.findViewById(R.id.saved_count);




        mProgressDialog = new ProgressDialog(getActivity());


        if (checkingid.equals(userid)) {


            follow.setVisibility(View.GONE);

            prof_saved.setVisibility(View.VISIBLE);

            signout.setVisibility(View.VISIBLE);

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


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stories_count.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("followers").child(userid).exists()) {
                    follow.setText("Following");
                }

                if (dataSnapshot.child("uname").exists()) {
                    uname.setText(dataSnapshot.child("uname").getValue().toString());
                    checking_name = dataSnapshot.child("uname").getValue().toString();
                }

                if (dataSnapshot.child("user_email").exists())
                    user_email.setText(dataSnapshot.child("user_email").getValue().toString());


                if (dataSnapshot.child("photolink").exists()) {
                    String photo = dataSnapshot.child("photolink").getValue().toString();
                    Picasso.with(getActivity()).load(photo).fit().centerCrop().into(prof_image);
                }


                if (dataSnapshot.child("followers").exists()) {

                    followers_count.setText(String.valueOf(dataSnapshot.child("followers").getChildrenCount()));

                }


                if (dataSnapshot.child("following").exists()) {

                    following_count.setText(String.valueOf(dataSnapshot.child("following").getChildrenCount()));

                }

                if (dataSnapshot.child("saved").exists()) {

                    saved_count.setText(String.valueOf(dataSnapshot.child("saved").getChildrenCount()));

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        signout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                AuthUI.getInstance()
                        .signOut(getActivity());
                getActivity().stopService(new Intent(getActivity(), NotificationListener.class));



            }
        });


        prof_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("storyuserid", checkingid);
                edit.putInt("storiesfragment", 1);
                edit.putInt("error", 1);

                edit.commit();

                Fragment fragment = new StoriesFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });


        prof_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("prof_followers_id", checkingid);
                edit.putInt("followfragment", 3);
                edit.putInt("error", 2);

                edit.commit();

                Fragment fragment = new FollowFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        prof_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("prof_followers_id", checkingid);
                edit.putInt("followfragment", 4);
                edit.putInt("error", 3);


                edit.commit();

                Fragment fragment = new FollowFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        prof_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("storiesfragment", 5);
                edit.putInt("error", 4);
                edit.commit();

                Fragment fragment = new StoriesFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });




        if (!checkingid.equals(userid)) {


            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mFollowRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String str = "cool";

                            if (dataSnapshot.child(checkingid).child("followers").exists()) {

                                followers_count.setText(String.valueOf(dataSnapshot.child("followers").getChildrenCount()));

                            }

                            if (dataSnapshot.child(checkingid).child("following").exists()) {

                                following_count.setText(String.valueOf(dataSnapshot.child("following").getChildrenCount()));

                            }


                            if (dataSnapshot.child(checkingid).child("followers").hasChild(userid)) {

                                mFollowRef.child(checkingid).child("followers").child(userid).removeValue();

                                mFollowRef.child(userid).child("following_name").removeValue();
                                mFollowRef.child(userid).child("following").child(checkingid).removeValue();

                                DatabaseReference flwRef = FirebaseDatabase.getInstance().getReference().child("Users").child(checkingid).child("Notifications");
                                Query fQueryRef = flwRef.orderByChild("follower_user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());


                                fQueryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                follow.setText("Follow");
                            } else {


                                mFollowRef.child(checkingid).child("followers").child(userid).child("follower_id").setValue(userid);
                                mFollowRef.child(checkingid).child("followers").child(userid).child("follower_name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                mFollowRef.child(userid).child("following").child(checkingid).child("following_id").setValue(checkingid);
                                mFollowRef.child(userid).child("following").child(checkingid).child("following_name").setValue(checking_name);

                                DatabaseReference flwRef = FirebaseDatabase.getInstance().getReference().child("Users").child(checkingid).child("Notifications");

                                Map postdata = new HashMap();
                                postdata.put("follower_user_id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                postdata.put("follower_name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString());
                                postdata.put("timestamp", ServerValue.TIMESTAMP);
                                flwRef.push().setValue(postdata);


                                follow.setText("Following");

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            });

        }


        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();
            Uri selectedImageUri = data.getData();
           /* Uri selectedImageUri2 = Uri.EMPTY;
            Bitmap im = BitmapFactory.decodeResource(getResources(), R.drawable.blur);

            try {
                im = decodeUri(getActivity(), selectedImageUri, 192);
                selectedImageUri2 = getImageUri(getActivity(), im);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
          /*  try {

            InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size
                selectedImageUri = getImageUri(getActivity(),selectedImage);
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

            }

         catch (FileNotFoundException e) {
            e.printStackTrace();
             Toast.makeText(getContext(), "Exception Occured", Toast.LENGTH_SHORT).show();
        }*/
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

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "title", null);
        return Uri.parse(path);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
