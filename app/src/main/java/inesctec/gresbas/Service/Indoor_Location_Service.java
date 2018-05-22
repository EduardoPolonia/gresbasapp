package inesctec.gresbas.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import inesctec.gresbas.R;
import inesctec.gresbas.Utils.Step.StepDetector;
import inesctec.gresbas.Utils.Step.StepListener;
import inesctec.gresbas.Utils.Utils;

public class Indoor_Location_Service extends Service implements SensorEventListener, StepListener {

    private static Double range = 0.10;
    Collection<String> similar;
    String previous_update;
    private SharedPreferences fakeCache;
    private SharedPreferences.Editor editor;
    private WifiManager wifi_service;
    private List<ScanResult> wifiscanList;
    private List<String> nearWifi = new ArrayList<String>();
    private List<String> savedBSSID = new ArrayList<String>();

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;

    //-------------------------------------------------------------------------------------------------

    //remove doplicate items on list
    private static List<String> removeDuplicates(List<String> withDuplicatesCities) {

        Set<String> set = new TreeSet<String>();

        List<String> newList = new ArrayList<String>();

        for (String city : withDuplicatesCities) {
            set.add(city);
        }
        newList.addAll(set);

        return newList;
    }

    //Return most common item on list
    public static <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }

    //------------------------------------------------------------------------------------------------- Class Override's ---

    @Override
    public void onDestroy() {
        super.onDestroy();

        sensorManager.unregisterListener(Indoor_Location_Service.this);

        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    //------------------------------------------------------------------------------------------------- Random ---

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Fake_Cache
        fakeCache = getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
        editor = fakeCache.edit();

        //Firebase
        //firebaseAuth = FirebaseAuth.getInstance();
        //firebaseUser = firebaseAuth.getCurrentUser();
        //firebaseUserID = firebaseUser.getUid();
        //databaseReference = FirebaseDatabase.getInstance().getReference();

        wifi_service = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        sensorManager.registerListener(Indoor_Location_Service.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

        return START_STICKY;
    }

    //Get BSSID's from Saved Locations
    public void getSavedBSSID() {

        //     savedBSSID.clear();

        savedBSSID = removeDuplicates(savedBSSID);

        getNearWifi();

        /*databaseReference.child("locations").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    if (dsp.child("Approved").getValue().toString().equals("true")) {

                        for (int i = 1; i < 5; i++) {

                            try {

                                savedBSSID.add(dsp.child("wifi" + i).child("BSSID").getValue().toString());

                            }catch (Exception e){

                                //Toast.makeText(Indoor_Location_Service.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }


                        }

                    }

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });*/

    }

    //Get Near Wifi's
    public void getNearWifi() {

        if (fakeCache.getBoolean("wifi_on", false) && fakeCache.getBoolean("gps_on", false)) {

            nearWifi.clear();

            wifi_service.startScan();
            wifiscanList = wifi_service.getScanResults();

            for (int i = 0; i < wifiscanList.size(); i++) {

                nearWifi.add(wifiscanList.get(i).BSSID);

            }

            compararBSSID();

        } else {

            compararBSSID();

        }

    }

    //Remover redes que não estiverem na lista de locais salvos
    public void compararBSSID() {

        similar = new HashSet<String>(nearWifi);
        similar.retainAll(savedBSSID);

        if (!similar.isEmpty()) {

            //getInfoSavedLocations();

            getSavedBSSID();

        } else {

        }

    }

    //------------------------------------------------------------------------------------------------- SensorEventListener Override's ---

    //Get Possible Locations
/*    public void getInfoSavedLocations() {

        databaseReference.child("locations").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> data = new ArrayList<String>();
                List<String> have = new ArrayList<String>();

                for (DataSnapshot contact : dataSnapshot.getChildren()) {

                    data.clear();

                    if (contact.child("Approved").getValue().toString().equals("true")) {

                        for (int i = 1; i < 5; i++) {

                            data.add(contact.child("wifi" + i).child("BSSID").getValue().toString());

                        }

                    }

                    List<String> common = new ArrayList<String>(similar);
                    common.retainAll(data);

                    Double wifi1 = Double.parseDouble(contact.child("wifi1").child("distance").getValue().toString().replace(",", "."));
                    Double wifi2 = Double.parseDouble(contact.child("wifi2").child("distance").getValue().toString().replace(",", "."));
                    Double wifi3 = Double.parseDouble(contact.child("wifi3").child("distance").getValue().toString().replace(",", "."));
                    Double wifi4 = Double.parseDouble(contact.child("wifi4").child("distance").getValue().toString().replace(",", "."));

                    for (int i = 0; i < wifiscanList.size(); i++) {

                        if (common.contains(wifiscanList.get(i).BSSID)) {

                            DecimalFormat precision = new DecimalFormat("0.00");
                            double exp = (27.55 - (20 * Math.log10(wifiscanList.get(i).frequency)) + Math.abs(wifiscanList.get(i).level)) / 20.0;

                            double distance = Double.parseDouble(precision.format(Math.pow(10.0, exp)).replace(",", "."));

                            if ((distance > wifi1 - range || distance < wifi1 + range) &&
                                    (distance > wifi2 - range || distance < wifi2 + range) &&
                                    (distance > wifi3 - range || distance < wifi3 + range) &&
                                    (distance > wifi4 - range || distance < wifi4 + range)) {

                                have.add(contact.getKey());

                            }

                        }

                    }

                }

                betterMatch(dataSnapshot, mostCommon(have));

                Toast.makeText(Indoor_Location_Service.this, have.toString(), Toast.LENGTH_SHORT).show(); //TODO: Remove

                editor.putString("locais_possiveis", mostCommon(have)).apply();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    //Get Best Match
    public void betterMatch(DataSnapshot data, String have_wifi) {

        Calendar now = Calendar.getInstance();
        String hours = String.format("%02d", now.get(Calendar.HOUR_OF_DAY)) + "h" + String.format("%02d", now.get(Calendar.MINUTE)) + "m" + String.format("%02d", now.get(Calendar.SECOND)) + "s";

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (!have_wifi.equals(previous_update)) {

            previous_update = have_wifi;

            if (!have_wifi.matches("") || !have_wifi.isEmpty()) {   //Se só houver 1 rede na lista

                Utils.log(Indoor_Location_Service.this, have_wifi);

                Notification n = new Notification.Builder(this)

                        .setContentTitle("Localização atual")
                        .setSmallIcon(R.drawable.ic_map_marker_white_48dp)
                        .setStyle(new Notification.BigTextStyle().bigText(have_wifi + "\n\nLast Update: " + hours))

                        .build();

                notificationManager.notify(987654321, n);

            } else if (have_wifi.matches("") || have_wifi.isEmpty()) {   //Se não houver nenhuma rede na lista

                Intent yesIntent = new Intent("location_notification_action_YES_s");
                PendingIntent yesPendingIntent = PendingIntent.getBroadcast(this, 0, yesIntent, 0);

                Intent noIntent = new Intent("location_notification_action_NO_s");
                PendingIntent noPendingIntent = PendingIntent.getBroadcast(this, 0, noIntent, 0);

                Notification n = new Notification.Builder(this)

                        .setContentTitle("Não foi possivel detetar localização")
                        .setSmallIcon(R.drawable.ic_map_marker_white_48dp)
                        .setStyle(new Notification.BigTextStyle().bigText("Pretende adicionar local?"))
                        .addAction(0, "Sim", yesPendingIntent)
                        .addAction(0, "Não", noPendingIntent)

                        .build();

                notificationManager.notify(987654321, n);

            }
        }

    }
*/
    //On Sensor change
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);
        }

    }

    //On Sensor Accuracy change
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //when detect step do...
    @Override
    public void step(long timeNs) {

        editor.putLong("previous_update_loc", System.currentTimeMillis()).apply();

        getSavedBSSID();

        Toast.makeText(this, "aqui", Toast.LENGTH_SHORT).show();

    }
}

