package ir.zelzele.pushNotification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import ir.zelzele.storage.PreferenceManager;


public class UpdateNotification extends Notification {

    String url;
    String version;

    public UpdateNotification(Map<String, String> data) {
        super(data);
        url = "https://cafebazaar.ir/app/ir.zelzele/?l=fa";
        version=data.get(NotificationDataConstance.VERSION);
    }

    public UpdateNotification(JSONObject data) {
        super(data);
        try {

            url = "https://cafebazaar.ir/app/ir.zelzele/?l=fa";
            version = data.getString(NotificationDataConstance.VERSION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doAction(Context context) {
        if (type == NotificationType.NOTIF_TYPE_FORCEUPDATE)
            PreferenceManager.getInstance(context).setForceUpdate(true);
            PreferenceManager.getInstance(context).setUpdateVersion(version);
            PreferenceManager.getInstance(context).setUpdate(true);
            PreferenceManager.getInstance(context).setUpdateMessage(message_fa);
        if (type == NotificationType.NOTIF_TYPE_UPDATE)
        {
            PreferenceManager.getInstance(context).setUpdateMessage(message_fa);
            PreferenceManager.getInstance(context).setForceUpdate(false);
            PreferenceManager.getInstance(context).setUpdate(true);
            PreferenceManager.getInstance(context).setUpdateVersion(version);
        }
    }

    @Override
    protected PendingIntent getPendingIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        return pendingIntent;
    }

   /* @Override
    public void show(Context context, NotificationManager notificationManager) {
        super.show(context, notificationManager);
      *//*  if (type == NotificationType.NOTIF_TYPE_FORCE_UPDATE) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }*//*
    }*/
}
