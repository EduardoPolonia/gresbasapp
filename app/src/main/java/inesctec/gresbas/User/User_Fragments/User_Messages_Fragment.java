
package inesctec.gresbas.User.User_Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import inesctec.gresbas.RecyclerAdapters.MessagesRecyclerAdapter;
import inesctec.gresbas.R;
import inesctec.gresbas.User.Tabs_Impact.impact_tab;
import inesctec.gresbas.Utils.Utils;
import inesctec.gresbas.data.config.RetrofitInstance;
import inesctec.gresbas.data.model.model_files.Messages;
import inesctec.gresbas.data.model.model_data.MessagesData;
import inesctec.gresbas.data.routes.GameClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;


public class User_Messages_Fragment extends Fragment {

    private View view;

    private BottomNavigationView bottomNavigationView;


    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<Messages> MessagesList;

    // Create handle for the RetrofitInstance
    private static Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

    public User_Messages_Fragment() {
        // Required empty public constructor
    }

    TextView titlepage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_messages, container, false);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Raleway/Raleway-Medium.ttf");


        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigationmes);

        bottomNavigationView.setSelectedItemId(R.id.action_messages);


        titlepage = (TextView) getActivity().findViewById(R.id.title_page);
        titlepage.setText("Messages");

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
                            Intent intent = new Intent(getActivity(), Restaurants.class);
                            startActivity(intent);
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
                });


        // Create LIST
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userid = pref.getString("userid", null);
        final String username = pref.getString("username", null);
        final String userpass = pref.getString("userpass", null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        MessagesList = new ArrayList<>();

        getMessage(username, userpass, userid);


        return view;
    }


    public void getMessage(String username, String pass, String userid) {
        // BEGIN WS
        GameClient gameClient = retrofit.create(GameClient.class);
        String base = username + ":" + pass;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<MessagesData> call = gameClient.getMessage(userid, authHeader);
        call.enqueue(new Callback<MessagesData>() {
            @Override
            public void onResponse(Call<MessagesData> call, retrofit2.Response<MessagesData> response) {
                MessagesData messagelist = response.body();

                mAdapter = new MessagesRecyclerAdapter(getActivity().getApplicationContext(), messagelist.getMessageMenu());
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<MessagesData> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
