package com.hkapps.storykorner;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by kamal on 10-01-2017.
 */
public class NotifyAdapter extends FirebaseRecyclerAdapter<NotifyObject, NotifyHolder> {

    public NotifyAdapter(Class<NotifyObject> modelClass, int modelLayout, Class<NotifyHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(NotifyHolder viewHolder, NotifyObject model, int position) {


        viewHolder.liker_name.setText(model.getLiker_name());


    }
}
