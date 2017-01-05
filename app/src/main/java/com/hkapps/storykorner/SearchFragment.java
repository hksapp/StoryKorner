package com.hkapps.storykorner;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {
    String CategoryName = "";

    Button loveAndRomance,horror,sciencefiction,humor,drama,satire,adventureAndAction,mystery,biography,shortStory;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_search, container, false);


        LinearLayout search_parent = (LinearLayout) rootview.findViewById(R.id.search_parent);
        loveAndRomance = (Button) rootview.findViewById(R.id.loveAndRomance);
        horror = (Button) rootview.findViewById(R.id.horror);
        sciencefiction = (Button) rootview.findViewById(R.id.sciencefiction);
        humor = (Button) rootview.findViewById(R.id.humor);
        drama = (Button) rootview.findViewById(R.id.drama);
        satire = (Button) rootview.findViewById(R.id.satire);
        adventureAndAction = (Button) rootview.findViewById(R.id.adventureAndAction);
        mystery = (Button) rootview.findViewById(R.id.mystery);
        biography = (Button) rootview.findViewById(R.id.biography);
        shortStory = (Button) rootview.findViewById(R.id.shortStory);

        loveAndRomance.setOnClickListener(this);
        horror.setOnClickListener(this);
        sciencefiction.setOnClickListener(this);
        humor.setOnClickListener(this);
        drama.setOnClickListener(this);
        satire.setOnClickListener(this);
        adventureAndAction.setOnClickListener(this);
        mystery.setOnClickListener(this);
        biography.setOnClickListener(this);
        shortStory.setOnClickListener(this);



        search_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Hi", Toast.LENGTH_SHORT).show();
                Fragment fragment = new SearchStoriesUserFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        return rootview;
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("CategoryBoolean", true);
        edit.putBoolean("profile", false);

        edit.putBoolean("storysearch_boolean", false);
        edit.commit();


        int id = view.getId();
        switch (id){
            case R.id.loveAndRomance:
                edit.putString("Category", "Love & Romance");
                edit.commit();
                break;
            case R.id.horror:
                edit.putString("Category", "Horror");
                edit.commit();
                break;
            case R.id.sciencefiction:
                edit.putString("Category", "Science Fiction");
                edit.commit();
                break;
            case R.id.humor:
                edit.putString("Category", "Humor");
                edit.commit();
                break;
            case R.id.drama:
                edit.putString("Category", "Drama");
                edit.commit();
                break;
            case R.id.satire:
                edit.putString("Category", "Satire");
                edit.commit();
                break;
            case R.id.adventureAndAction:
                edit.putString("Category", "Adventure & Action");
                edit.commit();
                break;
            case R.id.mystery:
                edit.putString("Category", "Mystery");
                edit.commit();
                break;
            case R.id.biography:
                edit.putString("Category", "Biography");
                edit.commit();
                break;
            case R.id.shortStory:
                edit.putString("Category", "Short Story");
                edit.commit();
                break;
        }
        Fragment fragment = new StoriesFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
