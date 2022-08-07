package ir.zelzele.pushNotification;

import java.util.Map;

/**
 * Created by k.mohammadzadeh on 1/29/2017.
 */
public class NotificationFactory {

    public static Notification createNotification(Map<String,String> data){
        switch (Integer.parseInt(data.get(NotificationDataConstance.TYPE))){
            case NotificationType.NOTIF_TYPE_INFO:
                return new Notification(data);
            case NotificationType.NOTIF_TYPE_FORCEUPDATE:
                return new UpdateNotification(data);
            case NotificationType.NOTIF_TYPE_UPDATE:
                return new UpdateNotification(data);

            case NotificationType.NOTIF_ZELZELE:
                return new ZelzeleNotification(data);

            default:
                return new Notification(data);
        }

    }
}
