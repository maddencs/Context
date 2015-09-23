package com.example.cory.annotate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by cory on 9/21/15.
 */
public class AvailableNetworksAdapter extends ArrayAdapter<WifiNetwork>{
    private final Context context;
    private final WifiNetwork[] wifiNetworks;

    public AvailableNetworksAdapter(Context context, WifiNetwork[] wifiNetworks) {
        super(context, -1, wifiNetworks);
        this.context = context;
        this.wifiNetworks = wifiNetworks;
    }

    static class ViewHolder{
        TextView ssidView;
        TextView rssiView;
        TextView noteCountView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder();
        final WifiNetwork network = wifiNetworks[position];
        if(convertView == null){
            convertView =  inflater.inflate(R.layout.listitem_available_network, parent, false);
            viewHolder.ssidView = (TextView) convertView.findViewById(R.id.ssid);
            viewHolder.rssiView = (TextView) convertView.findViewById(R.id.signalStrength);
            viewHolder.noteCountView = (TextView) convertView.findViewById(R.id.noteCount);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ssidView.setText(wifiNetworks[position].getSSID());
        viewHolder.rssiView.setText(String.valueOf(wifiNetworks[position].getRSSI()));
        viewHolder.noteCountView.setText("3 notes");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WifiNetwork.class);
                intent.putExtra("WifiNetwork", network);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
