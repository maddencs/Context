package com.example.cory.annotate;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Annotate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotate);
        setCurrentNetworkName();

    }

    private void setCurrentNetworkName(){
        TextView currNet = (TextView) findViewById(R.id.currNet);
        WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String ssid = info.getSSID();
        if(!isWifiConnected()){
            currNet.setText("Not connected to a wifi network.");
        } else {
            currNet.setText(ssid);
        }
    }

    public void viewNetworks(View view){
        Intent intent = new Intent(this, AvailableNetworks.class);
        startActivity(intent);
    }

    public void addWifiNote(View view){
        Intent intent = new Intent(this, AddWifiNote.class);
        WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        intent.putExtra("WifiInfo", info);
        startActivity(intent);
    }

    public boolean isWifiConnected(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo().getTypeName().equals("WIFI");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_context, menu);
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
