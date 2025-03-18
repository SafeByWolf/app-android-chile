package com.safebywolf.safebywolf.Class.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.safebywolf.safebywolf.Class.Utils.Log;

public class CallBroadcastReceiver extends BroadcastReceiver {
    public boolean isPhoneCalling = false;
    PhoneCallListener phoneListener = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        call(context);
    }

    private void call(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (phoneListener == null) {
            phoneListener = new PhoneCallListener(context);
            telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }


    public class PhoneCallListener extends PhoneStateListener {
        Context context=null;
        public PhoneCallListener(Context context) {
           this.context = context;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                //Aquí ya detectas que el teléfono esta recibiendo una llamada entrante
                Log.v("llamadas","llamadaaaaaaaaaaaaaaa 1");
                isPhoneCalling = true;
                sendBroadcast();
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.v("llamadas","llamadaaaaaaaaaaaaaaa 2");
                isPhoneCalling = true;
                sendBroadcast();
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                Log.v("llamadas","llamadaaaaaaaaaaaaaaa 3");

                isPhoneCalling = false;
                sendBroadcast();
            }

        }

        public void sendBroadcast(){
            Intent i=new Intent("onCallStateChanged");
            i.putExtra("call" , isPhoneCalling);
            context.sendBroadcast(i);
        }
    }
}
