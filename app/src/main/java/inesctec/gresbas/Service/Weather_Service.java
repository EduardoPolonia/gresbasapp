package inesctec.gresbas.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;

import inesctec.gresbas.Utils.Get_Weather;


public class Weather_Service extends Service implements LocationListener {

    Handler handler = new Handler();
    private LocationManager locationManager;
    private SharedPreferences.Editor editor;
    private SharedPreferences fakeCache;
    private SharedPreferences sharedPreferences;

    //------------------------------------------------------------------------------------------------- Weather Update every 'x' Millisecond ---

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {

            if (fakeCache.getBoolean("gps_on", false)) {

                getCoordinates();

            }

            handler.postDelayed(runnableCode, 1800000);

        }
    };

    //------------------------------------------------------------------------------------------------- Location Listener ---

    @Override
    public void onLocationChanged(Location location) {

        getWeather(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

        locationManager.removeUpdates(this);
        locationManager = null;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //------------------------------------------------------------------------------------------------- Class Override's ---

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        fakeCache = getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
        editor = fakeCache.edit();

        // handler.post(runnableCode);

        if (fakeCache.getBoolean("gps_on", false)) {

            getCoordinates();

        }

        return START_STICKY;
    }

    //-------------------------------------------------------------------------------------------------

    //Get GPS Coordenates
    public void getCoordinates() {

        try {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    //Get Weather
    public void getWeather(String latitude, String longitude) {

        Get_Weather.placeIdTask asyncTask = new Get_Weather.placeIdTask(new Get_Weather.AsyncResponse() {
            public void processFinish(String weather_location,
                                      String weather_temperature,
                                      String weather_iconText,
                                      String weather_update) {

                int id = getResources().getIdentifier("drawable/w" + weather_iconText, null, getPackageName());

                editor.putString("weather_temp", null).putString("weather_location", null).putInt("weather_icon", 0).apply();
                editor.putString("weather_temp", weather_temperature).putString("weather_location", weather_location).putInt("weather_icon", id).putString("weather_update", weather_update).apply();

            }
        });

        asyncTask.execute(latitude, longitude); //  asyncTask.execute("Latitude", "Longitude")

    }

}
