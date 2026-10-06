package com.example.projecto_1_chat.data.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.projecto_1_chat.R;
import com.example.projecto_1_chat.presentation.view.App;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationMessagingService extends FirebaseMessagingService {
    public NotificationMessagingService() {
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        com.google.firebase.auth.FirebaseAuth auth = com.google.firebase.auth.FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            String uid = auth.getCurrentUser().getUid();
            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .update("token", token);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if(message.getNotification() != null){
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();

            mostrarNotificacion(this, title, body);
        }
    }

    public static void mostrarNotificacion(Context context, String title, String body){
        String channelId = "notificacion_channel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Notificacion de Chat",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Canal para notificacion de mensajes");

            if(notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }

        Intent intent = new Intent(context, App.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        if(notificationManager != null){
            int notificationId = (int) System.currentTimeMillis();
            notificationManager.notify(notificationId, builder.build());
        }

    }

}