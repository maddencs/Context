package com.example.cory.annotate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*
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
        ImageView rssiView;
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
            viewHolder.rssiView = (ImageView) convertView.findViewById(R.id.signalStrength);
            viewHolder.noteCountView = (TextView) convertView.findViewById(R.id.noteCount);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.ssidView.setText(wifiNetworks[position].getSSID());
        viewHolder.rssiView.setImageResource(network.drawSignalStrength(context));
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
