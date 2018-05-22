package inesctec.gresbas.User.User_Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import inesctec.gresbas.R;
import inesctec.gresbas.RecyclerAdapters.ScoreBoardRecyclerAdapter;
import inesctec.gresbas.User.Popup.popup_MyEpScore;
import inesctec.gresbas.data.config.RetrofitInstance;
import inesctec.gresbas.data.model.model_files.EarnedPoints;
import inesctec.gresbas.data.model.model_data.EarnedPointsData;
import inesctec.gresbas.data.model.model_files.Level;
import inesctec.gresbas.data.model.model_data.LevelData;
import inesctec.gresbas.data.model.model_data.ScoreBoardData;
import inesctec.gresbas.data.routes.GameClient;
import inesctec.gresbas.data.routes.UserClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;


public class User_Level_Fragment extends Fragment {

    private View view;

    private BottomNavigationView bottomNavigationView;
    ImageView iv;
    String level = "0";
    String points = "0";

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<EarnedPoints> earnedPointsList;
    List<Level> userLevelist;

    ImageButton pop;
    TextView txtclosepop;

    RecyclerView recyclerViewscore;
    RecyclerView.Adapter mAdapterscore;
    RecyclerView.LayoutManager layoutManagerscore;

    List<EarnedPoints> scoreBoardList;

    Dialog dialog;

    // Create handle for the RetrofitInstance
    private static Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    private static final String DIALOG_DATE = "date";

    public User_Level_Fragment() {
        // Required empty public constructor
    }

    TextView titlepage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_level, container, false);

        pop = (ImageButton) view.findViewById(R.id.medalhalevel);


        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(getActivity(), popup_MyEpScore.class);
                startActivity(k);
            }
        });

 /*       bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigationlev);

        bottomNavigationView.setSelectedItemId(R.id.action_myscore);


        titlepage = (TextView) getActivity().findViewById(R.id.title_page);
        titlepage.setText("My EP Score");

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.action_home) {
                            User_Home_Fragment user_home_fragment = new User_Home_Fragment();
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction()
                                    .replace(R.id.content_user_frameContainer, new User_Home_Fragment(), Utils.Fragmento_User_Home).commit();
                            titlepage.setText("Home");
                        }

                        if (item.getItemId() == R.id.action_myscore) {
                            User_Level_Fragment user_level_fragment = new User_Level_Fragment();
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction()
                                    .replace(R.id.content_user_frameContainer, new User_Level_Fragment(), Utils.Fragmento_EP_Frequency).commit();
                            titlepage.setText("My EP Score");
                        }

                        if (item.getItemId() == R.id.action_scoreboard) {
                            User_Score_Fragment user_score_fragment = new User_Score_Fragment();
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction()
                                    .replace(R.id.content_user_frameContainer, new User_Score_Fragment(), Utils.Fragmento_Score_Frequency).commit();
                            titlepage.setText("My Team");
                        }

                        if (item.getItemId() == R.id.action_impact) {
                            Intent i = new Intent(getActivity(), impact_tab.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }

                        if (item.getItemId() == R.id.action_messages) {
                            User_Messages_Fragment user_messages_fragment = new User_Messages_Fragment();
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction()
                                    .replace(R.id.content_user_frameContainer, new User_Messages_Fragment(), Utils.Fragmento_Messages_Frequency).commit();
                            titlepage.setText("Messages");
                        }

                        return true;
                    }
                });*/


        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Raleway/Raleway-Medium.ttf");


        Typeface typeface2 = Typeface.createFromAsset(getContext().getAssets(), "Raleway/Raleway-Italic.ttf");
        //TextView tv2 = (TextView) view.findViewById(R.id.nivel_user);
        //tv2.setTypeface(typeface2);

TextView title = (TextView) view.findViewById(R.id.titlescore);
title.setTypeface(typeface);
        // Global variables admin
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userid = pref.getString("userid", null);
        String username = pref.getString("username", null);
        String userpass = pref.getString("userpass", null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewContainer);
        recyclerViewscore = (RecyclerView) view.findViewById(R.id.recycleViewContainerscore);


        layoutManager = new LinearLayoutManager(getActivity());
        layoutManagerscore = new LinearLayoutManager(getActivity());

        recyclerViewscore.setLayoutManager(layoutManagerscore);

        earnedPointsList = new ArrayList<>();
        userLevelist = new ArrayList<>();
        scoreBoardList = new ArrayList<>();

        getlevel(username, userpass);
        getEarnedPoints(username, userpass, userid);
        setScoreBoard(username, userpass);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void getlevel(String username, String userpass) {

        /* DEFAULT VALUES
        iv = (ImageView) view.findViewById(R.id.medalhalevel);
        iv.setImageResource(R.drawable.medal1);
        */

        // BEGIN WS
        UserClient userClient = retrofit.create(UserClient.class);
        String base = username + ":" + userpass;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<LevelData> call = userClient.getUserLevel(authHeader);
        call.enqueue(new Callback<LevelData>() {
            @Override
            public void onResponse(Call<LevelData> call, retrofit2.Response<LevelData> response) {
                LevelData levelData = response.body();
                if (!levelData.getLevel().isEmpty()) {
                    String level = levelData.getLevel().firstElement().getIdlevel().trim();

                    iv = (ImageView) view.findViewById(R.id.medalhalevel);

                    if (level == "1") {
                        iv.setImageResource(R.drawable.medal1);
                    } else if (level == "2") {
                        iv.setImageResource(R.drawable.medal2);
                    } else if (level == "3") {
                        iv.setImageResource(R.drawable.medal3);
                    } else if (level == "4") {
                        iv.setImageResource(R.drawable.medal4);
                    } else {
                        iv.setImageResource(R.drawable.medal1);
                    }

                } else {
                    iv = (ImageView) view.findViewById(R.id.medalhalevel);
                    iv.setImageResource(R.drawable.medal1);
                }
            }

            @Override
            public void onFailure(Call<LevelData> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void getEarnedPoints(String username, String pass, String userid) {
        // BEGIN WS
        UserClient userClient = retrofit.create(UserClient.class);
        String base = username + ":" + pass;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<EarnedPointsData> call = userClient.getEarnedPoints(userid, authHeader);
        call.enqueue(new Callback<EarnedPointsData>() {
            @Override
            public void onResponse(Call<EarnedPointsData> call, retrofit2.Response<EarnedPointsData> response) {
                EarnedPointsData earnedPointsList = response.body();


            }

            @Override
            public void onFailure(Call<EarnedPointsData> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void setScoreBoard(String username, String userpass) {
        // BEGIN WS
        GameClient gameClient = retrofit.create(GameClient.class);
        String base = username + ":" + userpass;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<ScoreBoardData> call = gameClient.getScoreboard(authHeader);
        call.enqueue(new Callback<ScoreBoardData>() {
            @Override
            public void onResponse(Call<ScoreBoardData> call, retrofit2.Response<ScoreBoardData> response) {
                ScoreBoardData scoreBoardList = response.body();

                mAdapterscore = new ScoreBoardRecyclerAdapter(getActivity().getApplicationContext(), scoreBoardList.getLevel());
                recyclerViewscore.setAdapter(mAdapterscore);

            }

            @Override
            public void onFailure(Call<ScoreBoardData> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}
