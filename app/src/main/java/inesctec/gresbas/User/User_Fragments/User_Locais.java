package inesctec.gresbas.User.User_Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.Map;

import inesctec.gresbas.R;
import inesctec.gresbas.Utils.Utils;

public class User_Locais extends AppCompatActivity {

    private ListView list_view_locais;
    private Intent intent;

    private FloatingActionButton fab;

    private ProgressDialog progressDialog;
    private String firebaseUserID;
    private SharedPreferences fakeCache;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    //------------------------------------------------------------------------------------------------- Class Override's ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_locais);

        //Declare Everyting
        declarations();


        onListeners();

        //Get Saved Locations
        getlocallist();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getlocallist();

    }

    //------------------------------------------------------------------------------------------------- Random ---

    public void declarations() {

        //Fake_Cache
        fakeCache = getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
        editor = fakeCache.edit();

        progressDialog = new ProgressDialog(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);



        //TextViews, Buttons, EditTexts, etc...
        fab = (FloatingActionButton) findViewById(R.id.locais_fab);
        list_view_locais = (ListView) findViewById(R.id.list_view_locais_guardados);

    }

    public void onListeners() {

        //Add Location
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (sharedPreferences.getBoolean("settings_preference_auto_add_wifi", false)) {



                } else {

                    intent = new Intent(getApplicationContext(), User_Adicionar_Locais.class);
                    startActivity(intent);

                }

            }
        });

        //Get Info from Saved Locations
        list_view_locais.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                String key = parent.getItemAtPosition(position).toString().replace(" (Waiting For Approval)", "");


            }
        });

        //On Long_Click Saved Location
        list_view_locais.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(User_Locais.this);
                dlgAlert.setMessage("Tem a certeza que pretende eliminar " + parent.getItemAtPosition(position).toString() + " das suas localizações?");
                dlgAlert.setTitle("Aviso");
                dlgAlert.setNegativeButton("Não", null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                dlgAlert.create().show();

                return true;
            }
        });

    }

    public void getlocallist() {

        progressDialog.setMessage("A receber Locais");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

}
