package ir.zelzele.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Payam on 12/29/2017.
 */

public class SendLocation {
    @SerializedName("APP_NAME")
    @Expose
    private String appName;
    @SerializedName("ERROR")
    @Expose
    private String error;
    @SerializedName("CURRENT_TIME")
    @Expose
    private String currentTime;
    @SerializedName("TOKEN")
    @Expose
    private String token;


    @SerializedName("STATUS")
    @Expose
    private String status;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @SerializedName("RESULT")
    @Expose
    private String result;

}
