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
                errText.setText("No Stories to show..!");
                break;

            case 2:
                errImg.setImageResource(R.drawable.ic_people_outline_white_24dp);
                errText.setText("No Users to show");
                break;

            case 3:
                errImg.setImageResource(R.drawable.ic_people_white_24dp);
                errText.setText("No Users to show");
                break;

            case 4:
                errImg.setImageResource(R.drawable.ic_chevron_right_white_24dp);
                errText.setText("You haven't saved any Stories yet..!");
                break;

            case 5:
                errImg.setImageResource(R.drawable.ic_import_contacts_white_24dp);
                errText.setText("No Content to Show");
                break;

            case 6:
                errImg.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_white_48dp);
                errText.setText("No Internet Connection");
                break;

            case 7:
                errImg.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_white_48dp);
                errText.setText("Your Story will be uploaded as soon as \n your phone connects to Internet");
                break;


        }


        return rootView;
    }

}
