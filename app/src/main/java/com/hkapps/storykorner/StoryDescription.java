package com.hkapps.storykorner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StoryDescription extends AppCompatActivity {

    private TextView mTitle , mStory;
    private LinearLayout bbbg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bbbg = (LinearLayout) findViewById(R.id.bgcolor);
        mTitle = (TextView) findViewById(R.id.title_description);
        mStory = (TextView) findViewById(R.id.story_description);

        mTitle.setText(getIntent().getStringExtra("intent_title"));
        mStory.setText(getIntent().getStringExtra("intent_story"));
/*
        Bundle bed = getIntent().getExtras();

     String   pos =  getIntent().getStringExtra("position");


       int[] color = {R.color.first,R.color.sec,R.color.third,R.color.fourth,R.color.fifth,R.color.sixth};

        Toast.makeText(this, pos, Toast.LENGTH_SHORT).show();
        */
//bbbg.setBackgroundColor(pos);


        //Intent i = getIntent();
        //Bundle bed = i.getExtras();
        int pos = getIntent().getIntExtra("position", 5);
        int[] rainbow = {R.color.first, R.color.sec, R.color.third, R.color.fourth, R.color.fifth, R.color.sixth};
        //int[] rainbow = getApplication().getResources().getIntArray(R.array.bg);
        // int postcount = FirebaseDatabase.getInstance().getReference().child("Posted_Stories").get
        //  for (int i = 0; i < l; i++) {
        // View.backgroundColor.setBackgroundColor(rainbow[poss]);

        bbbg.setBackgroundColor(rainbow[pos]);


    }

}
