package com.hkapps.storykorner;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyScreenFragment extends Fragment {
    private SharedPreferences sharedPreference;
    private ImageView errImg;
    private TextView errText;
    public EmptyScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_empty_screen, container, false);

        errImg = (ImageView) rootView.findViewById(R.id.errImg);
        errText = (TextView) rootView.findViewById(R.id.errText);


        sharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int errormsg = sharedPreference.getInt("error", 404);


        switch (errormsg) {

            case 1:
                errImg.setImageResource(R.drawable.ic_import_contacts_white_24dp);
                break;

            case 2:
                errImg.setImageResource(R.drawable.ic_people_outline_white_24dp);
                break;

            case 3:
                errImg.setImageResource(R.drawable.ic_people_white_24dp);
                break;

            case 4:
                errImg.setImageResource(R.drawable.ic_chevron_right_white_24dp);
                break;


        }


        return rootView;
    }

}
