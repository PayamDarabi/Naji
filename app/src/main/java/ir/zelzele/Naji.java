package ir.zelzele;

import android.app.Application;
import android.content.res.Configuration;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatDelegate;

import ir.zelzele.classes.FontsOverride;

/**
 * Created by Payam on 12/29/2017.
 */

public class Naji  extends Application {
    private static Naji mInstance;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        try {
            mInstance = this;
            FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/parastoo.ttf");
            FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/parastoo.ttf");
            FontsOverride.setDefaultFont(this, "SERIF", "fonts/parastoo.ttf");
            FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/parastoo.ttf");
            // Required initialization logic here!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Naji getInstance() {
        return mInstance;
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}