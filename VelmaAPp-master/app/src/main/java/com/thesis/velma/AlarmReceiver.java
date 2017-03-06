package com.thesis.velma;

/**
 * Created by admin on 12/21/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.thesis.velma.helper.OkHttp;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "MyActivity";
    Context mcontext;
    private static final String YES_ACTION = "com.thesis.velma.YES";

    @Override
    public void onReceive(final Context context, Intent intent) {

        mcontext = context;
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();

        showNotification(intent.getStringExtra("unix"), intent.getStringExtra("name"), intent.getStringExtra("description"), intent.getStringExtra("location"), intent.getStringExtra("start"), intent.getStringExtra("end"), intent.getStringExtra("dateS"), intent.getStringExtra("dateE"), intent.getStringExtra("people"));
    }

    private void showNotification(String unixtime, String eventname, String des, String loc, String timeStart, String timeEnd, String dateStart, String dateEnd, String invitedContacts) {
        String received;

//        Log.d(TAG, "Received: " +unixtime + " " + eventname + " " + des + " " + loc + " " + timeStart + " " + timeEnd + " " + dateStart + " " + dateEnd + " " + invitedContacts);
        String eventDescription = "Please click to open velma app";
        final String EXTRA_VOICE_REPLY = "extra_voice_reply";
        int notificationId = new Random().nextInt();

        Intent okayIntent = new Intent(mcontext, acceptEvent.class);
        Bundle okayBundle = new Bundle();
        okayBundle.putInt("ID", notificationId);
        okayBundle.putString("unix", unixtime);
        okayBundle.putString("name", eventname);
        okayBundle.putString("description", des);
        okayBundle.putString("location", loc);
        okayBundle.putString("start", timeStart);
        okayBundle.putString("end", timeEnd);
        okayBundle.putString("dateS", dateStart);
        okayBundle.putString("dateE", dateEnd);
        okayBundle.putString("people", invitedContacts);
        okayIntent.putExtras(okayBundle);

        Intent cancelIntent = new Intent(mcontext, declineEvent.class);
        Bundle cancelBundle = new Bundle();
        cancelBundle.putInt("ID", notificationId);
//        cancelBundle.putString("unix", unixtime);
//        cancelBundle.putString("name", eventname);
//        cancelBundle.putString("description", des);
//        cancelBundle.putString("location", loc);
//        cancelBundle.putString("start", timeStart);
//        cancelBundle.putString("end", timeEnd);
//        cancelBundle.putString("dateS", dateStart);
//        cancelBundle.putString("dateE", dateEnd);
//        cancelBundle.putString("people", invitedContacts);
        cancelIntent.putExtras(cancelBundle);


        String replyLabel = "My Input";
        String[] replyChoices = mcontext.getResources().getStringArray(R.array.reply_choices);

        android.support.v4.app.NotificationCompat.BigTextStyle bigStyle = new android.support.v4.app.NotificationCompat.BigTextStyle();
        bigStyle.bigText(eventDescription);

        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel(replyLabel)
                .setChoices(replyChoices)
                .build();
        android.support.v4.app.NotificationCompat.Action action = new android.support.v4.app.NotificationCompat.Action.Builder(R.drawable.ic_reply_white_24dp, "My Action", PendingIntent.getActivity(mcontext, 0111, okayIntent, 0))
                .addRemoteInput(remoteInput)
                .build();


        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        Notification notification = new android.support.v4.app.NotificationCompat.Builder(mcontext)
                .setStyle(inboxStyle)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.velmalogo)
                .setContentTitle(eventname)
                .setAutoCancel(true)
//                .setContentTitle("You have an event in " +loc+" at " +timeStart+" until "+timeEnd)
                .addAction(R.drawable.ic_check_circle_blue_500_18dp,"Okay", PendingIntent.getActivity(mcontext, 0111, okayIntent, PendingIntent.FLAG_UPDATE_CURRENT)) // #0
                .addAction(R.drawable.ic_cancel_blue_500_18dp, "Cancel Event",PendingIntent.getActivity(mcontext, 0111, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT))// #1
                .addAction(R.drawable.ic_assignment_late_blue_500_18dp, "I'll be coming late", PendingIntent.getActivity(mcontext, 0111, okayIntent, 0))
                .setContentText("You have an event in " +loc+" at " +timeStart+" until "+timeEnd).setStyle(bigStyle)
                .extend(new android.support.v4.app.NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                .extend(new android.support.v4.app.NotificationCompat.WearableExtender().addAction(action))
                .build();

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
//        PendingIntent contentIntent = PendingIntent.getActivity(mcontext, 0,
//                new Intent(mcontext, LandingActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        notification.contentIntent = PendingIntent.getActivity(mcontext, 0,
                new Intent(mcontext, LandingActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mcontext);
//        int notificationId = 1;
        notificationManager.notify(notificationId, notification);


    }


}