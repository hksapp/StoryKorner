package com.hkapps.storykorner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;
    public static final String TAG = "" ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private BottomNavigationView mBottomNav;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragmentManager = getSupportFragmentManager();
        mAuth = FirebaseAuth.getInstance();


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("profile", true);
        edit.commit();

         transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, new StoriesFragment()).commit();

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);

/* BottomNavView Colors
        int[][] state = new int[][]{
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed

        };

        int[] color = new int[]{
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE
        };

        final ColorStateList csl = new ColorStateList(state, color);

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed

        };

        final int[] colors = new int[]{
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE
        };

        final ColorStateList csl2 = new ColorStateList(states, colors); */


        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // handle desired action here
                // One possibility of action is to replace the contents above the nav bar
                // return true if you want the item to be displayed as the selected item


                switch (item.getItemId()) {



                    case R.id.menu_search:
                        fragment = new SearchFragment();
                        break;

                    case R.id.menu_notifications:
                        fragment = new NotificationFragment();
                        break;

                    case R.id.menu_stories:

                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putBoolean("profile", true);
                        edit.commit();
                        fragment = new StoriesFragment();
                        break;

                    case R.id.menu_create:
                        fragment = new CreateFragment();
                        break;


                    case R.id.menu_profile:

                        SharedPreferences prof = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor profedit = prof.edit();
                        profedit.putString("profile_id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                        profedit.commit();


                        fragment = new ProfileFragment();
                        //    mBottomNav.setItemBackgroundResource(R.color.nav_profile_color);
                        //  mBottomNav.setItemIconTintList(csl);
                        // mBottomNav.setItemTextColor(csl);
                        break;

                    default: fragment = new StoriesFragment();
                        break;
                }

                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;

            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in


                    // Email Verificatiom
                    if(user.isEmailVerified())
                    Toast.makeText(getApplicationContext(),"Welcome "+user.getDisplayName(),Toast.LENGTH_SHORT).show();
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(getApplicationContext(),"Check your Email",Toast.LENGTH_SHORT).show();

                    }
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    //Send Uname & UID to Firebase!

                    mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uname");
                    mUserRef.setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());


                } else {

                   startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.drawable.sk)
                                    .setTheme(R.style.FullscreenTheme)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }




    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                //  final FragmentTransaction transaction = fragmentManager.beginTransaction();
                //transaction.replace(R.id.main_container, new SettingsFragment()).commit();
                AuthUI.getInstance()
                        .signOut(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }


}
