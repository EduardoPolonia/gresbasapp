package inesctec.gresbas.Utils;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import inesctec.gresbas.R;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        ListView list_log = (ListView) findViewById(R.id.list_log);

        SharedPreferences fakeCache = getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);

        String logString = fakeCache.getString("LOG", "");
        String[] itemsLog = logString.split(",");
        List<String> logs = new ArrayList<String>();

        for (int i = 0; i < itemsLog.length; i++) {

            if (!itemsLog[i].matches("")) {

                logs.add(itemsLog[i]);

            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LogActivity.this, android.R.layout.simple_list_item_1, logs);
        list_log.setAdapter(adapter);

    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {

        //On long press back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            SharedPreferences fakeCache = getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
            SharedPreferences.Editor editor = fakeCache.edit();

            editor.putString("LOG", null).apply(); //clean local log

            Toast.makeText(this, "LOG clean", Toast.LENGTH_SHORT).show();

            finish();

            return true;
        }

        return super.onKeyLongPress(keyCode, event);
    }

}
