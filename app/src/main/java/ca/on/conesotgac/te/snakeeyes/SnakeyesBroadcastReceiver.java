package ca.on.conesotgac.te.snakeeyes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;


public class SnakeyesBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        //MainActivity.isBatteryLow();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);


        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;

        if(batteryPct <= 15)
        {
            MainActivity.batteryLow = true;
            GameActivity.BatteryStatus = true;
        }
        else
        {
            MainActivity.batteryLow = false;
            GameActivity.BatteryStatus = false;
        }
    }

}

