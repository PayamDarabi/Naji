package ir.zelzele.pushNotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.util.Map;

import ir.zelzele.R;
import ir.zelzele.general.activities.MainActivity;

/**
 * Created by k.mohammadzadeh on 1/28/2017.
 */
public class Notification {
    Context context;
    int id;
    String title_fa;
    String message_fa;
    int type;
    int notifid;

    public Notification(Map<String, String> data) {

        exportData(data);
    }

    public Notification(int notifId, String Title, String Body) {
        this.title_fa = Title;
        this.message_fa = Body;
        this.notifid = notifId;
        //  exportData(data);
    }

    public Notification(JSONObject data) {

        exportData(data);
    }

    public Notification() {

    }

    protected void exportData(Map<String, String> data) {
        id = Integer.parseInt(data.get(NotificationDataConstance.NOTIF_ID));
        title_fa = data.get(NotificationDataConstance.TITLE_FA);
        message_fa = data.get(NotificationDataConstance.MESSAGE_FA);
        type = Integer.parseInt(data.get(NotificationDataConstance.TYPE));

    }

    protected void exportData(JSONObject data) {


    }

    public void doAction(Context context) {

    }

    protected PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        return pendingIntent;
    }

    public void show(Context context, NotificationManager notificationManager) {
        this.context = context;
        PendingIntent pendingIntent = getPendingIntent();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title_fa)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message_fa))
                .setContentText(message_fa)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        notificationManager.notify(id, notificationBuilder.build());
    }

    public String getTitle_fa() {
        return title_fa;
    }

    public void setTitle_fa(String title_fa) {
        this.title_fa = title_fa;
    }



    public String getMessage_fa() {
        return message_fa;
    }

    public void setMessage_fa(String message_fa) {
        this.message_fa = message_fa;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
