package ir.zelzele.pushNotification;

import android.app.PendingIntent;
import android.content.Context;

import java.util.HashMap;

/**
 * Created by k.mohammadzadeh on 1/28/2017.
 */
public class NotificationDataConstance {




    public static final String TITLE_FA="title";
    public static final String MESSAGE_FA="message";
    public static final String NOTIF_ID="notif_id";
    public static final String URL="url";
    public static final String TYPE="type";
    public static final String VERSION="version";







    public static PendingIntent getPendingIntent(Context context, HashMap<String,String> data){
        switch (Integer.parseInt(data.get(TYPE))){

        }
        return null;
    }



}
