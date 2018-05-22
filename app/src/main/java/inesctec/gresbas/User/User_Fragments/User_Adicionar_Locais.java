package inesctec.gresbas.User.User_Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import inesctec.gresbas.R;
import inesctec.gresbas.Utils.Get_Wifi_List;
import inesctec.gresbas.Utils.Utils;
import inesctec.gresbas.Utils.Utils_Add_Local;
import inesctec.gresbas.Utils.Wifi_Adapter;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class User_Adicionar_Locais extends AppCompatActivity {

    public static final int PERMS_REQUEST_CODE = 123;
    private AlertDialog.Builder alertDialog_builder;
    private ImageButton imageButton_popup;
    private ListView listView_locais;
    private List<ScanResult> wifiscanList;
    private EditText nome_local;
    private MyCustomAdapter dataAdapter = null;
    private ArrayList<Wifi_Adapter> List_selected_wifi;
    private ArrayList<Wifi_Adapter> wifiadapter_list;
    private ArrayList<String> list_user_locations;
    private boolean exist_name;
    private FloatingActionButton fab_add_local;
    private WifiManager wifi_service;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private SharedPreferences fakeCache;
    private SharedPreferences.Editor editor;
    private String firebaseUserID;

    //------------------------------------------------------------------------------------------------- Class Override's ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_adicionar_locais);

        declarations();

        startScanWifi();

        onListeners();

    }

    //------------------------------------------------------------------------------------------------- Check Location Permissions ---

    //Recebe resultado do Request Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allowed = true;

        switch (requestCode) {

            case PERMS_REQUEST_CODE:

                for (int res : grantResults) {

                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);

                }

                break;

            default:

                allowed = false;

                break;

        }

        //If permission is allowed
        if (allowed) {

            startScanWifi();
        }

    }

    //Check if has Permission
    public boolean hasPermissions() {

        int res = 0;

        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }

        return true;
    }

    //Request Permission
    public void requestPerms() {

        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMS_REQUEST_CODE);
        } else {

        }

    }

    //------------------------------------------------------------------------------------------------- Random ---

    //Scan wifi signals nearby
    public void startScanWifi() {

        if (hasPermissions()) {

            scanWifiList();

        } else {

            requestPerms();

        }

    }

    //Declare Everyting
    public void declarations() {

        //Fake_Cache
        fakeCache = getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
        editor = fakeCache.edit();

        //Declarar variável responsavel pela Shared_Preferences das defenições
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Initiate Progress Dialog
        progressDialog = new ProgressDialog(this);

        //Get Manager for Wifi_Service
        wifi_service = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //Firebase

        //TextViews, Buttons, EditTexts, etc...
        listView_locais = (ListView) findViewById(R.id.listView1);
        imageButton_popup = (ImageButton) findViewById(R.id.adicionar_locais_button_popup);
        fab_add_local = (FloatingActionButton) findViewById(R.id.locais_fab_add);
        nome_local = (EditText) findViewById(R.id.adicionar_locais_editText_nome);

    }

    //Return Number of Wifi Selected
    public int num_wifi_selected() {

        Integer quant_wifi_selected = 0;

        List_selected_wifi = dataAdapter.wifiList;

        for (int i = 0; i < List_selected_wifi.size(); i++) {

            Wifi_Adapter wifi = List_selected_wifi.get(i);

            if (wifi.isSelected()) {

                quant_wifi_selected++;

            }

        }

        return quant_wifi_selected;

    }


    private void scanWifiList() {

        wifiscanList = Get_Wifi_List.getWifi(getApplicationContext());

        wifiadapter_list = new ArrayList<Wifi_Adapter>();

        Wifi_Adapter wifilist;

        for (int i = 0; i < wifiscanList.size(); i++) {

            if (!wifiscanList.get(i).SSID.matches("")) {

                wifilist = new Wifi_Adapter(wifiscanList.get(i).SSID,
                        wifiscanList.get(i).BSSID,
                        wifiscanList.get(i).frequency,
                        wifiscanList.get(i).level,
                        false);

                wifiadapter_list.add(wifilist);

            }

        }

        dataAdapter = new MyCustomAdapter(this, R.layout.wifi_info, wifiadapter_list);

        listView_locais.setAdapter(dataAdapter);

    }

    public void onListeners() {

        //If 'Info' Button in clicked
        imageButton_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog_builder = new AlertDialog.Builder(User_Adicionar_Locais.this);
                alertDialog_builder.setTitle("Informação")
                        .setMessage("bla bla bla")
                        .show();

            }
        });

        //If Keyboard 'Ok'/'Done' clicked
        nome_local.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_MASK_ACTION) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    return true;
                } else {
                    return false;
                }
            }
        });

        //Id Fab clicked
        fab_add_local.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (num_wifi_selected() > 4 || num_wifi_selected() < 4 || nome_local.getText().toString().equals("") || nome_local.getText().toString().equals(null)) {

                    if (num_wifi_selected() > 4) {
                        Toast.makeText(getApplicationContext(), "Selecione apenas 4 Redes", Toast.LENGTH_SHORT).show();
                    }

                    if (num_wifi_selected() < 4) {
                        Toast.makeText(getApplicationContext(), "Selecione 4 Redes", Toast.LENGTH_SHORT).show();
                    }

                    if (nome_local.getText().toString().equals("") || nome_local.getText().toString().equals(null)) {
                        nome_local.requestFocus();
                        nome_local.setError("Insira Nome do Local");
                    }

                } else {


                }


            }
        });

    }

    //------------------------------------------------------------------------------------------------- My Custom Adapter Class ---

    private class MyCustomAdapter extends ArrayAdapter<Wifi_Adapter> {

        private ArrayList<Wifi_Adapter> wifiList;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Wifi_Adapter> Wifi_List) {
            super(context, textViewResourceId, Wifi_List);

            this.wifiList = new ArrayList<Wifi_Adapter>();
            this.wifiList.addAll(Wifi_List);

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.wifi_info, null);

                holder = new ViewHolder();
                holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
                holder.info_wifi = (ImageButton) convertView.findViewById(R.id.button_info_wifi);
                convertView.setTag(holder);

                holder.cb.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb2 = (CheckBox) v;
                        Wifi_Adapter wifiadp = (Wifi_Adapter) cb2.getTag();
                        wifiadp.setSelected(cb2.isChecked());
                    }
                });

                holder.info_wifi.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Wifi_Adapter wifiadapter = wifiList.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(User_Adicionar_Locais.this);

                        DecimalFormat precision = new DecimalFormat("0.00");
                        double exp = (27.55 - (20 * Math.log10(wifiadapter.getFrequency())) + Math.abs(wifiadapter.getLevel())) / 20.0;


                        builder.setTitle("Informação")
                                .setMessage("SSID: " + wifiadapter.getSSID() +
                                        "\nBSSID: " + wifiadapter.getBSSID() +
                                        "\nFrequencia: " + wifiadapter.getFrequency() + " MHz" +
                                        "\nNível: " + wifiadapter.getLevel() + " dBm" +
                                        "\nDistância: " + String.valueOf(precision.format(Math.pow(10.0, exp))) + " m")
                                .show();


                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Wifi_Adapter wifiadapter = wifiList.get(position);
            holder.cb.setText(wifiadapter.getSSID());
            holder.cb.setTag(wifiadapter);


            holder.info_wifi.setBackgroundResource(getResources().getIdentifier("drawable/signal" + WifiManager.calculateSignalLevel(wifiadapter.getLevel(), 4), null, getPackageName()));

            return convertView;

        }

        private class ViewHolder {
            CheckBox cb;
            ImageButton info_wifi;
        }

    }

}