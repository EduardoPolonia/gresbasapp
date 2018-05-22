package inesctec.gresbas.User.User_Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import inesctec.gresbas.Login_Registo.Login_Registo;
import inesctec.gresbas.R;
import inesctec.gresbas.Service.Indoor_Location_Service;
import inesctec.gresbas.Utils.Utils;

import static android.content.Context.MODE_PRIVATE;

public class User_Settings_Fragment extends PreferenceFragment {

    private SharedPreferences fakeCache;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        //Fake_Cache
        fakeCache = getActivity().getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
        editor = fakeCache.edit();

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //Preferences
        final Preference logout = (Preference) findPreference("logout");
        final Preference start = (Preference) findPreference("start_i_s");


        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                dlgAlert.setMessage("Do you want to exit?");
                dlgAlert.setTitle("Alert");
                dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(getActivity(), Indoor_Location_Service.class);
                        getActivity().stopService(intent);

                        editor.putBoolean("login_offline", false).apply();


                        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", "");  // Set Global variable  username
                        editor.putString("userpass", "");  // Set Global variable
                        editor.putString("check", "n");
                        editor.putString("logoff", "y");
                        editor.apply();


                        intent = new Intent(getActivity(), Login_Registo.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                });
                dlgAlert.setNegativeButton("No", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();

                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("logoff", "n");
                editor.apply();

                return false;
            }
        });


        start.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                dlgAlert.setMessage("Do you want to Start Indoor Location?");
                dlgAlert.setTitle("Alert");
                dlgAlert.setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), Indoor_Location_Service.class);
                        getActivity().startService(intent);
                    }
                });

                dlgAlert.setNegativeButton("No", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();


                return false;
            }

        });

    }

}