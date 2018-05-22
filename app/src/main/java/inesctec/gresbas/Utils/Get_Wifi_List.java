package inesctec.gresbas.Utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Get_Wifi_List extends AppCompatActivity {

    public static List<ScanResult> getWifi(Context context) {

        WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        wifi_service.startScan();
        List<ScanResult> wifiscanList = wifi_service.getScanResults();

        // Sort by Level
        Comparator<ScanResult> comparator = new Comparator<ScanResult>() {

            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                return (lhs.level < rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
            }
        };

        Collections.sort(wifiscanList, comparator);
        Collections.reverse(wifiscanList); // Sort by Level (mais para menos)

        return wifiscanList;
    }

}
