package inesctec.gresbas.User.Tabs_Impact;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import inesctec.gresbas.RecyclerAdapters.ImpactRecyclerAdapter;
import inesctec.gresbas.R;
import inesctec.gresbas.data.config.RetrofitInstance;
import inesctec.gresbas.data.model.model_files.Impact;
import inesctec.gresbas.data.model.model_data.ImpactData;
import inesctec.gresbas.data.routes.GameClient;
import io.ghyeok.stickyswitch.widget.StickySwitch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class TabImpactTrees4 extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<Impact> impactList;

    // Create handle for the RetrofitInstance
    private static Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

    ArrayList<BarEntry> BARENTRY;
    ArrayList<String> BarEntryLabels;
    BarDataSet Bardataset;
    BarData BARDATA;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabimpacttrees, container, false);


        final BarChart barChart = (BarChart) rootView.findViewById(R.id.barchart);

        BARENTRY = new ArrayList<>();

        BarEntryLabels = new ArrayList<String>();


        // Create LIST
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userid = pref.getString("userid", null);
        final String username = pref.getString("username", null);
        final String userpass = pref.getString("userpass", null);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        impactList = new ArrayList<>();

        BARENTRY.clear();

        Calendar myCalendar = Calendar.getInstance();

        myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        System.out.println();
        // Print dates of the current week starting on Monday
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(df.format(myCalendar.getTime()));

        String start = df.format(myCalendar.getTime());
        for (int i = 0; i < 6; i++) {
            myCalendar.add(Calendar.DATE, 1);
        }
        String end = df.format(myCalendar.getTime());

        barChart.setDescription("");

        setImpact(username, start, end, userpass, barChart);


        StickySwitch stickySwitch = (StickySwitch) rootView.findViewById(R.id.stick_switch);

        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                if (s.equals("Week")) {
                    BARENTRY.clear();

                    Calendar myCalendar = Calendar.getInstance();

                    myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                    System.out.println();
                    // Print dates of the current week starting on Monday
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    System.out.println(df.format(myCalendar.getTime()));

                    String start = df.format(myCalendar.getTime());
                    for (int i = 0; i < 6; i++) {
                        myCalendar.add(Calendar.DATE, 1);
                    }
                    String end = df.format(myCalendar.getTime());

                    barChart.setDescription("");

                    setImpact(username, start, end, userpass, barChart);
                }
                if (s.equals("Month")) {
                    Calendar c = Calendar.getInstance();
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    d = c.getTime();
                    //seta primeiro dia do mês
                    c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
                    d = c.getTime();
                    String start = sdf.format(d);
                    System.out.println("Data do primeiro dia do mes: " + sdf.format(d));
                    //seta ultimo dia do mês
                    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                    d = c.getTime();
                    System.out.println("Data do ultimo dia do mes: " + sdf.format(d));
                    String end = sdf.format(d);
                    barChart.setDescription("");
                    setImpact(username, start, end, userpass, barChart);
                }
            }
        });

        ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.constraintLayout);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        return rootView;
    }

    private void setImpact(String username, String beginDate, String endDate, String userpass, final BarChart barChart) {
        // BEGIN WS
        GameClient gameClient = retrofit.create(GameClient.class);
        String base = username + ":" + userpass;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<ImpactData> call = gameClient.getImpact("4", beginDate, endDate, authHeader);  // 4 => Trees data
        call.enqueue(new Callback<ImpactData>() {
            @Override
            public void onResponse(Call<ImpactData> call, retrofit2.Response<ImpactData> response) {
                ImpactData impactList = response.body();

                mAdapter = new ImpactRecyclerAdapter(getActivity().getApplicationContext(), impactList.getImpact());
                recyclerView.setAdapter(mAdapter);
                BARENTRY.clear();
                // CREATE GRAAPH
                AddValuesToBARENTRY(impactList);

                AddValuesToBarEntryLabels(impactList);

                Bardataset = new BarDataSet(BARENTRY, "Trees");

                BARDATA = new BarData(BarEntryLabels, Bardataset);

                Bardataset.setColors(Collections.singletonList(ColorTemplate.getHoloBlue()));

                barChart.setData(BARDATA);

                barChart.animateY(3000);


            }

            @Override
            public void onFailure(Call<ImpactData> call, Throwable t) {
                Toast.makeText(getActivity(), "You don't have any data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void AddValuesToBARENTRY(ImpactData moneyList) {
        int i = 0;
        BARENTRY.clear();
        for (Impact impact : moneyList.getImpact()) {
            BARENTRY.add(new BarEntry(impact.getValueInt().intValue(), i));
            i = i + 1;
        }

    }

    public void AddValuesToBarEntryLabels(ImpactData moneyList) {
        BarEntryLabels.clear();
        for (Impact impact : moneyList.getImpact()) {
            BarEntryLabels.add(impact.getDate());
        }

    }
}
