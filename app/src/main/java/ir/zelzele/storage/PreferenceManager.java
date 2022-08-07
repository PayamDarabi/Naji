package ir.zelzele.storage;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Payam on 7/27/2017.
 */

public class PreferenceManager {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    static PreferenceManager preferenceManager;


    public static PreferenceManager getInstance(Context conext) {
        if (preferenceManager != null) {
            return preferenceManager;
        } else {
            return new PreferenceManager(conext);
        }
    }

    private PreferenceManager(Context conext) {
        preferences = conext.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    public void setUsername(String username) {
        editor.putString("username", username).apply();
    }

    public String getUsername() {
        return preferences.getString("username", "");
    }

    public void setToken(String token) {
        editor.putString("token", token).apply();
    }

    public String getLastServerLatLng() {
        return preferences.getString("lastLatlng", "");
    }


    public void setLastServerLatLng(String lastLatlng) {
        editor.putString("lastLatlng", lastLatlng).apply();
    }


    public String getLastServerUpdateTime() {
        return preferences.getString("serverUpdateTime", "");
    }


    public void setLastServerUpdateTime(String serverUpdateTime) {
        editor.putString("serverUpdateTime", serverUpdateTime).apply();
    }

    public String getToken() {
        return preferences.getString("token", "");
    }


    public void setFirebaseToken(String firebaseToken) {
        preferences.edit().putString("firebaseToken", firebaseToken).commit();
    }

    public String getFirebaseToken() {
        return preferences.getString("firebaseToken", "");
    }

    public void setSendToServer(boolean sendToServer) {
        preferences.edit().putBoolean("sendToServer", sendToServer).commit();
    }

    public boolean isSendToServer() {
        return preferences.getBoolean("sendToServer", false);
    }

    public void setNajioN(boolean najIsOn) {
        preferences.edit().putBoolean("najIsOn", najIsOn).commit();
    }

    public boolean isNajiOn() {
        return preferences.getBoolean("najIsOn", false);
    }

    public void setRequestDate(Long now) {
        preferences.edit().putLong("lastRequest", now).commit();
    }

    public long lastRequest() {
        return preferences.getLong("lastRequest", 0);
    }


    public void setForceUpdate(boolean forceUpdate) {
        preferences.edit().putBoolean("forceUpdate", forceUpdate).commit();
    }

    public boolean isforceUpdate() {
        return preferences.getBoolean("forceUpdate", false);
    }

    public void setUpdate(boolean update) {
        preferences.edit().putBoolean("update", update).commit();
    }

    public boolean isUpdate() {
        return preferences.getBoolean("update", false);
    }


    public void setUpdateVersion(String updateVersion) {
        preferences.edit().putString("updateVersion", updateVersion).commit();
    }

    public String getUpdateVersion() {
        return preferences.getString("updateVersion", "");
    }

    public void setUpdateMessage(String updateMessage) {
        preferences.edit().putString("updateMessage", updateMessage).commit();
    }

    public String getUpdateMessage() {
        return preferences.getString("updateMessage", "");
    }

    public boolean isSeeIntro(){
        return preferences.getBoolean("intro",false);
    }
    public void setSeeIntro(boolean isSee)
    {
        preferences.edit().putBoolean("intro",isSee).commit();
    }

}