package inesctec.gresbas.Utils;

import android.app.Application;


import inesctec.gresbas.Utils.CheckNetwork;

public class Gresbas extends Application {

    //Start of the app

    @Override
    public void onCreate() {
        super.onCreate();


        CheckNetwork checkNetwork = new CheckNetwork();
        checkNetwork.check(this);

    }


}
