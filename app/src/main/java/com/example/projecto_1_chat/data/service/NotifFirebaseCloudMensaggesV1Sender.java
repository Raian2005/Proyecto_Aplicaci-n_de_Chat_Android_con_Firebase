package com.example.projecto_1_chat.data.service;

import android.content.Context;

import com.example.projecto_1_chat.R;
import com.google.auth.oauth2.GoogleCredentials;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

public class NotifFirebaseCloudMensaggesV1Sender {

    private static final String PROJECT_ID = "project-1-prog-mobile";
    private static final String FCM_V1_URL = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";

    public static void sendNotification(Context context, String targetToken, String title, String body) {
        new Thread(() -> {
            try {
                InputStream inputStream = context.getResources().openRawResource(R.raw.service_account);
                GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
                        .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));

                credentials.refreshIfExpired();
                String accessToken = credentials.getAccessToken().getTokenValue();

                URL url = new URL(FCM_V1_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                JSONObject notification = new JSONObject();
                notification.put("title", title);
                notification.put("body", body);

                JSONObject message = new JSONObject();
                message.put("token", targetToken);
                message.put("notification", notification);

                JSONObject payload = new JSONObject();
                payload.put("message", message);

                OutputStream os = conn.getOutputStream();
                os.write(payload.toString().getBytes("UTF-8"));
                os.close();

                int responseCode = conn.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}