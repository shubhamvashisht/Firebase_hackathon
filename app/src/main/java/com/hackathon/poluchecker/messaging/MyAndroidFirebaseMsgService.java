package com.hackathon.poluchecker.messaging;

/**
 * Created by shivesh on 21/2/17.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hackathon.poluchecker.MainActivity;
import com.hackathon.poluchecker.R;
import com.hackathon.poluchecker.utl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
     public static Activity act;
     @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat
        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

             //create notification
        ctx=this;
        boolean chkchat=true,chklist=true;
        utl.init(ctx);



    }

    Gson js;
    Context ctx;

    private void createNotification( RemoteMessage remoteMessage) {
        try {
         //   String mess=remoteMessage.getNotification().getBody();


            Intent intent = new Intent( this , MainActivity. class );


            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String dd="";

            boolean isLoggedIn=(utl.readData(ctx)!=null);
            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification.Builder b = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ayi)
                    .setContentTitle(""+remoteMessage.getData().get("title"))
                    //.setColor(getResources().getColor(R.color.green_400))
                    .setContentText(""+remoteMessage.getData().get("text"))
                    .setAutoCancel( true )
                    .setSound(notificationSoundURI)
                    .setContentInfo("Wootout")
                    .setContentIntent(isLoggedIn?resultIntent: PendingIntent.getActivity( this , 0, new Intent(ctx, MainActivity.class),
                            PendingIntent.FLAG_ONE_SHOT));


            Notification n = NotificationExtras.buildWithBackgroundColor(ctx, b, getResources().getColor(R.color.green_400));
            // Notification n = NotificationExtras.buildWithBackgroundResource(this, b, R.drawable.bg_notification);
            NotificationManagerCompat.from(this).notify(1, n);



           /// notificationManager.notify(0, mNotificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.toLowerCase().contains(context.getPackageName())||
                                context.getPackageName().toLowerCase().contains(activeProcess.toLowerCase())
                                ) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().toLowerCase().contains(context.getPackageName())||
                    context.getPackageName().toLowerCase().contains(componentInfo.getPackageName().toLowerCase())
                    ) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }



}
