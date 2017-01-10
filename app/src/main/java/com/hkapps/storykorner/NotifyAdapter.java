package com.hkapps.storykorner;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
    protected void populateViewHolder(final NotifyHolder viewHolder, NotifyObject model, int position) {


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


    }
}
