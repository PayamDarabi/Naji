package ir.zelzele.webservices;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ir.zelzele.BuildConfig;
import ir.zelzele.models.LearningTips;
import ir.zelzele.models.LoginResponse;
import ir.zelzele.models.SendLocation;
import ir.zelzele.models.Target;
import ir.zelzele.storage.PreferenceManager;
import ir.zelzele.utils.AppInfoProvider;
import ir.zelzele.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Novin Pendar on 1/13/2016.
 */
public class ApiMethodCaller {


    private static ApiMethodCaller apiMethodCaller;

    private static Retrofit retrofit;
    private static ApiCaller apiCaller;
    private static Context mContext;
    private ApiMethodCaller() {
    }

    public static ApiMethodCaller getInstance(boolean getjson, Context context) {
        if (apiMethodCaller == null) {
            mContext = context;

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(50, TimeUnit.SECONDS);
            httpClient.connectTimeout(50, TimeUnit.SECONDS);
            if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(logging);
            }
            Gson gson = new GsonBuilder()
                   /* .registerTypeAdapterFactory(new DeviceObjectAdapterFactory())*/
                    .excludeFieldsWithModifiers(Modifier.FINAL,
                            Modifier.TRANSIENT,
                            Modifier.STATIC)
                    .create();

            apiMethodCaller = new ApiMethodCaller();
            if (!getjson)
                retrofit = new Retrofit.Builder().client(httpClient.build())
                        .baseUrl(AppInfoProvider.getBaseURL())
                        .addConverterFactory(GsonConverterFactory.create(
                                gson))
                        .build();
            else
                retrofit = new Retrofit.Builder().client(httpClient.build())
                        .baseUrl(AppInfoProvider.getBaseURL())
                        .build();
            apiCaller = retrofit.create(ApiCaller.class);
        }
        return apiMethodCaller;
    }

    public Call<List<Target>> getTargetList(String state, String device_name,
                                            String device_type
            , Callback<List<Target>> callback) {
        Call<List<Target>> caller = apiCaller.getTargetList("/events",
                String.valueOf(AppInfoProvider.getAppVersion(mContext)),
                PreferenceManager.getInstance(mContext).getToken(),
                Constants.Api_key,
                state, device_name, device_type);
        caller.enqueue(callback);
        return caller;

    }

    public Call<Integer> sendFcmToken(String topic_id, String notificationToken, String device_name,
                                      String device_type
            , Callback<Integer> callback) {
        Call<Integer> caller = apiCaller.sendFcmToken("/earth/token",
                String.valueOf(AppInfoProvider.getAppVersion(mContext)),
                PreferenceManager.getInstance(mContext).getToken(),
                Constants.Api_key,
                topic_id, notificationToken
                , device_name, device_type);
        caller.enqueue(callback);
        return caller;

    }

    public Call<LoginResponse> signIn(String username, String fname, String lname, String device_name,
                                      String device_type,String acCode
            , Callback<LoginResponse> callback) {
        Call<LoginResponse> caller = apiCaller.SignIn("/earth/auth",
                String.valueOf(AppInfoProvider.getAppVersion(mContext)),
                PreferenceManager.getInstance(mContext).getToken(),
                Constants.Api_key,
                username, fname,lname
                , device_name, device_type,acCode);
        caller.enqueue(callback);
        return caller;

    }

    public Call<SendLocation> sendUserLocationToServer(String username, String latitude, String longitude, String fcmToken,
                                                       String device_name, String device_type
            , Callback<SendLocation> callback) {
        Call<SendLocation> caller = apiCaller.SendUserLocationToserver("/earth/latlong",
                String.valueOf(AppInfoProvider.getAppVersion(mContext)),
                PreferenceManager.getInstance(mContext).getToken(),
                Constants.Api_key,username,
                latitude,longitude,fcmToken, device_name, device_type);
        caller.enqueue(callback);
        return caller;

    }

    public Call<List<LearningTips>> getLearningTips(String device_name, String device_type
            , Callback<List<LearningTips>> callback) {
        Call<List<LearningTips>> caller = apiCaller.getLearningTips("/earth/learningData",
                String.valueOf(AppInfoProvider.getAppVersion(mContext)),
                PreferenceManager.getInstance(mContext).getToken(),
                Constants.Api_key,
                device_name, device_type);
        caller.enqueue(callback);
        return caller;

    }
}
