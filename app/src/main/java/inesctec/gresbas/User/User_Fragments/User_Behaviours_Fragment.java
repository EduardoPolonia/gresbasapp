
package inesctec.gresbas.User.User_Fragments;

        import android.content.Intent;
        import android.graphics.Typeface;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.widget.BottomNavigationView;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import inesctec.gresbas.R;
        import inesctec.gresbas.User.Tabs_Impact.impact_tab;
        import inesctec.gresbas.Utils.Utils;


public class User_Behaviours_Fragment extends Fragment {

    private View view;

    private BottomNavigationView bottomNavigationView;

    public User_Behaviours_Fragment() {
        // Required empty public constructor
    }


    TextView titlepage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_behaviours, container, false);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"Raleway/Raleway-Medium.ttf");

        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigationbeh);

        titlepage = (TextView) getActivity().findViewById(R.id.title_page) ;
        titlepage.setText("Behaviours");

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.action_home)
                        {
                            User_Home_Fragment user_home_fragment = new User_Home_Fragment();
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction()
                                    .replace(R.id.content_user_frameContainer, new User_Home_Fragment(), Utils.Fragmento_User_Home).commit();
                            titlepage.setText("Home");
                        }

                        if (item.getItemId() == R.id.action_myscore)
                        {
                            User_Level_Fragment user_level_fragment = new User_Level_Fragment();
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction()
                                    .replace(R.id.content_user_frameContainer, new User_Level_Fragment(), Utils.Fragmento_EP_Frequency).commit();
                            titlepage.setText("My EP Score");
                        }

                        if (item.getItemId() == R.id.action_scoreboard)
                        {
                            Intent intent = new Intent(getActivity(), Restaurants.class);
                            startActivity(intent);
                        }

                        if (item.getItemId() == R.id.action_impact)
                        {
                            Intent i = new Intent(getActivity(), impact_tab.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }

                        if (item.getItemId() == R.id.action_messages)
                        {
                            User_Messages_Fragment user_messages_fragment = new User_Messages_Fragment();
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction()
                                    .replace(R.id.content_user_frameContainer, new User_Messages_Fragment(), Utils.Fragmento_Messages_Frequency).commit();
                            titlepage.setText("Messages");
                        }

                        return true;
                    }
                });





        return view;
    }

}
