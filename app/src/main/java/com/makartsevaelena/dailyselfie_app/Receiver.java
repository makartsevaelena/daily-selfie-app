package com.makartsevaelena.dailyselfie_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_camera)
                .setContentTitle("Daily Selfie")
                .setContentText("Time for another selfie")
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            notificationManager.notify(1, notification);
        } catch (NullPointerException e) {
            e.getStackTrace();
        }

    }
}
