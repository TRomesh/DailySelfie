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

    @Override
    public void onReceive(Context context, Intent intent) {
        CreateNotification(context,"Daily Selfie Challenge!","Time to take a selfie","Alert");
    }

    public void CreateNotification(Context context,String a,String b,String c){

        notificationIntent = PendingIntent.getActivity(context,0,new Intent(context, CameraActivity.class),0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(a);
        mBuilder.setContentText(b);
        mBuilder.setContentIntent(notificationIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,mBuilder.build());

    }
}
