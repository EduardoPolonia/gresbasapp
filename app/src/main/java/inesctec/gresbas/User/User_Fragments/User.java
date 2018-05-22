package inesctec.gresbas.User.User_Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pushbots.push.Pushbots;

import inesctec.gresbas.Service.Indoor_Location_Service;
import inesctec.gresbas.User.Tabs_Team.team_tab;
import inesctec.gresbas.User.Tabs_Impact.impact_tab;
import inesctec.gresbas.Utils.CustomTypefaceSpan;
import inesctec.gresbas.R;
import inesctec.gresbas.Service.Weather_Service;
import inesctec.gresbas.Utils.Utils;
import inesctec.gresbas.data.config.RetrofitInstance;
import inesctec.gresbas.data.model.model_files.Users;
import inesctec.gresbas.data.model.model_data.UsersData;
import inesctec.gresbas.data.routes.UserClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;



import java.util.ArrayList;
import java.util.List;

public class User extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static FragmentManager fragmentManager;
    private View navigationView_header;
    private Intent intent;

    private TextView textView_nome, textView_email;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private SharedPreferences fakeCache;
    private SharedPreferences.Editor editor;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SharedPreferences sharedPreferences;

    // Create handle for the RetrofitInstance
    private static Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

    TextView user, titlepage;

    List<Users> usersList;
    //------------------------------------------------------------------------------------------------- Class Default Override's ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final String username = pref.getString("username", null);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "Raleway/Raleway-Medium.ttf");


        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setAlias(username);
        Pushbots.sharedInstance().registerForRemoteNotifications();


        titlepage = (TextView) findViewById(R.id.title_page);
        titlepage.setText("Home");
        titlepage.setTypeface(typeface);
        ImageButton ib = (ImageButton) findViewById(R.id.logogesbas);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_user_frameContainer, new User_Home_Fragment(), Utils.Fragmento_User_Home).commit();
            }
        });
        //Declare Everyting
        declarations();

        //Set Default Fragment
        fragmentManager
                .beginTransaction()
                .add(R.id.content_user_frameContainer, new User_Home_Fragment(), Utils.Fragmento_User_Home).commit();


        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }


        //Set "Home" item checked
        navigationView.setCheckedItem(R.id.activity_user_drawer_item_home);


        Utils.start_service(this, Weather_Service.class);

        // SERVIÇO DE LOCALIZAÇÃO INDOOR COM ERROS (precisa de ser totalmente revisto)
        Utils.start_service(this, Indoor_Location_Service.class);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //close Navigation_Drawer
        close_navigationDrawer(false);

        //Get Weather
        Utils.start_service(getApplicationContext(), Weather_Service.class);


        //Get User data from Database
        get_user_data_from_internet();
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "Raleway/Raleway-Medium.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            close_navigationDrawer(true);

        } else {

            if (fragmentManager.findFragmentById(R.id.content_user_frameContainer).getTag().equals(Utils.Fragmento_User_Home)) {

                //Exit app
                super.onBackPressed();

            } else {

                //Go to 'Home' fragment
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_user_frameContainer, new User_Home_Fragment(), Utils.Fragmento_User_Home).commit();
                titlepage.setText("Home");
                navigationView.setCheckedItem(R.id.activity_user_drawer_item_home);

            }
        }
    }


//------------------------------------------------------------------------------------------------- Menu Buttons ---

    public void goEP(View v) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.content_user_frameContainer, new User_Level_Fragment(), Utils.Fragmento_EP_Frequency).commit();
        titlepage.setText("My EP Score");
    }

    public void goElectricity(View v) {
        Intent intent = new Intent(this, impact_tab.class);
        startActivity(intent);
        titlepage.setText("My Impact");
    }

    public void goTeam(View v) {
        Intent intent = new Intent(this, team_tab.class);
        startActivity(intent);
        titlepage.setText("My Team");
    }

    public void goMessages(View v) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.content_user_frameContainer, new User_Messages_Fragment(), Utils.Fragmento_Messages_Frequency).commit();
        titlepage.setText("Messages");
    }

    public void goScoreBoard(View v) {

        Intent intent = new Intent(this, Restaurants.class);
        startActivity(intent);
    }

    public void goBehaviours(View v) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.content_user_frameContainer, new User_Behaviours_Fragment(), Utils.Fragmento_Behaviours_Frequency).commit();
        titlepage.setText("Behaviours");
    }

    public void goGames(View v) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.content_user_frameContainer, new User_Games_Fragment(), Utils.Fragmento_Games_Frequency).commit();
        titlepage.setText("Mini Games");
    }

    public void goEvents(View v) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.content_user_frameContainer, new User_Events_Fragment(), Utils.Fragmento_Events_Frequency).commit();
        titlepage.setText("Events");
    }

    public void goHome() {
        Intent mainIntent = new Intent(this, User_Home_Fragment.class);
        startActivity(mainIntent);
        titlepage.setText("Home");

    }
    //------------------------------------------------------------------------------------------------- Navigation_Drawer Item Selected ---

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.activity_user_drawer_item_home:

                //Set Fragment_Container 'Home'
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_user_frameContainer, new User_Home_Fragment(), Utils.Fragmento_User_Home).commit();
                titlepage.setText("Home");

                //Close Navigation_Drawer
                close_navigationDrawer(true);

                break;


            case R.id.activity_user_drawer_item_ep:

                //Set Fragment_Container 'EP Score'
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_user_frameContainer, new User_Level_Fragment(), Utils.Fragmento_EP_Frequency).commit();
                titlepage.setText("My EP Score");

                //Close Navigation_Drawer
                close_navigationDrawer(true);

                break;

            case R.id.activity_user_drawer_item_impact:

                //Set Fragment_Container 'Electricity'
                Intent intent = new Intent(this, impact_tab.class);
                startActivity(intent);
                titlepage.setText("My Impact");
                //Close Navigation_Drawer
                close_navigationDrawer(true);


                break;


            case R.id.activity_user_drawer_team:

                //Set Fragment_Container 'Team'
                Intent intent1 = new Intent(this, team_tab.class);
                startActivity(intent1);
                titlepage.setText("My Team");

                //Close Navigation_Drawer
                close_navigationDrawer(true);

                break;


            case R.id.activity_user_drawer_item_messages:

                //Set Fragment_Container 'Messages'
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_user_frameContainer, new User_Messages_Fragment(), Utils.Fragmento_Messages_Frequency).commit();
                titlepage.setText("Messages");

                //Close Navigation_Drawer
                close_navigationDrawer(true);

                break;


            case R.id.activity_user_drawer_item_Board:

                //Set Fragment_Container 'Score Board'
                Intent intent2 = new Intent(this, Restaurants.class);
                startActivity(intent2);

                //Close Navigation_Drawer
                close_navigationDrawer(true);

                break;

            case R.id.activity_user_drawer_item_behaviours:

                //Set Fragment_Container 'Behaviours'
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_user_frameContainer, new User_Behaviours_Fragment(), Utils.Fragmento_Behaviours_Frequency).commit();
                titlepage.setText("Behaviours");

                //Close Navigation_Drawer
                close_navigationDrawer(true);

                break;


            case R.id.activity_user_drawer_item_games:

                //Set Fragment_Container 'Mini Games'
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_user_frameContainer, new User_Games_Fragment(), Utils.Fragmento_Games_Frequency).commit();
                titlepage.setText("Mini Games");

                //Close Navigation_Drawer
                close_navigationDrawer(true);

                break;

            case R.id.activity_user_drawer_item_events:

                //Set Fragment_Container 'Events'
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_user_frameContainer, new User_Events_Fragment(), Utils.Fragmento_Events_Frequency).commit();
                titlepage.setText("Events");

                //Close Navigation_Drawer
                close_navigationDrawer(true);

                break;


            case R.id.activity_user_drawer_item_definicoes:

                //Start Activity 'Settings'
                intent = new Intent(getApplicationContext(), User_Settings.class);
                startActivity(intent);

                break;

        /*    case R.id.activity_user_drawer_item_locais:

                //Start Activity 'Locais'
                intent = new Intent(getApplicationContext(), User_Locais.class);
                startActivity(intent);


                break; */
        }

        return true;
    }

    //------------------------------------------------------------------------------------------------- Random ---

    //Close Navigation_Drawer with or without Animation
    public void close_navigationDrawer(Boolean animation) {

        drawer.closeDrawer(GravityCompat.START, animation);

    }

    //Declare Everyting
    public void declarations() {

        //Fake_Cache
        fakeCache = getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
        editor = fakeCache.edit();

        //Declarar variável responsavel pela Shared_Preferences das defenições
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Tool_Bar
        toolbar = (Toolbar) findViewById(R.id.app_bar_user_toolbar);
        setSupportActionBar(toolbar);

        //Navigation_Drawer
        navigationView = (NavigationView) findViewById(R.id.activity_user_navView);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView_header = navigationView.getHeaderView(0);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //Firebase
        //firebaseAuth = FirebaseAuth.getInstance();
        //firebaseUser = firebaseAuth.getCurrentUser();
        //firebaseUserID = firebaseUser.getUid();
        //databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUserID);

        //Declarar Fragment_Manager
        fragmentManager = getSupportFragmentManager();

        //TextViews, Buttons, EditTexts, etc...
        textView_nome = (TextView) navigationView_header.findViewById(R.id.nav_header_user_textView_nome);


    }

    //Set Action_Bar Title
    public void getSupportActionBar_Title(String title) {

        getSupportActionBar().setTitle(title);

    }

    //Get User data from Database on Internet
    public void get_user_data_from_internet() {
        // Global variables admin
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final String username = pref.getString("username", null);
        final String userpass = pref.getString("userpass", null);
        final SharedPreferences.Editor editor = pref.edit();

        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "Raleway/Raleway-Medium.ttf");
        TextView us = (TextView) navigationView_header.findViewById(R.id.nav_header_user_textView_nome);
        us.setText("Welcome, " + username + "!");
        us.setTypeface(typeface);

        // Get User INFO from WS
        usersList = new ArrayList<>();

        UserClient userClient = retrofit.create(UserClient.class);
        String base = username + ":" + userpass;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<UsersData> call = userClient.getUserInfo(authHeader);
        call.enqueue(new Callback<UsersData>() {
            @Override
            public void onResponse(Call<UsersData> call, retrofit2.Response<UsersData> response) {
                UsersData usersData = response.body();
                if (!usersData.getUsers().isEmpty()) {
                    String userid = usersData.getUsers().firstElement().getId();
                    editor.putString("userid", userid); // Set Global variable userid
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<UsersData> call, Throwable t) {
                //Toast.makeText(intent, t.getMessage(), Toast.LENGTH_SHORT).show();
                usersList = null;
            }
        });

 /*
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Convert dataSnapshot Value into Map
                Map<String, String> data = (Map<String, String>) dataSnapshot.child("user_info").getValue();

                //Get Values from Database
               User_nome = data.get("nome").toString();
               User_apelido = data.get("apelido").toString();
                User_email = firebaseUser.getEmail();

                //Set Action_Bar Title
              getSupportActionBar_Title("Olá " + User_nome);

                //Set Name and Email of Navigation_Drawer
           //     textView_nome.setText(User_nome + " " + User_apelido);
                textView_email.setText(User_email);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
*/

    }

}
