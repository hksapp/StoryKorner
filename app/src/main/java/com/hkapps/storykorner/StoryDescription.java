package com.hkapps.storykorner;

import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class StoryDescription extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitle , mStory;
    private LinearLayout bbbg;

    private Boolean isFabOpen = false;
    private Boolean isFabShowing = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;


    private int colorCode;
    private float tsize;
    private ScrollView sview;


    private int bgcolor;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_story_description);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        bbbg = (LinearLayout) findViewById(R.id.bgcolor);
        mTitle = (TextView) findViewById(R.id.title_description);
        mStory = (TextView) findViewById(R.id.story_description);
        mTitle.setText(getIntent().getStringExtra("intent_title"));
        mStory.setText(getIntent().getStringExtra("intent_story"));


        sview = (ScrollView) findViewById(R.id.content_story_description);

        //bgcolor = sview.getSolidColor();


        mStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFab();
            }
        });


        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.bg);
        fab2 = (FloatingActionButton)findViewById(R.id.font);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        sview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (i1 > 0) {
                    hideFab();
                } else if (i1 < 0) {
                    showFab();
                }
            }
        });


    }
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:

                animateFAB();
                break;
            case R.id.bg:
                colorCheck();

                break;
            case R.id.font:
                sizeCheck();
                break;
        }
    }


    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;

        }
    }

    public void colorCheck(){

        ColorDrawable cd = (ColorDrawable) sview.getBackground();
        colorCode = cd.getColor();

        if(colorCode == getResources().getColor(R.color.bg1)){
            sview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg2));
            mStory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt2));
            mTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt2));

        }
        else if(colorCode == getResources().getColor(R.color.bg2)){
            sview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg3));
            mStory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt3));
            mTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt3));

        }
        else if(colorCode == getResources().getColor(R.color.bg3)){
            sview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg4));
            mStory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt4));
            mTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt4));

        }
        else if(colorCode == getResources().getColor(R.color.bg4)){
            sview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg1));
            mStory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt1));
            mTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt1));

        }

       /* Paint viewPaint = ((PaintDrawable) sview.getBackground()).getPaint();
        int colorARGB = viewPaint.getColor();
        //if(sview.getBackgroundColor == getResources().getColor(R.color.bg1))
        if(colorARGB == getResources().getColor(R.color.bg1))
        sview.setBackgroundColor(getResources().getColor(R.color.bg2));*/
    }

    public void sizeCheck(){

        tsize = mTitle.getTextSize();

        if(tsize == getResources().getDimension(R.dimen.title1)){
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.title2));
            mStory.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.story2));
        }
        else if(tsize == getResources().getDimension(R.dimen.title2)){


            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.title3));
            mStory.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.story3));

        }
        else if(tsize == getResources().getDimension(R.dimen.title3)){

            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.title4));
            mStory.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.story4));

        }
        else if(tsize == getResources().getDimension(R.dimen.title4)){

            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.title1));
            mStory.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.story1));

        }

    }

    private void hideFab() {

        if (isFabShowing) {
            isFabShowing = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                final Point point = new Point();
                getWindow().getWindowManager().getDefaultDisplay().getSize(point);
                final float translation = fab.getY() - point.y;
                fab.animate().translationYBy(-translation).start();
            } else {
                Animation animation = AnimationUtils.makeOutAnimation(getApplicationContext(), true);
                animation.setFillAfter(true);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fab.setClickable(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                fab.startAnimation(animation);
            }
        }
    }

    private void showFab() {
        if (!isFabShowing) {
            isFabShowing = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                fab.animate().translationY(0).start();
            } else {
                Animation animation = AnimationUtils.makeInAnimation(getApplicationContext(), false);
                animation.setFillAfter(true);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fab.setClickable(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                fab.startAnimation(animation);
            }
        }
    }



}
