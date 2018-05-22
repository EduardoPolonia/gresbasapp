package inesctec.gresbas.User.Tabs_Impact;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import inesctec.gresbas.R;
import inesctec.gresbas.User.User_Fragments.User;

public class impact_tab extends AppCompatActivity {

    private static FragmentManager fragmentManager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private TabLayout mCustomFontTab;

    ImageButton ib;
    TextView titlepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impact_tab);

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "Raleway/Raleway-Medium.ttf");


        ib = (ImageButton) findViewById(R.id.logotitleimpact);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(impact_tab.this, User.class);
                startActivity(intent);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.bar_user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_chome));

        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        tv.setTypeface(typeface);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    public void setCustomFont() {

        ViewGroup vg = (ViewGroup) mCustomFontTab.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    //Put your font in assests folder
                    //assign name of the font here (Must be case sensitive)
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "Raleway/Raleway-Medium.ttf"));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(impact_tab.this, User.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_impact_tab, menu);
        return true;
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
            Intent intent = new Intent(impact_tab.this, User.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TabImpactMoney1 tab1 = new TabImpactMoney1();
                    return tab1;
                case 1:
                    TabImpactElectricity2 tab2 = new TabImpactElectricity2();
                    return tab2;
                case 2:
                    TabImpactCo2_3 tab3 = new TabImpactCo2_3();
                    return tab3;
                case 3:
                    TabImpactTrees4 tab4 = new TabImpactTrees4();
                    return tab4;
                case 4:
                    TabImpactLoginFrequency5 tab5 = new TabImpactLoginFrequency5();
                    return tab5;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Money";
                case 1:
                    return "Electricity";
                case 2:
                    return "CO2 emissions";
                case 3:
                    return "Trees";
                case 4:
                    return "Login Frequency";
            }
            return null;
        }
    }
}
