package com.example.cory.context;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AddWifiNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi_note);
        Intent intent = getIntent();
        WifiInfo info = intent.getParcelableExtra("WifiInfo");
        setCurrentNetworkName(info);
    }

    public void setCurrentNetworkName(WifiInfo info){
        TextView currentNetwork = (TextView) findViewById(R.id.currentNetworkName);
        String ssid = info.getSSID();
        currentNetwork.setTextSize(40);
        currentNetwork.setText(ssid);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_wifi_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
