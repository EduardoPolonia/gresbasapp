package inesctec.gresbas.User.User_Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import inesctec.gresbas.R;
import inesctec.gresbas.RecyclerAdapters.RestaurantRecyclerAdapter;
import inesctec.gresbas.data.config.RetrofitInstance;
import inesctec.gresbas.data.model.model_files.Restaurant;
import inesctec.gresbas.data.model.model_data.RestaurantData;
import inesctec.gresbas.data.routes.GameClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Restaurants extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    ImageButton ib;

    TextView titlepage;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<Restaurant> RestaurantList;

    String idrest = null;

    String date;


    TextView tvtextmenu;
    ImageView ivnothing;
    TextView titlemenusday;
    String[] MENUID = {"1", "2", "3", "4"}; //ID Restaurant
    String[] MENUNAME = {"INESC BAR", "GRILL - FEUP", "Cafetaria - Restaurante FEUP", "Restaurante do INEGI/IDMEC"}; // Name Restaurant

    // Create handle for the RetrofitInstance
    private static Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        CalendarView cal = (CalendarView) findViewById(R.id.calendarView);
        cal.setDate(Calendar.getInstance().getTimeInMillis(), false, true);

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "Raleway/Raleway-Medium.ttf");

        titlemenusday = (TextView) findViewById(R.id.titlemenusday);
        titlemenusday.setTypeface(typeface);
        tvtextmenu = (TextView) findViewById(R.id.textmenu);
        tvtextmenu.setTypeface(typeface);
        ivnothing = (ImageView) findViewById(R.id.nothingtoshowicon);

        Toolbar toolbar = (Toolbar) findViewById(R.id.bar_user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        tv.setTypeface(typeface);


        int i;
        String[] SPINNERLIST = new String[4];
        for (i = 0; i <= 3; i++) {
            SPINNERLIST[i] = MENUNAME[i];
        }

        // Put array in spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final MaterialBetterSpinner betterSpinner = (MaterialBetterSpinner) findViewById(R.id.list_location);
        betterSpinner.setAdapter(arrayAdapter);

        betterSpinner.setTypeface(typeface);

        betterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        betterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                betterSpinner.showDropDown();
                String selection = (String) parent.getItemAtPosition(position);
                idrest = MENUID[position];
                ((TextView) view).setTextColor(Color.BLACK);
            }
        });


        ib = (ImageButton) findViewById(R.id.logotitleimpact);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Restaurants.this, User.class);
                startActivity(intent);
            }
        });


        // Create LIST
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userid = pref.getString("userid", null);
        final String username = pref.getString("username", null);
        final String userpass = pref.getString("userpass", null);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        RestaurantList = new ArrayList<>();


        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int mes = month + 1;
                date = year + "-" + mes + "-" + dayOfMonth;
                if (idrest == null) {
                    Toast.makeText(Restaurants.this, "You need to select a restaurant!", Toast.LENGTH_SHORT).show();
                } else {
                    setMenu(username, idrest, date, userpass);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void setMenu(String username, String id, String Date, String userpass) {
        // BEGIN WS
        GameClient gameClient = retrofit.create(GameClient.class);
        String base = username + ":" + userpass;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<RestaurantData> call = gameClient.getMenu(id, Date, authHeader);  // 3 => Money data
        call.enqueue(new Callback<RestaurantData>() {
            @Override
            public void onResponse(Call<RestaurantData> call, retrofit2.Response<RestaurantData> response) {
                try {
                    RestaurantData restaurantList = response.body();
                    mAdapter = new RestaurantRecyclerAdapter(getApplicationContext(), restaurantList.getMenu());
                    recyclerView.setAdapter(mAdapter);

                    if (mAdapter.getItemCount() == 0) {
                        tvtextmenu.setText("No menus for this day!");
                        ivnothing.setImageResource(R.drawable.no_food_icon);
                    } else {
                        tvtextmenu.setText("");
                        ivnothing.setImageDrawable(null);
                    }

                } catch (Exception e) {
                    System.out.print(e);
                }
            }

            @Override
            public void onFailure(Call<RestaurantData> call, Throwable t) {
                Toast.makeText(Restaurants.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
