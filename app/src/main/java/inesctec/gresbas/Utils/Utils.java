package inesctec.gresbas.Utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;



import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import inesctec.gresbas.R;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    //Email Validation pattern
    public static final String email_validation_pattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    //Fragments Tags
    public static final String Fragmento_Login = "Login_Fragment";
    public static final String Fragmento_Registar = "Registo_Fragment";
    public static final String Fragmento_User_Home = "User_Home";
    public static final String Fragmento_EP_Frequency = "EP Fragment";
    public static final String Fragmento_Impact_Frequency = "My Impact Fragment";
    public static final String Fragmento_Team_Frequency = "My Team Fragment";
    public static final String Fragmento_Messages_Frequency = "Messages Fragment";
    public static final String Fragmento_Score_Frequency = "Score Board Fragment";
    public static final String Fragmento_Behaviours_Frequency = "Behaviours Fragment";
    public static final String Fragmento_Games_Frequency = "Mini Games Fragment";
    public static final String Fragmento_Events_Frequency = "Events Fragment";
    public static final String Fragmento_Money_Frequency = "Money Fragment";
    public static final String Fragmento_Electricity_Frequency = "Electricity Fragment";
    public static final String Fragmento_Emissions_Frequency = "Co2 Emissions Fragment";
    public static final String Fragmento_Trees_Frequency = "Trees Fragment";
    public static final String Fragmento_Frequency_Frequency = "LogIn Frequency Fragment";

    static AlertDialog.Builder dlgAlert;
    //static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

    //Start task after Delay
    public static void delay(int secs, final DelayCallback delayCallback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, secs);
    }

    //How to user Delay on Activity
/*
    Utils.delay(2500, new Utils.DelayCallback() {
        @Override
        public void afterDelay() {
            // Do something after delay

        }
    });
*/

    //Start service
    public static void start_service(Context context, Class cls) {

        Intent intent = new Intent(context, cls);
        context.startService(intent);

    }

    //Stop service
    public static void stop_service(Context context, Class cls) {

        Intent intent = new Intent(context, cls);
        context.stopService(intent);

    }





    public static void semInternetDialog(Context context) {

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage("Sem ligação à Internet");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

    }

    //add to database log and add to fakeCache
    public static void log(final Context context, final String mensagem) {

        final SharedPreferences fakeCache = context.getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
        SharedPreferences.Editor editor = fakeCache.edit();

        final String logString = fakeCache.getString("LOG", "");

        String[] itemsLog = logString.split(",");
        List<String> logs = new ArrayList<String>();

        for (int i = 0; i < itemsLog.length; i++) {

            logs.add(itemsLog[i]);

        }

        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat hour = new SimpleDateFormat("HH:mm:ss");

        Date currentTime = Calendar.getInstance().getTime();

        logs.add(date.format(currentTime) + " " + hour.format(currentTime) + "\n" + "Activity: " + context.getClass().getSimpleName() + "\nMessage: " + mensagem);

        StringBuilder stringBuilder = new StringBuilder();

        for (String s : logs) {

            stringBuilder.append(s);
            stringBuilder.append(",");

        }

        editor.putString("LOG", stringBuilder.toString()).apply();


    }

    public interface DelayCallback {
        void afterDelay();
    }


}
