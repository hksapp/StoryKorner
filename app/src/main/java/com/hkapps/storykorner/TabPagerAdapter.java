package com.hkapps.storykorner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by kamal on 05-01-2017.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;

    public TabPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                // Stories fragment activity
                return new StoriesSearchFragment();
            case 1:
                // Username fragment activity
                return new UserSearchFragment();


        }

        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
