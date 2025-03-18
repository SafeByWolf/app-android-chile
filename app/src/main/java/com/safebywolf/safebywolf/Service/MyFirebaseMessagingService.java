package com.safebywolf.safebywolf.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import com.safebywolf.safebywolf.Class.Utils.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.safebywolf.safebywolf.Activity.NoticiaActivity;
import com.safebywolf.safebywolf.Activity.NoticiasActivity;
import com.safebywolf.safebywolf.Activity.NovedadActivity;
import com.safebywolf.safebywolf.Activity.NovedadesActivity;
import com.safebywolf.safebywolf.Activity.TensorFlow.DetectorActivity;
import com.safebywolf.safebywolf.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "NOTIFICACION";
    private static String CHANNEL_ID = "canalid";
    private static final int NOTIFICATION_ID = 101;
    static final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};
    PendingIntent pendingIntent;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createAndShowForegroundNotification(remoteMessage);
        }
        else
            startForeground(1, new Notification());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN", s);
    }

    private void mensaje() {
        //Intent i = new Intent(MainActivity.MENSAJE);
        //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createAndShowForegroundNotification(RemoteMessage remoteMessage) {

        Log.v("notificacion","prioridad: "+remoteMessage.getPriority());


        if(remoteMessage.getData().containsKey("tag")){
            String tag = remoteMessage.getData().get("tag");
            Log.v("notificacion","existe key: tag => "+tag);
            if(tag.equalsIgnoreCase("noticia")){
                if(remoteMessage.getData().containsKey("id")){
                    Intent intentNoticia = new Intent(this, NoticiaActivity.class);
                    String id = remoteMessage.getData().get("id");
                    Log.v("notificacion","existe key: id => "+id);
                    intentNoticia.putExtra("id",id);

                    Intent intentNoticiasActivity= new Intent(this, NoticiasActivity.class);

                    Intent intentMonitoreoActivity= new Intent(this, DetectorActivity.class);
                    intentMonitoreoActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    Intent[] intents = new Intent[]{intentMonitoreoActivity, intentNoticiasActivity, intentNoticia};
                    pendingIntent = PendingIntent.getActivities(this, 0, intents, PendingIntent.FLAG_ONE_SHOT);
                }
            }
            else if(tag.equalsIgnoreCase("novedad")){
                if(remoteMessage.getData().containsKey("id")){
                    Intent intentNovedad = new Intent(this, NovedadActivity.class);
                    String id = remoteMessage.getData().get("id");
                    Log.v("notificacion","existe key: id => "+id);
                    intentNovedad.putExtra("id",id);

                    Intent intentNovedadesActivity= new Intent(this, NovedadesActivity.class);

                    Intent intentMonitoreoActivity= new Intent(this, DetectorActivity.class);
                    intentMonitoreoActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    Intent[] intents = new Intent[]{intentMonitoreoActivity, intentNovedadesActivity, intentNovedad};
                    pendingIntent = PendingIntent.getActivities(this, 0, intents, PendingIntent.FLAG_ONE_SHOT);
                }
            } else if(tag.equalsIgnoreCase("actualizar")){
                Log.v("notificacion","existe key: tag => "+tag);
                if(remoteMessage.getData().containsKey("id") && remoteMessage.getData().containsKey("url")) {
                    String url = remoteMessage.getData().get("url");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    pendingIntent = PendingIntent.getActivity(this, 0, browserIntent, PendingIntent.FLAG_ONE_SHOT);

                }
            } else {
                return;
            }

        } else {
            Log.v("notificacion","no existe key: tag");
        }

        String CHANNEL_ID="5842";
        Uri sound = Settings.System.DEFAULT_NOTIFICATION_URI;
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //For API 26+ you need to put some additional code like below:
        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, "nameasd", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.GRAY);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(DEFAULT_VIBRATE_PATTERN);
            mChannel.setDescription("asdasd");
            mChannel.setSound(sound,attributes);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel( mChannel );
            }
        }

        char[] charArray = remoteMessage.getData().get("id").toString().toCharArray();
        int idNotificacion = parseInt(charArray);

        //General code:
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        Notification notification = builder.setOngoing(false)
                .setSmallIcon(R.drawable.logoredondeado)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setGroup(remoteMessage.getData().get("tag"))
                .setContentText(remoteMessage.getData().get("body"))
                .setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(remoteMessage.getData().get("urlImagen").toString())))

                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(DEFAULT_VIBRATE_PATTERN)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(remoteMessage.getData().get("urlImagen").toString())))
                .build();
        notificationManagerCompat.notify(idNotificacion, notification);

        Log.v("notificacion",notification.toString());
    }

    public static int parseInt(char [] c) {
        int valor = 0;
        for(int i=0; i < c.length; i++){
            valor += Character.getNumericValue(c[i]);
        }
        return valor;
    }


    /*
    @TargetApi(26)
    private static void prepareChannel(Context context) {

        final NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);


        if (nm != null) {
            List<NotificationChannel> channelList = nm.getNotificationChannels();

            for (int i = 0; channelList != null && i < channelList.size(); i++) {
                nm.deleteNotificationChannel(channelList.get(i).getId());
            }
        }


        Uri sound = Settings.System.DEFAULT_ALARM_ALERT_URI;
        //Uri sound =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.notification_mp3);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH);

        mChannel.setDescription("hola muindo");
        mChannel.enableVibration(true);
        mChannel.setSound(sound,attributes);
        mChannel.setVibrationPattern(DEFAULT_VIBRATE_PATTERN);
                nm.createNotificationChannel(mChannel);

    }

     */

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
