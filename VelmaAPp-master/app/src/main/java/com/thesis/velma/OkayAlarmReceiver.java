package com.thesis.velma;

/**
 * Created by admin on 12/21/2016.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class OkayAlarmReceiver extends WakefulBroadcastReceiver {

    Context mcontext;
    private static final String YES_ACTION = "com.thesis.velma.YES";

    @Override
    public void onReceive(final Context context, Intent intent) {

        mcontext = context;
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
//        ringtone.play();
//
//        StopNotification(intent.getStringExtra("id"));

        String action = intent.getAction();

        if(YES_ACTION.equals(action)) {
            Log.v("shuffTest","Pressed YES");
//        } else if(MAYBE_ACTION.equals(action)) {
//            Log.v("shuffTest","Pressed NO");
//        } else if(NO_ACTION.equals(action)) {
//            Log.v("shuffTest","Pressed MAYBE");
   }
    }

    private void StopNotification(String notifId) {

        Log.d("ID", notifId);
        Toast.makeText(mcontext, "Id Received: " + notifId, Toast.LENGTH_LONG).show();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mcontext);
//        int notificationId = 1;
        notificationManager.cancel(Integer.valueOf(notifId));
    }
}