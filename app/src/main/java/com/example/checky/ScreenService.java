package com.example.checky;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class ScreenService extends Service {

    private BroadcastReceiver yourReceiver;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
//        Toast.makeText(getApplicationContext(), "Service start",Toast.LENGTH_LONG).show();
        super.onCreate();
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction("android.intent.action.USER_PRESENT");
        theFilter.addAction("android.intent.action.SCREEN_ON");
        this.yourReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                SharedPreferences sp = context.getSharedPreferences("count", Context.MODE_PRIVATE);
                if (Intent.ACTION_USER_PRESENT.equals(action)) { // 开屏
                    Log.e("tag", "screen on");
                    SharedPreferences.Editor editor = sp.edit();
                    int  num = sp.getInt(Config.TODAY,0);
                    editor.putInt(Config.TODAY,num+1);
                    editor.apply();
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                    Log.e("tag","screen off");
                }
            }
        };
        // Registers the receiver so that your service will listen for
        // broadcasts
        this.registerReceiver(this.yourReceiver, theFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Do not forget to unregister the receiver!!!
        this.unregisterReceiver(this.yourReceiver);
    }

}