package com.hkapps.storykorner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kamal on 10-01-2017.
 */
public class NotifyAdapter extends FirebaseRecyclerAdapter<NotifyObject, NotifyHolder> {

    private Context context;

    public NotifyAdapter(Class<NotifyObject> modelClass, int modelLayout, Class<NotifyHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(final NotifyHolder viewHolder, final NotifyObject model, int position) {


        long tmp = model.getTimestamp();
        Date date = new Date(tmp);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String tym = formatter.format(date);


        Long tsLong = System.currentTimeMillis();


        long ctym = tsLong - tmp;

        long seconds = ctym / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days == 0) {

            if (hours != 0) {
                if (hours == 1)
                    viewHolder.notify_time.setText(String.valueOf(hours) + " Hr");
                else
                    viewHolder.notify_time.setText(String.valueOf(hours) + " Hrs");
            } else {
                if (minutes != 0) {
                    if (minutes == 1)
                        viewHolder.notify_time.setText(String.valueOf(minutes) + " Min");
                    else
                        viewHolder.notify_time.setText(String.valueOf(minutes) + " Mins");

                } else {
                    viewHolder.notify_time.setText(String.valueOf(seconds) + " Secs");

                }
            }
        } else {
            if (days == 1)
                viewHolder.notify_time.setText(String.valueOf(days) + " day");
            else
                viewHolder.notify_time.setText(String.valueOf(days) + " days");

        }


      /*  if (ctym <= 60000) {
            viewHolder.notify_time.setText(ctym / 1000 + " Secs ago");
        } else if (ctym <= 120000) {
            viewHolder.notify_time.setText(ctym / 60000 + " Min ago");
        } else if (ctym <= 3600000) {
            viewHolder.notify_time.setText(ctym / 60000 + " Mins ago");

        } else if (ctym <= 7200000) {
            viewHolder.notify_time.setText(ctym / 3600000 + " Hr ago");

        } else if (ctym <= 86400000) {
            viewHolder.notify_time.setText(ctym / 3600000 + " Hrs ago");

        } else if (ctym >= 86400000 && ctym < 86400000 * 2) {
            viewHolder.notify_time.setText(ctym / 86400000 + " Day ago");

        } else if (ctym > 86400000 * 2 && ctym < 86400000 * 30) {
            viewHolder.notify_time.setText(((ctym / (1000*60*60*24)) % 7) + " Days ago");

        } else {
            viewHolder.notify_time.setText(tym);
        }
*/

        if (model.getLiker_id() != null) {


            viewHolder.liker_name.setText(model.getLiker_name());

            DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getLiker_id().toString());

            imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("photolink").exists()) {
                        String photo = dataSnapshot.child("photolink").getValue().toString();
                        Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.notify_imgview);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            viewHolder.liker_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("profile_id", model.getLiker_id());
                    edit.commit();

                    Fragment fragment = new ProfileFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });


            viewHolder.notify_imgview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("profile_id", model.getLiker_id());
                    edit.commit();

                    Fragment fragment = new ProfileFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            viewHolder.notifStory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("story_id", model.getPost_id());
                    edit.putInt("storiesfragment", 8);
                    edit.commit();

                    Fragment fragment = new StoriesFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);

                    fragmentTransaction.commit();
                }
            });

        } else if (model.getCmt_user_id() != null) {

            viewHolder.liker_name.setText(model.getCmt_name());

            viewHolder.tag.setText("Commented on your Story");
            viewHolder.cmttag.setVisibility(View.VISIBLE);
            viewHolder.cmttag.setText("'" + model.getCmt() + "'");

            DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getCmt_user_id().toString());

            imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("photolink").exists()) {
                        String photo = dataSnapshot.child("photolink").getValue().toString();
                        Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.notify_imgview);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            viewHolder.liker_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("profile_id", model.getCmt_user_id());
                    edit.commit();

                    Fragment fragment = new ProfileFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });


            viewHolder.notify_imgview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("profile_id", model.getCmt_user_id());
                    edit.commit();

                    Fragment fragment = new ProfileFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            viewHolder.notifStory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("story_id", model.getPost_id());
                    edit.putInt("storiesfragment", 8);
                    edit.commit();

                    Fragment fragment = new StoriesFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });


        } else {

            viewHolder.liker_name.setText(model.getFollower_name());

            DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getFollower_user_id().toString());

            imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("photolink").exists()) {
                        String photo = dataSnapshot.child("photolink").getValue().toString();
                        Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.notify_imgview);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            viewHolder.tag.setText("started following you");


            viewHolder.liker_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("profile_id", model.getFollower_user_id());
                    edit.commit();

                    Fragment fragment = new ProfileFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });


            viewHolder.notify_imgview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("profile_id", model.getFollower_user_id());
                    edit.commit();

                    Fragment fragment = new ProfileFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

        }
    }


}
