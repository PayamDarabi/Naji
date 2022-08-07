package ir.zelzele;

/**
 * Created by Payam on 8/2/2017.
 */

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ir.zelzele.storage.PreferenceManager;
import ir.zelzele.utils.AppInfoProvider;
import ir.zelzele.webservices.ApiMethodCaller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        PreferenceManager.getInstance(getApplicationContext()).setFirebaseToken(refreshedToken);
        PreferenceManager.getInstance(getApplicationContext()).setSendToServer(false);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        ApiMethodCaller.getInstance(false,getApplicationContext()).sendFcmToken("1", token, AppInfoProvider.getDeviceName(),
                "android" + AppInfoProvider.getAndroidApiVersion(), new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.body().intValue()==1) {
                            PreferenceManager.getInstance(getApplicationContext()).setSendToServer(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        int x=0;
                    }
                });
        // TODO: Implement this method to send token to your app server.
    }
}