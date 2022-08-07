package ir.zelzele.pushNotification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import java.util.Map;

import ir.zelzele.general.activities.NajiActivity;

public class ZelzeleNotification extends Notification {
    String url;

    public ZelzeleNotification(Map<String, String> data) {
        super(data);

    }

    public ZelzeleNotification(JSONObject data) {
        super(data);

    }

    @Override
    public void doAction(Context context) {

    }

    protected PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, NajiActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        return pendingIntent;
    }


}
