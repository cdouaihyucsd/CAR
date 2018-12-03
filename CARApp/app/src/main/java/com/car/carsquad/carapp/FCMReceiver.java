package com.car.carsquad.carapp;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

public class FCMReceiver extends FirebaseMessagingService {
    private static final String TAG = "FCMReceiver";
    private static final String SERVER_URL = "http://acsweb.ucsd.edu/~jdfreili/car/handlerequests.php";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            boolean scheduleLater = false; // TODO: add some functionality
            if(scheduleLater) {
                scheduleJob();
            } else {
                handleNow();
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notifaction Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody(), getApplicationContext());
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void scheduleJob() {
        // TODO scheduler?
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done");
    }

    private void sendRegistrationToServer(String token) {
        ServerUploader uploader = new ServerUploader(getApplicationContext());
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", token);
        uploader.addRequest(SERVER_URL, hm);
    }

    private void sendNotification(String messageBody, Context context) {
//        Intent intent = new Intent(this, context.getClass());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_ONE_SHOT);
//        Log.d(TAG, "NOTIFICATION shown");
//        Toast.makeText(context, messageBody, Toast.LENGTH_LONG).show();
        Log.d(TAG, "sending notification");

        // TODO: add notifier
    }
}
