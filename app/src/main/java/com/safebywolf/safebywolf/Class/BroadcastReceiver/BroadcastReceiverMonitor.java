package com.safebywolf.safebywolf.Class.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.safebywolf.safebywolf.Service.MyFirebaseMessagingService;

public class BroadcastReceiverMonitor extends BroadcastReceiver {
    //public static String MENSAJE="mensaje";
    @Override
    public void onReceive(Context context, Intent intent) {
        //LocalBroadcastManager.getInstance(context).registerReceiver(this,new IntentFilter(MENSAJE));
        Intent service = new Intent(context,  MyFirebaseMessagingService.class);
        service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start service:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(service);
        } else {
            context.startService(service);
        }
    }
}
