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
        final long tmp = model.getTimestamp();
        Date date = new Date(tmp);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String tym = formatter.format(date);

        viewHolder.notify_time.setText(tym);

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


    }
}
