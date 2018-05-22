package inesctec.gresbas.User.Tabs_Team;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import inesctec.gresbas.R;
import inesctec.gresbas.RecyclerAdapters.MyTeamRecyclerAdapter;
import inesctec.gresbas.data.config.RetrofitInstance;
import inesctec.gresbas.data.model.model_data.MyTeamData;
import inesctec.gresbas.data.model.model_files.MyTeam;
import inesctec.gresbas.data.routes.GameClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class TabTeamPoints1 extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<MyTeam> MyTeamList;
    // Create handle for the RetrofitInstance
    private static Retrofit retrofit = RetrofitInstance.getRetrofitInstance();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tabteampoints, container, false);

        // Create LIST
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userid = pref.getString("userid", null);
        final String username = pref.getString("username", null);
        final String userpass = pref.getString("userpass", null);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        MyTeamList = new ArrayList<>();

        getMyTeam(username, userpass, userid);

        return rootView;
    }

    public void getMyTeam(String username, String pass, String userid) {
        // BEGIN WS
        GameClient gameClient = retrofit.create(GameClient.class);
        String base = username + ":" + pass;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<MyTeamData> call = gameClient.getTeam(userid, authHeader);
        call.enqueue(new Callback<MyTeamData>() {
            @Override
            public void onResponse(Call<MyTeamData> call, retrofit2.Response<MyTeamData> response) {
                MyTeamData myteamlist = response.body();

                mAdapter = new MyTeamRecyclerAdapter(getActivity().getApplicationContext(), myteamlist.getTeam());
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<MyTeamData> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
