package com.ryunote.app.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ryunote.app.R;
import com.ryunote.app.activity.MainActivity;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    static private final String TAG = "Firebase Cloud Messaging";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if(message.getData().size() > 0) {
            generateNotification(getApplicationContext(), message.getData().get("title"), message.getData().get("body"));
        }if(message.getNotification() != null) {
            sendNotification(message.getNotification().getTitle(), message.getNotification().getBody());
        }
        super.onMessageReceived(message);
    }

    private void generateNotification(Context context, String title, String body) {
        try {

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "channel-fbase";
            String channelName = "demoFbase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }

            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setSmallIcon(R.drawable.logo);
                int color = 0x008000;
                mBuilder.setColor(color);
            } else {
                mBuilder.setSmallIcon(R.drawable.logo);
            }
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

            mBuilder.setContentTitle(title);
            mBuilder.setContentText(body);
            mBuilder.setContentIntent(pendingIntent);
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            notificationManager.notify(m,mBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(String title, String body) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "Gagal mengambil token Firebase Messaging", task.getException());
                            return;
                        }

                        String token = task.getResult();
                        FcmSenderTask senderTask = new FcmSenderTask(token,title, body);
                        senderTask.execute();
                    }
                });
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
    }


}