package inesctec.gresbas.Utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import inesctec.gresbas.User.User_Fragments.User_Locais;

import static android.content.Context.NOTIFICATION_SERVICE;

public class my_BroadcastReceiver extends BroadcastReceiver {

    private String intent_action;

    //------------------------------------------------------------------------------------------------- Callback Broadcast

    public void onReceive(Context context, Intent intent) {

        intent_action = intent.getAction().toString();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        CheckNetwork checkNetwork = new CheckNetwork();

        switch (intent_action) {

            case "location_notification_action_YES_s": //Resposta 'SIM' sem redes wifi na lista

                /*notificationManager.cancel(987654321);
                Toast.makeText(context, "YES_s", Toast.LENGTH_SHORT).show();*/

                start_add_local(context);

                break;

            case "location_notification_action_NO_s": //Resposta 'NÃO' sem redes wifi na lista

               /* notificationManager.cancel(987654321);
                Toast.makeText(context, "NO_s", Toast.LENGTH_SHORT).show();*/

                break;

            case "android.net.conn.CONNECTIVITY_CHANGE":

                checkNetwork.check(context);

                break;

            case "android.location.PROVIDERS_CHANGED":

                checkNetwork.check(context);

                break;

            case "android.net.wifi.WIFI_STATE_CHANGED":

                checkNetwork.check(context);

                break;

        }

    }

    public void start_add_local(final Context context) {

        Intent intent1 = new Intent(context.getApplicationContext(), User_Locais.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

    }

}