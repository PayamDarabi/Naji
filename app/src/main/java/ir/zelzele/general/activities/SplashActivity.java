package ir.zelzele.general.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Locale;

import ir.zelzele.R;
import ir.zelzele.classes.MessagesDialogYesNo;
import ir.zelzele.customview.CustomButtonView;
import ir.zelzele.models.LoginResponse;
import ir.zelzele.storage.PreferenceManager;
import ir.zelzele.utils.AppInfoProvider;
import ir.zelzele.utils.Tools;
import ir.zelzele.webservices.ApiMethodCaller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.State;
import com.github.jorgecastillo.listener.OnStateChangeListener;*/

public class SplashActivity extends AppCompatActivity/* implements OnStateChangeListener */ {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static boolean needUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                Configuration configuration = getResources().getConfiguration();
                configuration.setLayoutDirection(new Locale("fa"));

                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
            }
            checkForUpdate();

            final LinearLayout layout_tryAgain = (LinearLayout) findViewById(R.id.layout_tryAgain);
            final CustomButtonView btn_tryAgain = (CustomButtonView) findViewById(R.id.btn_try_again);

            if (Tools.internetConnect()) {
                if (!needUpdate) {
                    layout_tryAgain.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (PreferenceManager.getInstance(getApplicationContext()).isSeeIntro()) {
                                if (PreferenceManager.getInstance(getApplicationContext()).getToken().equals("")) {
                                    final String device_name = AppInfoProvider.getDeviceName();
                                    ApiMethodCaller.getInstance(false, getApplicationContext()).signIn(
                                            "",
                                            "",
                                            "",
                                            device_name,
                                            "android",
                                            "",
                                            new Callback<LoginResponse>() {
                                                @Override
                                                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                                    try {
                                                        LoginResponse responses = response.body();
                                                        PreferenceManager.getInstance(getApplicationContext()).setUsername("");
                                                        if (responses.getToken().isEmpty())
                                                            PreferenceManager.getInstance(getApplicationContext())
                                                                    .setToken("h42nsjk98kkdiwjd7hbw6ha0m75bgf1");
                                                        else
                                                            PreferenceManager.getInstance(getApplicationContext()).setToken(responses.getToken());
                                                    }
                                                    catch (Exception e){
                                                        e.printStackTrace();
                                                        PreferenceManager.getInstance(getApplicationContext())
                                                                .setToken("h42nsjk98kkdiwjd7hbw6ha0m75bgf1");
                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<LoginResponse> call, Throwable t) {
                                                    PreferenceManager.getInstance(getApplicationContext()).setUsername("");
                                                    PreferenceManager.getInstance(getApplicationContext()).setToken("h42nsjk98kkdiwjd7hbw6ha0m75bgf1");
                                                }
                                            });
                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    finish();

                                    // }
                                }
                                else {
                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    finish();
                                }
                            } else {
                                startActivity(new Intent(SplashActivity.this, IntroductionActivity.class));
                                finish();
                            }
                        }
                    }, 3000);
                }
            } else {
                Toast.makeText(this, R.string.internet_Fail, Toast.LENGTH_LONG).show();
                layout_tryAgain.setVisibility(View.VISIBLE);
            }

            btn_tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout_tryAgain.setVisibility(View.GONE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Tools.internetConnect()) {
                                if (!needUpdate) {

                                    if (//!PreferenceManager.getInstance(getApplicationContext()).getUsername().equals("") &&
                                            PreferenceManager.getInstance(getApplicationContext()).getToken().equals("")) {
                                        final String device_name = AppInfoProvider.getDeviceName();
                                        ApiMethodCaller.getInstance(false, getApplicationContext()).signIn(
                                                "",
                                                "",
                                                "",
                                                device_name,
                                                "android",
                                                "",
                                                new Callback<LoginResponse>() {
                                                    @Override
                                                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                                        LoginResponse responses = response.body();
                                                        PreferenceManager.getInstance(getApplicationContext()).setUsername("");
                                                        PreferenceManager.getInstance(getApplicationContext()).setToken(responses.getToken());

                                                    }

                                                    @Override
                                                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                                                        PreferenceManager.getInstance(getApplicationContext()).setUsername("");
                                                        PreferenceManager.getInstance(getApplicationContext()).setToken("h42nsjk98kkdiwjd7hbw6ha0m75bgf1");
                                                    }
                                                });
                                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                        finish();

                                    } else {
                                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                        finish();
                                    }

                                }
                            } else {
                                Toast.makeText(SplashActivity.this, R.string.internet_Fail, Toast.LENGTH_LONG).show();
                                layout_tryAgain.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void checkForUpdate() {
        if (PreferenceManager.getInstance(this).isUpdate()) {
            if (Integer.valueOf(PreferenceManager.getInstance(this).getUpdateVersion()) > AppInfoProvider.getAppVersion(this))
                showUpdateDialog(PreferenceManager.getInstance(this).isforceUpdate());
        }
    }

    public void showUpdateDialog(final boolean isForce) {
        needUpdate = true;
        final MessagesDialogYesNo ms = new MessagesDialogYesNo(this,
                PreferenceManager.getInstance(this).getUpdateMessage() +
                        System.getProperty("line.separator")
                , R.string.update_app, R.string.cancel
        );
        ms.setMessageDialogButton(new MessagesDialogYesNo.MessagesDialogButtons() {
            @Override
            public void AcceptButtonClicked() {
                String url = getString(R.string.bazarLink);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                ms.dismiss();
                finish();
            }

            @Override
            public void CancleButtonClicked() {
                ms.dismiss();
                if (isForce)
                    finish();
                else {
                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        });
        ms.show();
    }
}