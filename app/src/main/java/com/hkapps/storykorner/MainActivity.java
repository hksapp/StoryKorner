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
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;
    public static final String TAG = "";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private BottomNavigationView mBottomNav;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private DatabaseReference mUserRef;
    private GoogleApiClient mGoogleApiClient;
//    private AdView mAdView;


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sp.edit();
        edit.remove("cStory");
        edit.remove("cTitle");
        edit.remove("cCatPos");
        edit.commit();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4776719075738998~8141250862");

        mAuth = FirebaseAuth.getInstance();


//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in



                    //To open notification fragment on clicking notification

                    Intent n = getIntent();
                    boolean s = n.getBooleanExtra("notif", false);

                    if (s) {
                        Fragment fragment = new NotificationFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }



                    startService(new Intent(getApplicationContext(), NotificationListener.class));



                    // Email Verificatiom
                    if (!user.isEmailVerified()) {

                        user.sendEmailVerification();
                        Toast.makeText(getApplicationContext(), "Verify your Account from Email", Toast.LENGTH_SHORT).show();

                    }


                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    //Send Uname & UID to Firebase!

                    mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mUserRef.child("uname").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    mUserRef.child("uname_lower").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString().toLowerCase());
                    mUserRef.child("user_email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    mUserRef.child("userid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                } else {


                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.drawable.sk)
                                    .setTheme(R.style.FullscreenTheme)
                                    .setProviders(
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");


                }
                // ...
            }
        };

        //  startService(new Intent(getApplicationContext(), NotificationListener.class));


//To Open Stories Screen
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("storiesfragment", 4);
        edit.commit();

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
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
                        //To open Stories Screen
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("storiesfragment", 4);
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

                    default:
                        fragment = new StoriesFragment();
                        break;
                }

                final FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.main_container, fragment).commit();
                return true;

            }
        });


// Build GoogleApiClient with AppInvite API for receiving deep links
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(AppInvite.API)
                .build();

        // Check if this app was launched from a deep link. Setting autoLaunchDeepLink to true
        // would automatically launch the deep link if one is found.
        boolean autoLaunchDeepLink = false;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(@NonNull AppInviteInvitationResult result) {
                                if (result.getStatus().isSuccess()) {
                                    // Extract deep link from Intent
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);


                                    // Handle the deep link. For example, open the linked
                                    // content, or apply promotional credit to the user's
                                    // account.

                                    String stid = deepLink.substring(deepLink.indexOf("(") + 1, deepLink.indexOf(")"));

                                    // Toast.makeText(MainActivity.this, stid, Toast.LENGTH_SHORT).show();

                                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor edit = sp.edit();
                                    edit.putString("story_id", stid);
                                    edit.putInt("storiesfragment", 8);
                                    edit.commit();

                                    Fragment fragment = new StoriesFragment();
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.main_container, fragment);

                                    fragmentTransaction.commit();

                                    // ...
                                } else {
                                    Log.d(TAG, "getInvitation: no deep link found.");
                                }
                            }
                        });





    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();


                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("storiesfragment", 4);
                edit.commit();

                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_container, new StoriesFragment()).commit();


                //  startService(new Intent(getApplicationContext(), NotificationListener.class));



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

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                //  final FragmentTransaction transaction = fragmentManager.beginTransaction();
                //transaction.replace(R.id.main_container, new SettingsFragment()).commit();
                AuthUI.getInstance()
                        .signOut(this);
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }


}
