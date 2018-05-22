package inesctec.gresbas.Login_Registo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.karan.churi.PermissionManager.PermissionManager;
import com.pushbots.push.Pushbots;

import java.io.IOException;

import inesctec.gresbas.R;
import inesctec.gresbas.User.User_Fragments.User;
import inesctec.gresbas.Utils.Globals;
import inesctec.gresbas.Utils.Utils;
import inesctec.gresbas.data.config.RetrofitInstance;
import inesctec.gresbas.data.model.Auth;
import inesctec.gresbas.data.routes.UserClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WIFI_SERVICE;


public class Login_Fragment extends Fragment {

    private View view;
    private static FragmentManager fragmentManager;
    private SharedPreferences fakeCache;
    private SharedPreferences.Editor editor;

    Globals sharedData = Globals.getInstance();

    Button btnLogin;
    EditText edtUserName, edtPassword;

    CheckBox saveuser;

    TextView createuser, fg;


    private String token = "";
    private ProgressDialog progressDialog;

    // Create handle for the RetrofitInstance
    private static Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

    PermissionManager permissionManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();

        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(getActivity());


        ConnectivityManager manager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = manager.getActiveNetworkInfo();

        boolean isConnected = network != null && network.isConnected() && network.isAvailable();

        if (isConnected == false) {

            final AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            final String message = "Enable a Wifi or mobile Wifi connection!";

            builder.setMessage(message)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int id) {
                                    d.dismiss();
                                }
                            });
            builder.create().show();
        }

        //Fake_Cache
        fakeCache = getActivity().getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
        editor = fakeCache.edit();

        //Inicialize ProgressDialog
        progressDialog = new ProgressDialog(getContext());

        edtUserName = (EditText) view.findViewById(R.id.fragment_login_editText_email);
        edtPassword = (EditText) view.findViewById(R.id.fragment_login_editText_password);
        btnLogin = (Button) view.findViewById(R.id.fragment_login_button_login);
        createuser = (TextView) view.findViewById(R.id.fragment_login_textView_registar);
        saveuser = (CheckBox) view.findViewById(R.id.ch_rememberme);
        String user = (String) edtUserName.getText().toString();
        String pass = (String) edtPassword.getText().toString();

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Raleway/Raleway-Medium.ttf");
        edtUserName.setTypeface(typeface);
        edtPassword.setTypeface(typeface);
        btnLogin.setTypeface(typeface);
        createuser.setTypeface(typeface);
        saveuser.setTypeface(typeface);

        createuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registo_Fragment registo_fragment = new Registo_Fragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.activity_login_registo_frameContainer, new Registo_Fragment(), Utils.Fragmento_Registar).commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUserName.getText().toString();
                String password = edtPassword.getText().toString();

                sharedData.setValue(username);

                String check;
                if (saveuser.isChecked()) {
                    check = "y";
                } else {
                    check = "n";
                }

                SharedPreferences prefs = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username", username);
                editor.putString("password", password);
                editor.putString("check", check);
                editor.commit();
                //validate form
                if (validateLogin(username, password)) {
                    //do login
                    doLogin(username, password, getActivity());
                }
            }

        });


        return view;
    }


    @Override
    public void onStart() { // Inicialize menu
        // TODO Auto-generated method stub
        super.onStart();

        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String username = prefs.getString("username", "");
        String pwd = prefs.getString("password", "");
        String cb = prefs.getString("check", "");
        String logoff = prefs.getString("logoff", "");

        if (logoff == "y") {
            editor.putString("username", null);
            editor.putString("password", null);
            editor.putString("check", "n");
        } else {
            if (cb.equals("y")) {
                edtUserName.setText(username);
                edtPassword.setText(pwd);
                saveuser.setChecked(true);
            } else {
                edtUserName.setText("");
                edtPassword.setText("");
                saveuser.setChecked(false);
            }
        }


    }


    private boolean validateLogin(String username, String password) {
        if (username == null || username.trim().length() == 0) {
            Toast.makeText(getActivity(), "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password == null || password.trim().length() == 0) {
            Toast.makeText(getActivity(), "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        ConnectivityManager manager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = manager.getActiveNetworkInfo();

        boolean isConnected = network != null && network.isConnected() && network.isAvailable();

        final String action = Settings.ACTION_WIFI_SETTINGS;
        final WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        if (isConnected==false) {
            Toast.makeText(getActivity(), "You have to sucesufully connect with WiFi or mobile data!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doLogin(final String username, final String password, Activity v) {
        // Global variables admin
        //final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", username);  // Set Global variable  username
        editor.putString("userpass", password);  // Set Global variable  userpass
        editor.apply();

        // Validate Login by FJCM

        progressDialog.setMessage("Checking credentials...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    try {
                        UserClient userClient = retrofit.create(UserClient.class);
                        String base = username + ":" + password;
                        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
                        Call<Auth> call = userClient.loginUser(authHeader);
                        try {
                            Response<Auth> response = call.execute();
                            if (response.isSuccessful()) {
                                String aa = response.body().getAuthJwt();

                                // OPEN MAIN MENU
                                Bundle bundle = new Bundle();
                                bundle.putString("username", username);
                                bundle.putString("password", password);
                                Intent intent = new Intent(getActivity(), User.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtras(bundle);
                                getActivity().startActivity(intent);
                            }
                            // FAIL LOGIN
                            progressDialog.hide();
                            Toast.makeText(getActivity(), "Username or password not match or not exist", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }
}