package inesctec.gresbas.User.User_Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import inesctec.gresbas.R;
import inesctec.gresbas.Service.Weather_Service;
import inesctec.gresbas.Utils.Utils;


import static android.content.Context.MODE_PRIVATE;

public class User_Home_Fragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    TextView tvsharedpref;
    private ImageView weather_icon;

    private String firebaseUserID;
    private TextView weather_loc, weather_temp, weather_update;
    private SharedPreferences fakeCache;
    private SharedPreferences.Editor editor;
    //private FirebaseUser firebaseUser;
    //private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton button_ep;
    private Button button_electricity;
    private Button button_team;
    private Button button_messages;
    private Button button_score;
    private Button button_behaviours;
    private Button button_games;
    private Button button_events;


    public User_Home_Fragment() {
        // Required empty public constructor
    }

    TextView titlepage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_user_home, container, false);

        titlepage = (TextView) getActivity().findViewById(R.id.title_page) ;
        titlepage.setText("Home");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        declarations();


       /* Map<String, ?> allEntries = fakeCache.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            tvsharedpref.append(entry.getKey() + ": " + entry.getValue().toString() + "\n");
        }

        update_weather();

        if (sharedPreferences.getBoolean("settings_preference_show", false)) {

            CardView cv2 = (CardView) view.findViewById(R.id.cv2);

            cv2.setVisibility(View.VISIBLE);

        } else {

            CardView cv2 = (CardView) view.findViewById(R.id.cv2);

            cv2.setVisibility(View.GONE);

        }
/**/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {

                Utils.start_service(getContext(), Weather_Service.class);

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*update_weather(); /**/



       /* if (sharedPreferences.getBoolean("settings_preference_show", false)) {

            CardView cv2 = (CardView) view.findViewById(R.id.cv2);

            cv2.setVisibility(View.VISIBLE);

        } else {

            CardView cv2 = (CardView) view.findViewById(R.id.cv2);

            cv2.setVisibility(View.GONE);

        }
*/
    }

    //Declare Everyting
    public void declarations() {

        //Fake_Cache
        fakeCache = getActivity().getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
        fakeCache.registerOnSharedPreferenceChangeListener(User_Home_Fragment.this);
        editor = fakeCache.edit();

        //Declarar variável responsavel pela Shared_Preferences das defenições
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //Firebase
        //firebaseAuth = FirebaseAuth.getInstance();
        //firebaseUser = firebaseAuth.getCurrentUser();
        //firebaseUserID = firebaseUser.getUid();
        //databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUserID);


        //Buttons

        ImageButton button_ep =(ImageButton) view.findViewById(R.id.ImageButton01);
/*
        button_ep = (Button) view.findViewById(R.id.ImageButton01);
        button_electricity = (Button) view.findViewById(R.id.ImageButton02);
        button_team = (Button) view.findViewById(R.id.ImageButton03);
        button_messages = (Button) view.findViewById(R.id.ImageButton04);
        button_score = (Button) view.findViewById(R.id.ImageButton05);
        button_behaviours = (Button) view.findViewById(R.id.ImageButton06);
        button_games = (Button) view.findViewById(R.id.ImageButton07);
        button_events = (Button) view.findViewById(R.id.ImageButton08);
        /*
        //TextViews, Buttons, EditTexts, etc...
        weather_icon = (ImageView) view.findViewById(R.id.weather_icon);
        weather_loc = (TextView) view.findViewById(R.id.weather_loc);
        weather_temp = (TextView) view.findViewById(R.id.weather_temp);
        weather_update = (TextView) view.findViewById(R.id.weather_update);


     tvsharedpref = (TextView) view.findViewById(R.id.tvsharedpref);
/**/
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

    }


    //On Shared_Preferences Change



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        //FJCM
        // tvsharedpref.setText("");

        /*
        FJCM
        Map<String, ?> allEntries = fakeCache.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            tvsharedpref.append(entry.getKey() + ": " + entry.getValue().toString() + "\n");
        }
        */

        //FJCM
        // update_weather();

    }

    //Update Weather Info
   public void update_weather() {

        weather_temp.setText(fakeCache.getString("weather_temp", ""));
        weather_loc.setText(fakeCache.getString("weather_location", ""));
        weather_icon.setImageResource(fakeCache.getInt("weather_icon", R.drawable.weather_none_available));
        weather_update.setText(fakeCache.getString("weather_update", null));

    }

}
/**/