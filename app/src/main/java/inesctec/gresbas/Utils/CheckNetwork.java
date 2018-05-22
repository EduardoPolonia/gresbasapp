package inesctec.gresbas.Utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import static android.content.Context.MODE_PRIVATE;

public class CheckNetwork extends AppCompatActivity {

    ConnectivityManager connectivityManager;
    LocationManager locationManager;
    public static final int PERMS_REQUEST_CODE = 123;
    WifiManager wifi;

    SharedPreferences fakeCache;
    SharedPreferences.Editor editor;

    public void check(Context context) {

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        fakeCache = context.getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
        editor = fakeCache.edit();

        has_internet_connection();
        has_gps_on();
        has_wifi_on();

    }

    public void has_internet_connection() {

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            editor.putBoolean("internet_connection", true).apply();

        } else {

            editor.putBoolean("internet_connection", false).apply();

        }

    }

    public void has_gps_on() {

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            editor.putBoolean("gps_on", true).apply();

        } else {

            editor.putBoolean("gps_on", false).apply();

        }

    }

    public void has_wifi_on() {

        if (wifi.isWifiEnabled()) {

            editor.putBoolean("wifi_on", true).apply();

        } else {

            editor.putBoolean("wifi_on", false).apply();

        }

    }


    public void has_location_perm() {

        if (!hasPermissions()) {

            requestPerms();

        }

    }


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

            final Intent ia = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            ia.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ia);

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


}
