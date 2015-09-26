package com.example.cory.annotate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddWifiNote extends Activity {
    String bssid;
    WifiInfo info;
    WifiNetwork network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi_note);
        Intent intent = getIntent();
        this.info = intent.getParcelableExtra("WifiInfo");
        if(info == null){
            network = intent.getParcelableExtra("WifiNetwork");
            bssid = network.getBSSID();
            setCurrentNetworkName(network.getSSID());
        } else {
            bssid = info.getBSSID();
            setCurrentNetworkName(info);
        }
    }

    private void setCurrentNetworkName(String ssid) {
        TextView currentNetwork = (TextView) findViewById(R.id.currentNetworkName);
        currentNetwork.setTextSize(20);
        currentNetwork.setText(ssid);
    }

    public void saveNote(View view){
        DBManager manager = new DBManager(this);
        TextView body = (TextView) findViewById(R.id.note_body);
        TextView title = (TextView) findViewById(R.id.note_title);
        Note note = new Note(title.getText().toString(), body.getText().toString(), null, null);
            if(network != null){
                network = new WifiNetwork(network.getSSID(), network.getBSSID(), network.getRSSI(), null);
                network.setId(manager.saveNetwork(network));
            } else{
                network = new WifiNetwork(info.getSSID(), info.getBSSID(), info.getRssi(), null);
                network.setId(manager.saveNetwork(network));
            }
            note.setNetworkId(network.getId());
            long newNote = manager.saveNote(note);
            if(newNote < 0) {
                makeToast("Couldn't save the note");
            } else {
                makeToast("Note saved!");
                Note queriedNote = manager.getNote(newNote);
                makeToast(queriedNote.getNoteTitle());
            }
    }

    public void goHome(View view){
        Intent intent = new Intent(this, Annotate.class);
        startActivity(intent);
    }

    public void makeToast(String message){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public void setCurrentNetworkName(WifiInfo info){
        TextView currentNetwork = (TextView) findViewById(R.id.currentNetworkName);
        String ssid = info.getSSID();
        currentNetwork.setTextSize(20);
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
