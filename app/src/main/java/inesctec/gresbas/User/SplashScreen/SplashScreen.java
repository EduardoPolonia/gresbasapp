package inesctec.gresbas.User.SplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import inesctec.gresbas.Login_Registo.Login_Registo;
import inesctec.gresbas.R;



public class SplashScreen extends Activity {



    //------------------------------------------------------------------------------------------------- Class Override's ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView tv = (ImageView) findViewById(R.id.img1);
        ImageView iv = (ImageView) findViewById(R.id.img2);


        Animation myanim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.mytransition);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);

        final Intent i = new Intent(this, Login_Registo.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Thread timer = new Thread() {
            public void run() { //Temporizador

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }

        };
        timer.start();
    }

    private static Activity getActivity(final View view) {
        return (Activity) view.findViewById(android.R.id.content).getContext();
    }
}




