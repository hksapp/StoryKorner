package com.hkapps.storykorner
        ;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchStoriesUserFragment extends Fragment {

    private ViewPager viewPager;
    private TabPagerAdapter mAdapter;
    private boolean storyTab = true;

    public SearchStoriesUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootview = inflater.inflate(R.layout.fragment_search_user_stories, container, false);

        SearchView searchView = (SearchView) rootview.findViewById(R.id.searchview);
        searchView.setQueryHint("Search");
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true);


        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub


            }
        });

        //*** setOnQueryTextListener ***


        TabLayout tabLayout = (TabLayout) rootview.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Stories"));
        tabLayout.addTab(tabLayout.newTab().setText("Users"));


        viewPager = (ViewPager) rootview.findViewById(R.id.pager);
        mAdapter = new TabPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());


        viewPager.setAdapter(mAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                storyTab = tab.getPosition() == 0;

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                storyTab = tab.getPosition() == 0;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub


                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sp.edit();

                if (storyTab) {

                    edit.putInt("storiesfragment", 2);
                    edit.putString("storysearch", newText);
                    edit.commit();

                    Fragment fragment = new StoriesFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.stor, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else {

                    edit.putInt("followfragment", 2);
                    edit.putString("usersearch", newText);
                    edit.commit();

                    Fragment fragment = new FollowFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.user, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }




                return false;


            }
        });



        return rootview;
    }

}
