package com.sliit.dailyselfie.AlertReciver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.sliit.dailyselfie.Camera.CameraActivity;
import com.sliit.dailyselfie.R;

/**
 * Created by Tharaka on 04/05/2016.
 */
public class AlarmReciver extends BroadcastReceiver {

    PendingIntent notificationIntent;
    Bundle extras;

    @Override
    public void onReceive(Context context, Intent intent) {
        extras=intent.getExtras();
        CreateNotification(context,"Daily Selfie Challenge","Time to take a selfie", extras.get("Category").toString());
    }

    private void CreateNotification(Context context, String s, String s1, String alert) {


        notificationIntent = PendingIntent.getActivity(context,0,new Intent(context, CameraActivity.class).putExtra("Challenge",alert),0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(s);
        mBuilder.setContentText(s1);
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        mBuilder.setContentIntent(notificationIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,mBuilder.build());

    }
}
