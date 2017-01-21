package com.hkapps.storykorner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by kamal on 23-12-2016.
 */

public class NotificationListener extends Service {

    public static boolean isRunning;
    private DatabaseReference notifying;
    private Target mTarget;
    private NotificationCompat.Builder mBuilder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    //When the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        isRunning = true;


        DatabaseReference notif = FirebaseDatabase.getInstance().getReference().child("Posted_Stories");
        notifying = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Notifications");

        notifying.keepSynced(true);
        notifying.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot1, String s) {

                if (dataSnapshot1.child("liker_id").exists()) {

                    String uid = dataSnapshot1.child("liker_id").getValue().toString();

                final DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


                imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("photolink").exists()) {
                            String photo = dataSnapshot.child("photolink").getValue().toString();
                            // Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.notify_imgview);
                            showNotifications(dataSnapshot1.child("liker_name").getValue().toString(), photo, "liked");
                        } else {
                            showNotifications(dataSnapshot1.child("liker_name").getValue().toString(), null, "liked");

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                } else if (dataSnapshot1.child("cmt_user_id").exists()) {


                    String uid = dataSnapshot1.child("cmt_user_id").getValue().toString();

                    final DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


                    imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child("photolink").exists()) {
                                String photo = dataSnapshot.child("photolink").getValue().toString();
                                // Picasso.with(context).load(photo).fit().centerCrop().into(viewHolder.notify_imgview);
                                showNotifications(dataSnapshot1.child("cmt_name").getValue().toString(), photo, "Commented on");
                            } else {
                                showNotifications(dataSnapshot1.child("cmt_name").getValue().toString(), null, "Commented on");

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



       /* nRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                    //   Toast.makeText(NotificationListener.this, ds.child("userid").getValue().toString(), Toast.LENGTH_SHORT).show();


                    ds.child("likes").getRef().addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                            Map postdata = new HashMap();
                            postdata.put("liker_id",dataSnapshot.getKey().toString());
                            postdata.put("liker_name", dataSnapshot.getValue().toString());


                            postdata.put("post_id", dataSnapshot.getRef());
                            postdata.put("timestamp", ServerValue.TIMESTAMP);


                            notifying.push().setValue(ds.getKey().toString());

                            showNotifications(dataSnapshot.getValue().toString());

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/

        return START_STICKY;
    }


    private void showNotifications(String username, String pic, String reacted) {

        mBuilder = new NotificationCompat.Builder(this);


        // mBuilder.setLargeIcon(Picasso.with(getBaseContext()).load(pic).get());

        mBuilder.setContentTitle(username);
        mBuilder.setContentText(username + " " + reacted + " your post");
        mBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_black_24dp);
        mBuilder.setLargeIcon(icon);
        //  Toast.makeText(this, pic, Toast.LENGTH_SHORT).show();
        loadImage(getApplicationContext(), pic);

     /*   if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.bestfrnds);
        } else {
           mBuilder.setSmallIcon(R.drawable.bestfrnds);
        }*/





        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);


        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("notif", true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        // PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);
        // PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);

        mBuilder.setContentIntent(resultPendingIntent);


        mBuilder.setVibrate(new long[]{500, 500});

       /* Notification note = mBuilder.build();
        note.defaults |= Notification.DEFAULT_VIBRATE;
        note.defaults |= Notification.DEFAULT_SOUND;
*/

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

       /* final int notifId = 1337;
        final RemoteViews contentView = mBuilder.getContentView();
        final int iconId = android.R.id.icon;
        Picasso.with(getApplicationContext()).load(pic).into(contentView, iconId, notifId,mBuilder.build() );
*/



    }


    void loadImage(Context context, String url) {


        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                //Do something
                mBuilder.setLargeIcon(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(url)
                .into(mTarget);
    }

}
