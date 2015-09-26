package com.example.cory.annotate;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class WifiNetwork extends AppCompatActivity implements Parcelable {
    private long mId;
    private String mSSID;
    private String mBSSID;
    private String mCustomSSID;
    private int mRSSI;
    private String mCapabilities;
    private WifiNetwork network;

    public int drawSignalStrength(Context context){
        int level = WifiManager.calculateSignalLevel(mRSSI, 5);
        ImageView icon = new ImageView(context);
        switch(String.valueOf(level)){
            case "0":
                return R.drawable.wifi_sig_none;
            case "1":
                return R.drawable.wifi_sig_1;
            case "2":
                return R.drawable.wifi_sig_2;
            case "3":
                return R.drawable.wifi_sig_3;
            case "4":
                return R.drawable.wifi_sig_full;
        }
        return R.drawable.wifi_sig_none;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBManager manager = new DBManager(this);
        Intent intent = getIntent();
        network = intent.getParcelableExtra("WifiNetwork");
        mSSID = network.getSSID();
        mBSSID = network.getBSSID();
        mRSSI = network.getRSSI();
        mCapabilities = network.getCapabilities();
        String bssid = network.getBSSID();
        Log.d("The network ID", String.valueOf(network.getId()));
        Note[] notes = manager.getNotesByNetworkId(network.getId());
        if(notes.length > 0){
            NoteAdapter adapter = new NoteAdapter(this, notes);
            setContentView(R.layout.activity_note_list);
            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(adapter);
        } else{
            setContentView(R.layout.no_notes);
            LinearLayout layout = (LinearLayout) findViewById(R.id.noNotesLayout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNote();
                }
            });
        }
    }

    public void addNote(){
        Intent intent = new Intent(this, AddWifiNote.class);
        intent.putExtra("WifiNetwork", network);
        startActivity(intent);
    }

    public int getRSSI() {
        return mRSSI;
    }

    public void setRSSI(int RSSI) {
        mRSSI = RSSI;
    }

    public String getCapabilities() {
        return mCapabilities;
    }

    public void setCapabilities(String capabilities) {
        mCapabilities = capabilities;
    }


    public long saveNetwork(){
        // Saves this network to database
        return 0;
    }

    public String getSSID() {
        return mSSID;
    }

    public void setSSID(String SSID) {
        mSSID = SSID;
    }

    public String getBSSID() {
        return mBSSID;
    }

    public void setBSSID(String BSSID) {
        mBSSID = BSSID;
    }

    public String getCustomSSID() {
        return mCustomSSID;
    }

    public void setCustomSSID(String customSSID) {
        mCustomSSID = customSSID;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wifi_network, menu);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mSSID);
        dest.writeString(mBSSID);
        dest.writeString(mCustomSSID);
        dest.writeInt(mRSSI);
        dest.writeString(mCapabilities);
    }
    public WifiNetwork() {}

    public WifiNetwork(String SSID, String BSSID, int RSSI, String capabilities) {
        mSSID = SSID;
        mBSSID = BSSID;
        mRSSI = RSSI;
        mCapabilities = capabilities;
    }

    protected WifiNetwork(Parcel in) {
        mId = in.readLong();
        mSSID = in.readString();
        mBSSID = in.readString();
        mCustomSSID = in.readString();
        mRSSI = in.readInt();
        mCapabilities = in.readString();
    }

    public static final Creator<WifiNetwork> CREATOR = new Creator<WifiNetwork>() {
        @Override
        public WifiNetwork createFromParcel(Parcel in) {
            return new WifiNetwork(in);
        }

        @Override
        public WifiNetwork[] newArray(int size) {
            return new WifiNetwork[size];
        }
    };
}
