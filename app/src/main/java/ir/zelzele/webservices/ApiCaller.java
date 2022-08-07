package ir.zelzele.webservices;


import java.util.List;

import ir.zelzele.models.LearningTips;
import ir.zelzele.models.LoginResponse;
import ir.zelzele.models.SendLocation;
import ir.zelzele.models.Target;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;


/**
 * Created by payam on 12/24/2017
 */
public interface ApiCaller {

    /* @Headers({
             "version: 0",
             "token: h42nsjk98kkdiwjd7hbw6ha0m75bgf1",
             "app_key: JetEqIr1396"
     })*/
    @FormUrlEncoded
    @POST
    public Call<List<Target>> getTargetList(@Url String path,
                                            @Header("version") String version,
                                            @Header("token") String token,
                                            @Header("app_key") String app_key,

                                            @Field("state") String state,
                                            @Field("device_name") String device_name,
                                            @Field("device_type") String device_type

    );

    /* @Headers({
             "version: 0",
             "token: h42nsjk98kkdiwjd7hbw6ha0m75bgf1",
             "app_key: JetEqIr1396"
     })*/
    @FormUrlEncoded
    @POST
    public Call<Integer> sendFcmToken(@Url String path,
                                      @Header("version") String version,
                                      @Header("token") String token,
                                      @Header("app_key") String app_key,
                                      @Field("topic_id") String topic_id,
                                      @Field("notification_token") String notification_token,
                                      @Field("device_name") String device_name,
                                      @Field("device_type") String device_type

    );

    /*
        @Headers({
                "version: 0",
                "token: h42nsjk98kkdiwjd7hbw6ha0m75bgf1",
                "app_key: JetEqIr1396"
        })*/
    @FormUrlEncoded
    @POST
    public Call<LoginResponse> SignIn(@Url String path,
                                      @Header("version") String version,
                                      @Header("token") String token,
                                      @Header("app_key") String app_key,
                                      @Field("username") String username,
                                      @Field("fname") String fname,
                                      @Field("lname") String lname,
                                      @Field("device_name") String device_name,
                                      @Field("device_type") String device_type,
                                      @Field("ac_code") String ac_code
    );


    /*  @Headers({
              "version: 0",
              "token: h42nsjk98kkdiwjd7hbw6ha0m75bgf1",
              "app_key: JetEqIr1396"
      })*/
    @FormUrlEncoded
    @POST
    public Call<SendLocation> SendUserLocationToserver(@Url String path,
                                                       @Header("version") String version,
                                                       @Header("token") String token,
                                                       @Header("app_key") String app_key,
                                                       @Field("username") String username,
                                                       @Field("latitude") String latitude,
                                                       @Field("longitude") String longitude,
                                                       @Field("token") String fcmToken,
                                                       @Field("device_name") String device_name,
                                                       @Field("device_type") String device_type
    );

    /* @Headers({
             "version: 0",
             "token: h42nsjk98kkdiwjd7hbw6ha0m75bgf1",
             "app_key: JetEqIr1396"
     })*/
    @FormUrlEncoded
    @POST
    public Call<List<LearningTips>> getLearningTips(@Url String path,
                                                    @Header("version") String version,
                                                    @Header("token") String token,
                                                    @Header("app_key") String app_key,
                                                    @Field("device_name") String device_name,
                                                    @Field("device_type") String device_type
    );
}