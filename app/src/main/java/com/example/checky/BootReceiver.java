package com.example.checky;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context,"开机启动成功",Toast.LENGTH_LONG).show();
        context.startService(new Intent(context, ScreenService.class));
    }
}
