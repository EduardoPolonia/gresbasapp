package inesctec.gresbas.Login_Registo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import inesctec.gresbas.R;
import inesctec.gresbas.Utils.Utils;

public class Login_Registo extends AppCompatActivity {

    private static FragmentManager fragmentManager;

    //------------------------------------------------------------------------------------------------- Class Default Override's ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registo);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_login_registo_frameContainer, new Login_Fragment(), Utils.Fragmento_Login).commit();
        }

    }

    //On back button pressed
    @Override
    public void onBackPressed() {

        Fragment Fragmento_Registar = fragmentManager.findFragmentByTag(Utils.Fragmento_Registar);

        if (Fragmento_Registar != null) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.activity_login_registo_frameContainer, new Login_Fragment(), Utils.Fragmento_Login).commit();
        } else {

            super.onBackPressed();

        }
    }

}