package inesctec.gresbas.User.Popup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import inesctec.gresbas.R;
import inesctec.gresbas.RecyclerAdapters.CustomRecyclerAdapter;
import inesctec.gresbas.RecyclerAdapters.PointsWeekRecyclerAdapter;
import inesctec.gresbas.data.config.RetrofitInstance;
import inesctec.gresbas.data.model.model_files.EarnedPoints;
import inesctec.gresbas.data.model.model_data.EarnedPointsData;
import inesctec.gresbas.data.model.model_files.PointsWeek;
import inesctec.gresbas.data.model.model_data.PointsWeekData;
import inesctec.gresbas.data.routes.GameClient;
import inesctec.gresbas.data.routes.UserClient;
import io.ghyeok.stickyswitch.widget.StickySwitch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class popup_MyEpScore extends Activity {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<EarnedPoints> earnedPointsList;

    List<PointsWeek> pointsWeeklist;

    private View view;
    TextView title;


    private static Retrofit retrofit = RetrofitInstance.getRetrofitInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup__my_ep_score);
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "Raleway/Raleway-Medium.ttf");
        title = (TextView) findViewById(R.id.titlepop);
        title.setTypeface(typeface);
        TextView finish = (TextView) findViewById(R.id.txtclose);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Global variables admin
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final String userid = pref.getString("userid", null);
        final String username = pref.getString("username", null);
        final String userpass = pref.getString("userpass", null);

        recyclerView = (RecyclerView) findViewById(R.id.recycleViewContainer);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        earnedPointsList = new ArrayList<>();

        pointsWeeklist = new ArrayList<>();
        getEarnedPoints(username, userpass, userid);


        StickySwitch stickySwitch = (StickySwitch) findViewById(R.id.stick_switch);

        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                if (s.equals("Total")) {
                    getEarnedPoints(username, userpass, userid);
                }
                if (s.equals("Week")) {
                    getEarnedPointsWeek(username, userpass, userid);
                }
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

                mAdapter = new CustomRecyclerAdapter(getApplicationContext(), earnedPointsList.getPointstype());
                recyclerView.setAdapter(mAdapter);
                title.setText("My Points");

            }

            @Override
            public void onFailure(Call<EarnedPointsData> call, Throwable t) {
/*
                Toast.makeText(activity_pop__my_ep_score.this, t.getMessage(), Toast.LENGTH_SHORT).show();
*/
            }
        });

    }


    public void getEarnedPointsWeek(String username, String pass, String userid) {
        // BEGIN WS
        GameClient gameClient = retrofit.create(GameClient.class);
        String base = username + ":" + pass;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<PointsWeekData> call = gameClient.getPointsWeek(userid, authHeader);
        call.enqueue(new Callback<PointsWeekData>() {
            @Override
            public void onResponse(Call<PointsWeekData> call, retrofit2.Response<PointsWeekData> response) {
                PointsWeekData pointsweeklist = response.body();

                mAdapter = new PointsWeekRecyclerAdapter(getApplicationContext(), pointsweeklist.getPointstypeWeek());
                recyclerView.setAdapter(mAdapter);

                if (mAdapter.getItemCount() == 0) {
                    title.setText("Nothing to show");
                } else {
                    title.setText("My Points");
                }

            }

            @Override
            public void onFailure(Call<PointsWeekData> call, Throwable t) {
/*
                Toast.makeText(activity_pop__my_ep_score.this, t.getMessage(), Toast.LENGTH_SHORT).show();
*/
            }
        });

    }


}
