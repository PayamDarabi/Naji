package ir.zelzele;

/**
 * Created by Payam on 8/2/2017.
 */


import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ir.zelzele.pushNotification.Notification;
import ir.zelzele.pushNotification.NotificationFactory;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData() != null) {
            Notification notification = NotificationFactory.createNotification(remoteMessage.getData());
            Log.i("TAGTAGATGAT", notification.getType() + "");
            notification.doAction(getApplicationContext());
            notification.show(getApplicationContext(),
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));

        }
    }
}